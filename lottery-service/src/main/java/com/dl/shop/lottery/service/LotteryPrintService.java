package com.dl.shop.lottery.service;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.MD5Utils;
import com.dl.base.util.SNGenerator;
import com.dl.lottery.dto.DlQueryAccountDTO;
import com.dl.lottery.dto.DlQueryIssueDTO;
import com.dl.lottery.dto.DlQueryIssueDTO.QueryIssue;
import com.dl.lottery.dto.DlQueryPrizeFileDTO;
import com.dl.lottery.dto.DlQueryStakeDTO;
import com.dl.lottery.dto.DlQueryStakeDTO.BackQueryStake;
import com.dl.lottery.dto.DlQueryStakeFileDTO;
import com.dl.lottery.dto.DlToStakeDTO;
import com.dl.lottery.dto.DlToStakeDTO.BackOrderDetail;
import com.dl.lottery.dto.LotteryPrintDTO;
import com.dl.lottery.param.DlCallbackStakeParam;
import com.dl.lottery.param.DlCallbackStakeParam.CallbackStake;
import com.dl.lottery.param.DlQueryAccountParam;
import com.dl.lottery.param.DlQueryIssueParam;
import com.dl.lottery.param.DlQueryPrizeFileParam;
import com.dl.lottery.param.DlQueryStakeFileParam;
import com.dl.lottery.param.DlQueryStakeParam;
import com.dl.lottery.param.DlToStakeParam;
import com.dl.order.api.IOrderService;
import com.dl.order.param.LotteryPrintParam;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.model.LotteryPrint;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class LotteryPrintService extends AbstractService<LotteryPrint> {
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private RestTemplate restTemplate;  

	@Value("${print.ticket.url}")
	private String printTicketUrl;
	
	@Value("${print.ticket.merchant}")
	private String merchant;
	
	@Value("${print.ticket.merchantPassword}")
	private String merchantPassword;
	
	@Value("${spring.datasource.druid.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.druid.username}")
	private String dbUserName;
	
	@Value("${spring.datasource.druid.password}")
	private String dbPass;
	
	@Value("${spring.datasource.druid.driver-class-name}")
	private String dbDriver;
	
	/**
	 * 投注接口（竞彩足球，game参数都是T51）
	 * @return
	 */
	public DlToStakeDTO toStake(DlToStakeParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/stake");
		JSONObject backJo = JSONObject.fromObject(backStr);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("orders", BackOrderDetail.class);
		DlToStakeDTO dlToStakeDTO = (DlToStakeDTO) JSONObject.toBean(backJo, DlToStakeDTO.class, mapClass); 
		return dlToStakeDTO;
	}
	
	/**
	 * 回调参数
	 * @param param
	 */
	public void callbackStake(DlCallbackStakeParam param) {
		log.info("************************callbackStake***********订单出票回调");
		List<CallbackStake> callbackStakes = param.getOrders();
		if(CollectionUtils.isNotEmpty(callbackStakes)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<LotteryPrintParam> lotteryPrintParams = new LinkedList<LotteryPrintParam>();
			List<LotteryPrint> lotteryPrints = new LinkedList<>();
			for(CallbackStake callbackStake : callbackStakes) {
				if(ProjectConstant.CALLBACK_STAKE_SUCCESS.equals(callbackStake.getPrintStatus())) {
					LotteryPrint lotteryPrint = new LotteryPrint();
					lotteryPrint.setTicketId(callbackStake.getTicketId());
					lotteryPrint = lotteryPrintMapper.selectOne(lotteryPrint);
					if(null != lotteryPrint && lotteryPrint.getStatus() == 3) {
						lotteryPrint.setStatus(1);
						lotteryPrint.setPlatformId(callbackStake.getPlatformId());
						lotteryPrint.setPrintNo(callbackStake.getPrintNo());
						lotteryPrint.setPrintSp(callbackStake.getSp());
						lotteryPrint.setPrintStatus(callbackStake.getPrintStatus());
						Date printTime = null;
						try {
							String printTimeStr = callbackStake.getPrintTime();
							printTimeStr = printTimeStr.replaceAll("/", "-");
							printTime = sdf.parse(printTimeStr);
							lotteryPrint.setPrintTime(printTime);
						} catch (ParseException e) {
							e.printStackTrace();
							log.error("订单编号：" + callbackStake.getTicketId() + "，出票回调，时间转换异常");
							continue;
						}
						lotteryPrints.add(lotteryPrint);
						LotteryPrintParam lotteryPrintParam = new LotteryPrintParam();
						lotteryPrintParam.setOrderSn(lotteryPrint.getOrderSn());
						lotteryPrintParam.setAcceptTime(lotteryPrint.getAcceptTime());
						lotteryPrintParam.setTicketTime(DateUtil.getCurrentTimeLong(printTime.getTime()/1000));
						lotteryPrintParam.setPrintSp(getComparePrintSp(callbackStake.getSp(), callbackStake.getTicketId()));
						lotteryPrintParams.add(lotteryPrintParam);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lotteryPrints)) {
				updateLotteryPrintByCallBack(lotteryPrints);
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintParams)) {
				orderService.updateOrderInfoByPrint(lotteryPrintParams);
			}
		}
	}
	
	/**
	 * 比较回调和主动查询的赔率是否一致，如果不一致，以主动查询成功的结果为准
	 * @param callBackSp
	 * @param issue
	 * @return
	 */
	private String getComparePrintSp(String callBackSp, String ticketId) {
		DlQueryStakeParam param = new DlQueryStakeParam();
		param.setMerchant(merchant);
		String[] orders = new String[1];
		orders[0] = ticketId;
		param.setOrders(orders);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		param.setTimestamp(sdf.format(new Date()));
		param.setVersion("1.0");
		DlQueryStakeDTO dlQueryStakeDTO = queryStake(param);
		if(!dlQueryStakeDTO.getRetCode().equals("0")) {
			return callBackSp;
		}
		List<BackQueryStake> backQueryStakes = dlQueryStakeDTO.getOrders();
		if(CollectionUtils.isEmpty(backQueryStakes)) {
			return callBackSp;
		}
		BackQueryStake backQueryStake = backQueryStakes.get(0);
		if(null == backQueryStake || backQueryStake.getPrintStatus() != 16) {
			return callBackSp;
		}
		if(StringUtils.isNotEmpty(callBackSp) && StringUtils.isNotEmpty(backQueryStake.getSp())) {
			if(callBackSp.equals(backQueryStake.getSp())) {
				return callBackSp;
			} else {
				return backQueryStake.getSp();
			}
		} else if(StringUtils.isNotEmpty(callBackSp)) {
			return callBackSp;
		}
		
		return backQueryStake.getSp();
	}
	
	/**
	 * 高速批量更新LotteryPrint
	 * @param list
	 */
	public void updateLotteryPrintByCallBack(List<LotteryPrint> list) {
		try {
			Class.forName(dbDriver);
			Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName, dbPass);
			conn.setAutoCommit(false);
			String sql = "update dl_print_lottery set status = ?,platform_id = ?,print_status = ?,"
					   + "print_sp = ?,print_no = ?,print_time = ? "
					   + "where ticket_id = ?";
			PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0, size = list.size(); i < size; i++) {
				prest.setInt(1, list.get(i).getStatus());
				prest.setString(2, list.get(i).getPlatformId());
				prest.setInt(3, list.get(i).getPrintStatus());
				prest.setString(4, list.get(i).getPrintSp());
				prest.setString(5, list.get(i).getPrintNo());
				prest.setDate(6, new java.sql.Date(list.get(i).getPrintTime().getTime()));
				prest.setString(7, list.get(i).getTicketId());
				prest.addBatch();
			}
			prest.executeBatch();
			conn.commit();
			conn.close();
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			log.error(ex.getMessage());
		}
	}
	
	/**
	 * 高速批量更新LotteryPeint
	 * @param list
	 */
	public void updateBatchErrorByTicketId(List<LotteryPrint> list) {
		try {
			Class.forName(dbDriver);
			Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName, dbPass);
			conn.setAutoCommit(false);
			String sql = "update dl_print_lottery set error_code = ?,status = ? where ticket_id = ?";
			PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0, size = list.size(); i < size; i++) {
				prest.setInt(1, list.get(i).getErrorCode());
				prest.setInt(2, list.get(i).getStatus());
				prest.setString(3, list.get(i).getTicketId());
				prest.addBatch();
			}
			prest.executeBatch();
			conn.commit();
			conn.close();
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			log.error(ex.getMessage());
		}
	}
	
	/**
	 * 高速批量更新LotteryPrint
	 * @param list
	 */
	public void updateBatchSuccessByTicketId(List<LotteryPrint> list) {
		try {
			Class.forName(dbDriver);
			Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName, dbPass);
			conn.setAutoCommit(false);
			String sql = "update dl_print_lottery set status = ? where ticket_id = ?";
			PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0, size = list.size(); i < size; i++) {
				prest.setInt(1, list.get(i).getStatus());
				prest.setString(2, list.get(i).getTicketId());
				prest.addBatch();
			}
			prest.executeBatch();
			conn.commit();
			conn.close();
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			log.error(ex.getMessage());
		}
	}
	
	/**
	 * 出票失败，更新订单状态为出票失败（2），并回滚红包状态为未使用
	 * @param list
	 */
	public void updateOrder(List<LotteryPrint> list){
		if(CollectionUtils.isNotEmpty(list)) {
			for(LotteryPrint lotteryPrint : list) {
				
			}
		}
	}
	
	/**
	 * 投注结果查询
	 * @return
	 */
	public DlQueryStakeDTO queryStake(DlQueryStakeParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/stake_query");
		JSONObject backJo = JSONObject.fromObject(backStr);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("orders", BackQueryStake.class);
		DlQueryStakeDTO dlQueryStakeDTO = (DlQueryStakeDTO) JSONObject.toBean(backJo, DlQueryStakeDTO.class, mapClass); 
		return dlQueryStakeDTO;
	}
	
	/**
	 * 期次查询（暂时不支持）
	 * @return
	 */
	public DlQueryIssueDTO queryIssue(DlQueryIssueParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/issue_query");
		JSONObject backJo = JSONObject.fromObject(backStr);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("issue", QueryIssue.class);
		DlQueryIssueDTO dlQueryIssueDTO = (DlQueryIssueDTO) JSONObject.toBean(backJo, DlQueryIssueDTO.class, mapClass); 
		return dlQueryIssueDTO;
	}
	
	/**
	 * 账户余额查询
	 * @return
	 */
	public DlQueryAccountDTO queryAccount(DlQueryAccountParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/account");
		JSONObject backJo = JSONObject.fromObject(backStr);
		DlQueryAccountDTO dlQueryAccountDTO = (DlQueryAccountDTO) JSONObject.toBean(backJo, DlQueryAccountDTO.class); 
		return dlQueryAccountDTO;
	}
	
	/**
	 * 期次投注对账文件查询
	 * @return
	 */
	public DlQueryStakeFileDTO queryStakeFile(DlQueryStakeFileParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/ticket_file");
		JSONObject backJo = JSONObject.fromObject(backStr);
		DlQueryStakeFileDTO dlQueryStakeFileDTO = (DlQueryStakeFileDTO) JSONObject.toBean(backJo, DlQueryStakeFileDTO.class); 
		return dlQueryStakeFileDTO;
	}
	
	/**
	 * 期次中奖文件查询
	 * @return
	 */
	public DlQueryPrizeFileDTO queryPrizeFile(DlQueryPrizeFileParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/prize_file");
		JSONObject backJo = JSONObject.fromObject(backStr);
		DlQueryPrizeFileDTO dlQueryPrizeFileDTO = (DlQueryPrizeFileDTO) JSONObject.toBean(backJo, DlQueryPrizeFileDTO.class); 
		return dlQueryPrizeFileDTO;
	}
	
	/**
	 * 获取返回信息
	 * @param jo
	 * @return
	 */
	private String getBackDateByJsonData(JSONObject jo, String inter) {
		String authStr = merchant + merchantPassword + jo.toString();
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		String authorization = MD5Utils.MD5(authStr);
		headers.add("Authorization", authorization);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(jo, headers);
        return rest.postForObject(printTicketUrl + inter, requestEntity, String.class);
	}

	/**
	 * 保存预出票信息
	 * @param list
	 * @return
	 */
	@Transactional
	public BaseResult<String> saveLotteryPrintInfo(List<LotteryPrintDTO> list, String orderSn) {
		List<LotteryPrint> models = list.stream().map(dto->{
			LotteryPrint lotteryPrint = new LotteryPrint();
			lotteryPrint.setGame("T51");
			lotteryPrint.setMerchant(merchant);
			lotteryPrint.setTicketId(dto.getTicketId());
			lotteryPrint.setAcceptTime(DateUtil.getCurrentTimeLong());
			lotteryPrint.setBettype(dto.getBetType());
			lotteryPrint.setMoney(BigDecimal.valueOf(dto.getMoney()*100));
			lotteryPrint.setIssue(dto.getIssue());
			lotteryPrint.setPlaytype(dto.getPlayType());
			lotteryPrint.setTimes(dto.getTimes());
			lotteryPrint.setStakes(dto.getStakes());
			lotteryPrint.setOrderSn(orderSn);
			lotteryPrint.setStatus(0);
			return lotteryPrint;
		}).collect(Collectors.toList());
		super.save(models);
		return ResultGenerator.genSuccessResult();
	}
}
