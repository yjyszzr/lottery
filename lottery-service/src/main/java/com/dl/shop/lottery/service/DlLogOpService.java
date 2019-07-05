package com.dl.shop.lottery.service;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.DlOpLogDTO;
import com.dl.lottery.dto.LogPicDetailDTO;
import com.dl.lottery.dto.OperationRecordDTO;
import com.dl.shop.lottery.dao.DlOpLogMapper;
import com.dl.shop.lottery.model.DlOpLog;
import com.dl.shop.lottery.model.NewDlOpLog;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DlLogOpService {

	@Resource
	private DlOpLogMapper dlOpLogMapper;

	public BaseResult<OperationRecordDTO> queryLogByTime(Integer pageNum, Integer pageSize, String phone, Integer startTime, Integer endTime) {
		log.info("queryLogByTime_pageNum==========={}", pageNum);
		log.info("queryLogByTime_pageSize==========={}", pageSize);
		log.info("queryLogByTime_phone==========={}", phone);
		log.info("queryLogByTime_startTime==========={}", startTime);
		log.info("queryLogByTime_endTime==========={}", endTime);
		OperationRecordDTO dto = new OperationRecordDTO();
		List<NewDlOpLog> logList = dlOpLogMapper.queryLogByTime(phone, startTime, endTime);
		log.info("queryLogByTime_logList==========={}", logList);
		BigDecimal sucMoney = BigDecimal.ZERO;
		Integer sucNum = 0;
		Integer failNum = 0;
		if (!CollectionUtils.isEmpty(logList)) {
			sucMoney = logList.stream().filter(s -> s.getOpType() == 1).map(s -> s.getMoneyPaid().add(s.getBonus())).reduce(BigDecimal.ZERO, BigDecimal::add);
			sucNum = logList.stream().filter(s -> s.getOpType() == 1).collect(Collectors.toList()).size();
			failNum = logList.stream().filter(s -> s.getOpType() == 2).collect(Collectors.toList()).size();
		} else {
			return ResultGenerator.genSuccessResult("success", dto);
		}

		PageHelper.startPage(pageNum, pageSize);
		List<NewDlOpLog> logListAll = dlOpLogMapper.queryLogByTime(phone, startTime, endTime);
		PageInfo<NewDlOpLog> pageInfo = new PageInfo<NewDlOpLog>(logListAll);
		List<DlOpLogDTO> opDTOList = new ArrayList<DlOpLogDTO>();
		logListAll.stream().forEach(s -> {
			DlOpLogDTO logDTO = new DlOpLogDTO();
			logDTO.setOrderSn(s.getOrderSn());
			logDTO.setOptType(String.valueOf(s.getOpType()));
			logDTO.setLotteryClassifyId(String.valueOf(s.getLotteryClassifyId()));
			opDTOList.add(logDTO);
		});
		PageInfo<DlOpLogDTO> printRecordList = new PageInfo<DlOpLogDTO>();
		try {
			BeanUtils.copyProperties(printRecordList, pageInfo);
		} catch (Exception e) {
		}

		printRecordList.setList(opDTOList);
		dto.setOpList(printRecordList);
		dto.setSucNum(String.valueOf(sucNum));
		dto.setFailNum(String.valueOf(failNum));
		dto.setSucMoney(sucMoney.toString());
		if (opDTOList != null) {
			for (DlOpLogDTO logDTO : opDTOList) {
				String classifyId = logDTO.getLotteryClassifyId();
				if ("1".equals(classifyId)) {
					logDTO.setLogo("https://szcq-icon.oss-cn-beijing.aliyuncs.com/jingzu.png");
				} else if ("2".equals(classifyId)) {
					logDTO.setLogo("https://szcq-icon.oss-cn-beijing.aliyuncs.com/daletou.png");
				} else if("3".equals(classifyId)){
                    logDTO.setLogo("hhttps://szcq-icon.oss-cn-beijing.aliyuncs.com/lancai.png");
                }
			}
		}
		log.info("queryLogByTime_dto==========={}", dto);
		return ResultGenerator.genSuccessResult("success", dto);
	}

	/**
	 * 根据订单号查询操作日志
	 * 
	 * @param orderSn
	 * @return
	 */
	public BaseResult<LogPicDetailDTO> queryLogOpByOrderSn(String orderSn) {
		LogPicDetailDTO dto = new LogPicDetailDTO();
		DlOpLog dlLog = dlOpLogMapper.queryLogByOrderSn(orderSn);
		if (null == dlLog) {
			return ResultGenerator.genSuccessResult("未查询到该订单的彩票照片", dto);
		}
		dto.setPicUrl(dlLog.getPic());
		dto.setOptType(String.valueOf(dlLog.getOpType()));
		dto.setDateStr(DateUtil.getTimeString(dlLog.getAddTime(), DateUtil.datetimeFormat));
		dto.setFailReason(dlLog.getFailMsg());
		return ResultGenerator.genSuccessResult("success", dto);
	}

}
