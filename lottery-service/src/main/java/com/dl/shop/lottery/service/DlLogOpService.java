package com.dl.shop.lottery.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DlOpLogDTO;
import com.dl.lottery.dto.OperationRecordDTO;
import com.dl.shop.lottery.dao.DlOpLogMapper;
import com.dl.shop.lottery.model.DlOpLog;


@Service
public class DlLogOpService {
	
	@Resource
	private DlOpLogMapper dlOpLogMapper;
	
	public BaseResult<OperationRecordDTO> queryLogByTime(String phone,Integer startTime,Integer endTime){
		List<DlOpLog> logList = dlOpLogMapper.queryLogByTime(phone,startTime, endTime);
		List<DlOpLogDTO> opDTOList = new ArrayList<DlOpLogDTO>();
		logList.stream().forEach(s->{
			DlOpLogDTO logDTO = new DlOpLogDTO();
			logDTO.setOrderSn(s.getOrderSn());
			logDTO.setOptType(String.valueOf(s.getOpType()));
			logDTO.setLotteryClassifyId(String.valueOf(s.getLotteryClassifyId()));
			opDTOList.add(logDTO);
		});
		
		BigDecimal sucMoney = BigDecimal.ZERO;
		Integer sucNum = 0;
		Integer failNum = 0;
		if(!CollectionUtils.isEmpty(logList)) {
			sucMoney = logList.stream().filter(s->s.getOpType() == 1).map(s->s.getMoneyPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);
			sucNum = logList.stream().filter(s->s.getOpType() == 1).collect(Collectors.toList()).size();
			failNum = logList.stream().filter(s->s.getOpType() == 2).collect(Collectors.toList()).size();
		}
		OperationRecordDTO dto = new OperationRecordDTO();
		dto.setOpList(opDTOList);
		dto.setSucNum(String.valueOf(sucNum));
		dto.setFailNum(String.valueOf(failNum));
		dto.setSucMoney(sucMoney.toString());
		
		return ResultGenerator.genSuccessResult("success", dto);
	}

}
