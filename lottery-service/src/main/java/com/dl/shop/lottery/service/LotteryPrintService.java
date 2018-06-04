package com.dl.shop.lottery.service;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.dl.base.enums.ThirdApiEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.JSONHelper;
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
import com.dl.lottery.dto.MatchResultDTO;
import com.dl.lottery.param.DlCallbackStakeParam;
import com.dl.lottery.param.DlCallbackStakeParam.CallbackStake;
import com.dl.lottery.param.DlToStakeParam.PrintTicketOrderParam;
import com.dl.lottery.param.DlQueryAccountParam;
import com.dl.lottery.param.DlQueryIssueParam;
import com.dl.lottery.param.DlQueryPrizeFileParam;
import com.dl.lottery.param.DlQueryStakeFileParam;
import com.dl.lottery.param.DlQueryStakeParam;
import com.dl.lottery.param.DlToStakeParam;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderDetailDataDTO;
import com.dl.order.dto.OrderInfoAndDetailDTO;
import com.dl.order.dto.OrderInfoDTO;
import com.dl.order.param.LotteryPrintParam;
import com.dl.order.param.OrderSnListGoPrintLotteryParam;
import com.dl.order.param.UpdateOrderInfoParam;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.dao.PeriodRewardDetailMapper;
import com.dl.shop.lottery.dao2.LotteryMatchMapper;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.LotteryThirdApiLog;
import com.dl.shop.lottery.model.PeriodRewardDetail;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class LotteryPrintService extends AbstractService<LotteryPrint> {
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private RestTemplate restTemplate; 
	
	@Resource
	private DlLeagueMatchResultService matchResultService;
	
	@Resource
	private PeriodRewardDetailMapper periodRewardDetailMapper;

	@Value("${print.ticket.url}")
	private String printTicketUrl;
	
	@Value("${print.ticket.merchant}")
	private String merchant;
	
	@Value("${print.ticket.merchantPassword}")
	private String merchantPassword;
	
	/*@Value("${spring.datasource.druid.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.druid.username}")
	private String dbUserName;
	
	@Value("${spring.datasource.druid.password}")
	private String dbPass;
	
	@Value("${spring.datasource.druid.driver-class-name}")
	private String dbDriver;*/
	
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
			List<LotteryPrint> lotteryPrints = new ArrayList<>(callbackStakes.size());
			for(CallbackStake callbackStake : callbackStakes) {
				if(ProjectConstant.CALLBACK_STAKE_SUCCESS.equals(callbackStake.getPrintStatus())) {
					LotteryPrint lotteryPrint = new LotteryPrint();
					lotteryPrint.setTicketId(callbackStake.getTicketId());
					lotteryPrint = lotteryPrintMapper.selectOne(lotteryPrint);
					if(null != lotteryPrint) {
						Integer printStatus = callbackStake.getPrintStatus();
						if(printStatus.equals(ProjectConstant.PRINT_STATUS_FAIL)) {
							lotteryPrint.setStatus(2);
						}else if(printStatus.equals(ProjectConstant.PRINT_STATUS_SUCCESS)) {
							lotteryPrint.setStatus(1);
						}else if(printStatus.equals(ProjectConstant.PRINT_STATUS_PRINT)) {
							lotteryPrint.setStatus(3);
						} else {
							continue;
						}
						String stakes = lotteryPrint.getStakes();
						String sp = callbackStake.getSp();
						String comparePrintSp = getComparePrintSp(sp, callbackStake.getTicketId());
						comparePrintSp = StringUtils.isBlank(comparePrintSp)?sp:comparePrintSp;
						
						String game = lotteryPrint.getGame();
						String printSp = null;
						if("T51".equals(game)  && StringUtils.isNotBlank(comparePrintSp)) {
							printSp = this.getPrintSp(stakes, comparePrintSp);
						} else if("T56".equals(game)) {
							printSp = comparePrintSp;
						}
						
						lotteryPrint.setPlatformId(callbackStake.getPlatformId());
						lotteryPrint.setPrintNo(callbackStake.getPrintNo());
						lotteryPrint.setPrintSp(sp);
						lotteryPrint.setPrintStatus(printStatus);
						Date printTime = null;
						String printTimeStr = callbackStake.getPrintTime();
						if(StringUtils.isNotBlank(printTimeStr)) {
							try {
								printTimeStr = printTimeStr.replaceAll("/", "-");
								printTime = sdf.parse(printTimeStr);
								lotteryPrint.setPrintTime(printTime);
							} catch (ParseException e) {
								e.printStackTrace();
								log.error("订单编号：" + callbackStake.getTicketId() + "，出票回调，时间转换异常");
								continue;
							}
						}
						lotteryPrints.add(lotteryPrint);
						LotteryPrintParam lotteryPrintParam = new LotteryPrintParam();
						lotteryPrintParam.setOrderSn(lotteryPrint.getOrderSn());
						lotteryPrintParam.setAcceptTime(lotteryPrint.getAcceptTime());
						if(printTime != null) {
							lotteryPrintParam.setTicketTime(DateUtil.getCurrentTimeLong(printTime.getTime()/1000));
						}
						lotteryPrintParam.setPrintSp(printSp);
						lotteryPrintParams.add(lotteryPrintParam);
					}
				}
			}
			log.info("updateLotteryPrintByCallBack size:"+lotteryPrints.size());
			if(CollectionUtils.isNotEmpty(lotteryPrints)) {
				updateLotteryPrintByCallBack(lotteryPrints);
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintParams)) {
				orderService.updateOrderInfoByPrint(lotteryPrintParams);
			}
		}
	}
	/**
	 * 获取我们需要的带玩法的赔率,供订单修改详情赔率计算奖金使用
	 */
	private String getPrintSp(String stakes, String spStr) {
		String[] stakesList = stakes.split(";");
		Map<String, String> codeTypeMap = new HashMap<String, String>();
		for(int i=0; i<stakesList.length; i++) {
			String stake = stakesList[i];
			String playType = stake.substring(0, stake.indexOf("|"));
			String playCode = stake.substring(stake.indexOf("|") + 1, stake.lastIndexOf("|"));
			codeTypeMap.put(playCode, playType);
		}
		String[] spArr = spStr.split(";");
		StringBuffer sbuf = new StringBuffer();
		for(String sp: spArr) {
			String[] split = sp.split("\\|");
			String playCode = split[0];
			String playType = codeTypeMap.get(playCode);
			String nsp = playType+"|"+sp;
			sbuf.append(nsp).append(";");
		}
		String printSp = sbuf.substring(0, sbuf.length()-1);
		return printSp;
		
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
		for(LotteryPrint print: list) {
			lotteryPrintMapper.updateLotteryPrintByCallBack(print);
		}
	}
	/**
	 * 定时任务去主动查询发票状态
	 */
	public void goQueryStake() {
		List<LotteryPrint> prints = lotteryPrintMapper.getPrintIngLotterys();
		log.info("彩票出票状态查询数据："+prints.size());
		while(prints.size() > 0) {
			log.info("彩票出票状态查询数据还有："+prints.size());
			int endIndex = prints.size()>20?20:prints.size();
			List<LotteryPrint> subList = prints.subList(0, endIndex);
			List<String> collect = subList.stream().map(print-> print.getTicketId()).collect(Collectors.toList());
			String[] orders = collect.toArray(new String[collect.size()]);
			this.goQueryStake(orders);
			prints.removeAll(subList);
		}
	}
	private void goQueryStake(String[] orders) {
		DlQueryStakeParam queryStakeParam = new DlQueryStakeParam();
		queryStakeParam.setMerchant(merchant);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		queryStakeParam.setTimestamp(sdf.format(new Date()));
		queryStakeParam.setVersion("1.0");
		queryStakeParam.setOrders(orders);
		DlQueryStakeDTO dlQueryStakeDTO = this.queryStake(queryStakeParam);
		String retCode = dlQueryStakeDTO.getRetCode();
		if("0".equals(retCode)) {
			List<BackQueryStake> queryStakes = dlQueryStakeDTO.getOrders();
			log.info("查询返回结果数据：size="+queryStakes.size());
			List<LotteryPrint> lotteryPrints = new ArrayList<>(queryStakes.size());
			List<LotteryPrintParam> lotteryPrintParams = new ArrayList<LotteryPrintParam>(queryStakes.size());
			for(BackQueryStake stake: queryStakes) {
				String ticketId = stake.getTicketId();
				LotteryPrint lotteryPrint = new LotteryPrint();
				lotteryPrint.setTicketId(ticketId);
				lotteryPrint = lotteryPrintMapper.selectOne(lotteryPrint);
				if(null != lotteryPrint) {
					Integer printStatus = stake.getPrintStatus();
					if(printStatus.equals(ProjectConstant.PRINT_STATUS_FAIL)) {
						lotteryPrint.setStatus(2);
					}else if(printStatus.equals(ProjectConstant.PRINT_STATUS_SUCCESS)) {
						lotteryPrint.setStatus(1);
					}else if(printStatus.equals(ProjectConstant.PRINT_STATUS_PRINT)) {
						lotteryPrint.setStatus(3);
					} else {
						continue;
					}
					String stakes = lotteryPrint.getStakes();
					String sp = stake.getSp();
					String comparePrintSp = getComparePrintSp(sp, stake.getTicketId());
					comparePrintSp = StringUtils.isBlank(comparePrintSp)?sp:comparePrintSp;

					String game = lotteryPrint.getGame();
					String printSp = null;
					if("T51".equals(game) && StringUtils.isNotBlank(comparePrintSp)) {
						printSp = this.getPrintSp(stakes, comparePrintSp);
					} else if("T56".equals(game)) {
						printSp = comparePrintSp;
					}
					
					lotteryPrint.setPlatformId(stake.getPlatformId());
					lotteryPrint.setPrintNo(stake.getPrintNo());
					lotteryPrint.setPrintSp(sp);
					lotteryPrint.setPrintStatus(printStatus);
					Date printTime = null;
					String printTimeStr = stake.getPrintTime();
					if(StringUtils.isNotBlank(printTimeStr)) {
						try {
							printTimeStr = printTimeStr.replaceAll("/", "-");
							printTime = sdf.parse(printTimeStr);
							lotteryPrint.setPrintTime(printTime);
						} catch (ParseException e) {
							e.printStackTrace();
							log.error("订单编号：" + stake.getTicketId() + "，出票回调，时间转换异常");
							continue;
						}
					}
					lotteryPrints.add(lotteryPrint);
					if(printSp != null) {
						LotteryPrintParam lotteryPrintParam = new LotteryPrintParam();
						lotteryPrintParam.setOrderSn(lotteryPrint.getOrderSn());
						lotteryPrintParam.setAcceptTime(lotteryPrint.getAcceptTime());
						if(printTime != null) {
							lotteryPrintParam.setTicketTime(DateUtil.getCurrentTimeLong(printTime.getTime()/1000));
						}
						lotteryPrintParam.setPrintSp(printSp);
						lotteryPrintParams.add(lotteryPrintParam);
					}
				}
			}
			log.info("goQueryStake orders size=" + orders.length +" -> updateLotteryPrintByCallBack size:"+lotteryPrints.size());
			if(CollectionUtils.isNotEmpty(lotteryPrints)) {
				updateLotteryPrintByCallBack(lotteryPrints);
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintParams)) {
				orderService.updateOrderInfoByPrint(lotteryPrintParams);
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
		String requestUrl = printTicketUrl + inter;
		String response = rest.postForObject(requestUrl, requestEntity, String.class);
		String requestParam = JSONHelper.bean2json(requestEntity);
		LotteryThirdApiLog thirdApiLog = new LotteryThirdApiLog(requestUrl, ThirdApiEnum.HE_NAN_LOTTERY.getCode(), requestParam, response);
        lotteryPrintMapper.saveLotteryThirdApiLog(thirdApiLog);
		return response;
	}

	/**
	 * 保存预出票信息
	 * @param list
	 * @return
	 */
	@Transactional(value="transactionManager1")
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
			lotteryPrint.setRealRewardMoney(BigDecimal.valueOf(0.00));
			lotteryPrint.setThirdPartRewardMoney(BigDecimal.valueOf(0.00));
			lotteryPrint.setCompareStatus("0");
			lotteryPrint.setComparedStakes("");
			lotteryPrint.setRewardStakes("");
			lotteryPrint.setStatus(0);
			return lotteryPrint;
		}).collect(Collectors.toList());
		super.save(models);
		return ResultGenerator.genSuccessResult();
	}
	@Transactional(value="transactionManager1")
	public BaseResult<String> saveLotteryPrintInfo(OrderInfoAndDetailDTO data, String orderSn) {
		List<OrderDetailDataDTO> orderDetailDataDTOs = data.getOrderDetailDataDTOs();
		OrderInfoDTO orderInfoDTO = data.getOrderInfoDTO();
		String betType = orderInfoDTO.getPassType();
		String playType = orderInfoDTO.getPlayType();
		Integer times = orderInfoDTO.getCathectic();
		OrderDetailDataDTO orderDetailDataDTO = orderDetailDataDTOs.get(0);
		Double money = orderDetailDataDTOs.size()*2.0*times;
		String stakes = orderDetailDataDTOs.stream().map(item->item.getTicketData().split("@")[0]).collect(Collectors.joining(","));
		String game = orderDetailDataDTO.getChangci();
		String issue = orderDetailDataDTO.getIssue();
		//orderDetailDataDTO.get
		String ticketId = SNGenerator.nextSN(SNBusinessCodeEnum.TICKET_SN.getCode());
		LotteryPrint lotteryPrint = new LotteryPrint();
		lotteryPrint.setGame(game);
		lotteryPrint.setMerchant(merchant);
		lotteryPrint.setTicketId(ticketId);
		lotteryPrint.setAcceptTime(DateUtil.getCurrentTimeLong());
		lotteryPrint.setBettype(betType);
		lotteryPrint.setMoney(BigDecimal.valueOf(money*100));
		lotteryPrint.setIssue(issue);
		lotteryPrint.setPlaytype(playType);
		lotteryPrint.setTimes(times);
		lotteryPrint.setStakes(stakes);
		lotteryPrint.setOrderSn(orderSn);
		lotteryPrint.setRealRewardMoney(BigDecimal.valueOf(0.00));
		lotteryPrint.setThirdPartRewardMoney(BigDecimal.valueOf(0.00));
		lotteryPrint.setCompareStatus("0");
		lotteryPrint.setComparedStakes("");
		lotteryPrint.setRewardStakes("");
		lotteryPrint.setStatus(0);
		super.save(lotteryPrint);
		return ResultGenerator.genSuccessResult();
	}
	
	/**
	 * 定时任务：更新彩票信息
	 */
	public void updatePrintLotteryCompareStatus() {
		List<LotteryPrint> lotteryPrints = lotteryPrintMapper.lotteryPrintsByUnCompare();
		if(lotteryPrints == null) {
			log.info("updatePrintLotteryCompareStatus 没有获取到需要更新状态的彩票数据");
			return;
		}
		log.info("updatePrintLotteryCompareStatus 获取到需要更新状态的彩票数据，size="+lotteryPrints.size());
		//获取没有赛事结果比较的playcodes
		Set<String> unPlayCodes = new HashSet<String>();
		List<LotteryPrint> endPrints = new ArrayList<LotteryPrint>(lotteryPrints.size());
		for(LotteryPrint print: lotteryPrints) {
			List<String> playCodes = this.printStakePlayCodes(print);
			String comparedStakes = print.getComparedStakes();
			List<String> comparedPlayCodes = null;
			if(StringUtils.isNotEmpty(comparedStakes)) {
				comparedPlayCodes = Arrays.asList(comparedStakes.split(","));
			}
			if(comparedPlayCodes != null) {
				playCodes.removeAll(comparedPlayCodes);
			}
			if(playCodes.size() == 0) {
				print.setCompareStatus(ProjectConstant.FINISH_COMPARE);
				endPrints.add(print);
			}else {
				unPlayCodes.addAll(playCodes);
			}
		}
		log.info("updatePrintLotteryCompareStatus 未更新状态彩票对应其次数，size="+unPlayCodes.size());
		if(CollectionUtils.isEmpty(unPlayCodes)) {
			return;
		}
		//获取赛事结果
		List<String> playCodes = new ArrayList<String>(unPlayCodes.size());
		playCodes.addAll(unPlayCodes);
		List<String> canCelPlayCodes = lotteryMatchMapper.getCancelMatches(playCodes);
		List<DlLeagueMatchResult> matchResults = matchResultService.queryMatchResultsByPlayCodes(playCodes);
		if(CollectionUtils.isEmpty(matchResults) && Collections.isEmpty(canCelPlayCodes)) {
			log.info("updatePrintLotteryCompareStatus 准备获取赛事结果的场次数："+playCodes.size() +" 没有获取到相应的赛事结果信息也没有取消的赛事");
			return;
		}
		log.info("updatePrintLotteryCompareStatus 准备获取赛事结果的场次数："+playCodes.size() +" 获取到相应的赛事结果信息数："+matchResults.size() + "  已取消赛事"+canCelPlayCodes.size());
		
		Map<String, List<DlLeagueMatchResult>> resultMap = new HashMap<String, List<DlLeagueMatchResult>>();
		if(!CollectionUtils.isEmpty(matchResults)) {
			for(DlLeagueMatchResult dto: matchResults) {
				String playCode = dto.getPlayCode();
				List<DlLeagueMatchResult> list = resultMap.get(playCode);
				if(list == null) {
					list = new ArrayList<DlLeagueMatchResult>(5);
					resultMap.put(playCode, list);
				}
				list.add(dto);
			}
		}
		//
		List<LotteryPrint> updates = new ArrayList<LotteryPrint>(lotteryPrints.size());
		for(String playCode: playCodes) {
			boolean isCancel = false;
			if(canCelPlayCodes.contains(playCode)) {
				isCancel = true;
			}
			List<DlLeagueMatchResult> matchResultList = resultMap.get(playCode);
			if(!isCancel && CollectionUtils.isEmpty(matchResultList)) {
				continue;
			}
			for(LotteryPrint print: lotteryPrints) {
				String stakes = print.getStakes();
				String comparedStakes = print.getComparedStakes()==null?"":print.getComparedStakes();
				//判断是否对比过
				if(stakes.contains(playCode) && !comparedStakes.contains(playCode)) {
					if(comparedStakes.length() > 0) {
						comparedStakes +=",";
					}
					comparedStakes += playCode;
					LotteryPrint updatePrint = new LotteryPrint();
					updatePrint.setPrintLotteryId(print.getPrintLotteryId());
					updatePrint.setComparedStakes(comparedStakes);
					String[] stakesarr = stakes.split(";");
					StringBuffer sbuf = new StringBuffer();
					Set<String> stakePlayCodes = new HashSet<String>(stakesarr.length);
					//彩票的每一场次分析
					for(String stake: stakesarr) {
						String[] split = stake.split("\\|");
						stakePlayCodes.add(split[1]);
						if(stake.contains(playCode)) {
							String playTypeStr = split[0];
							List<String> cellCodes = Arrays.asList(split[2].split(","));
							if(isCancel) {
								sbuf.append(";").append(playTypeStr).append("|")
								.append(playCode).append("|");
								for(int i=0; i< cellCodes.size(); i++) {
									if(i > 0) {
										sbuf.append(",");
									}
									String cellCode = cellCodes.get(i);
									sbuf.append(cellCode).append("@").append("1.00");
								}
							}else {
								//比赛结果获取中奖信息
								for(DlLeagueMatchResult rst : matchResultList) {
									if(rst.getPlayType().equals(Integer.valueOf(playTypeStr))) {
										String cellCode = rst.getCellCode();
										if(cellCodes.contains(cellCode)) {
											Map<String, String> aa = this.aa(print.getPrintSp());
											String key = rst.getPlayCode() + "|" + rst.getCellCode();
											String odds = aa.get(key);
											if(StringUtils.isNotBlank(odds)) {
												sbuf.append(";").append("0").append(rst.getPlayType()).append("|")
												.append(key)
												.append("@").append(odds);
												break;
											}
										}
									}
								}
							}
						}
					}
					//中奖记录
					String reward = print.getRewardStakes();
					if(sbuf.length() > 0) {
						reward = StringUtils.isBlank(reward)?sbuf.substring(1, sbuf.length()):(reward+sbuf.toString());
						updatePrint.setRewardStakes(reward);
					}
					
					//彩票对票结束 
					if(stakePlayCodes.size() == comparedStakes.split(",").length) {
						updatePrint.setCompareStatus(ProjectConstant.FINISH_COMPARE);
						if(StringUtils.isNotBlank(reward)) {
							//彩票中奖金额
							//log.info(reward);
							List<String> spList = Arrays.asList(reward.split(";"));
							List<List<Double>> winSPList = spList.stream().map(s -> {
								String cells = s.split("\\|")[2];
								String[] split = cells.split(",");
								List<Double> list = new ArrayList<Double>(split.length);
								for(String str: split) {
									list.add(Double.valueOf(str.substring(str.indexOf("@")+1)));
								}
								return list;
							}).collect(Collectors.toList());
							List<Double> rewardList = new ArrayList<Double>();
							/*this.groupByRewardList(Double.valueOf(2 * print.getTimes()), Integer.valueOf(print.getBetType()) / 10,winSPList, rewardList);
							double rewardSum = rewardList.stream().reduce(0.00, Double::sum);*/
							//2018-06-04计算税
							this.groupByRewardList(2.0, Integer.valueOf(print.getBetType()) / 10,winSPList, rewardList);
							double rewardSum = rewardList.stream().reduce(0.00, Double::sum)*print.getTimes();
							updatePrint.setRealRewardMoney(BigDecimal.valueOf(rewardSum));
							// 保存第三方给计算的单张彩票的价格
							PeriodRewardDetail periodRewardDetail = new PeriodRewardDetail();
							periodRewardDetail.setTicketId(print.getTicketId());
							List<PeriodRewardDetail> tickets = periodRewardDetailMapper.queryPeriodRewardDetailBySelective(periodRewardDetail);
							if (!CollectionUtils.isEmpty(tickets)) {
								BigDecimal thirdPartRewardMoney = BigDecimal.valueOf(tickets.get(0).getReward());
								updatePrint.setThirdPartRewardMoney(thirdPartRewardMoney);
							}
						}
					}
					//添加
					updates.add(updatePrint);
				}//判断是否对比过over
			}//over prints for
		}//over playcode for
		this.updateBatchLotteryPrint(updates);
		this.updateBatchLotteryPrint(endPrints);
	}
	
	private Map<String,String> aa(String printSp) {
		List<String> spList = Arrays.asList(printSp.split(";"));
		Map<String,String> spMap = new HashMap<String,String>();
		for(String temp:spList) {
			if(temp.contains(",")) {
				String playCode = temp.substring(0, temp.lastIndexOf("|"));
				String temp2 =  temp.substring(temp.lastIndexOf("|")+1);
				String[] tempArr = temp2.split(",");
				for(int j = 0;j < tempArr.length;j++) {
					String temp3 = playCode + "|" + tempArr[j];
					spMap.put(temp3.substring(0,temp3.indexOf("@")), temp3.substring(temp3.indexOf("@")+1));
				}
			}else {
				spMap.put(temp.substring(0,temp.indexOf("@")), temp.substring(temp.indexOf("@")+1));
			}
		}
		return spMap;
	}

	/**
	 * 高速批量更新LotteryPrint 10万条数据 18s
	 * @param list
	 */
	public void updateBatchLotteryPrint(List<LotteryPrint> list) {
		log.info("updateBatchLotteryPrint 准备更新彩票信息到数据库：size" + list.size());
		int num = 0;
		for(LotteryPrint print: list) {
			if(null == print.getRealRewardMoney()) {
				print.setRealRewardMoney(BigDecimal.ZERO);
			}
			int n = lotteryPrintMapper.updateBatchLotteryPrint(print);
			if(n > 0) {
				num += n;
			}
		}
		log.info("updateBatchLotteryPrint 更新彩票信息到数据库：size" + list.size() + "  入库返回：size=" + num);
	}
	/**
	 * 组合中奖集合
	 * @param amount:初始值2*times
	 * @param num:几串几
	 * @param list:赔率
	 * @param rewardList:组合后的中奖金额list
	 */
	private void groupByRewardList(Double amount, int num, List<List<Double>> list, List<Double> rewardList) {
		LinkedList<List<Double>> link = new LinkedList<List<Double>>(list);
		while(link.size() > 0) {
			List<Double> removes = link.remove(0);
			for(Double remove: removes) {
				Double item = amount*remove;
				if(num == 1) {
					//start对大于等于10000的单注奖金进行20%税收，：单注彩票奖金大于或者等于1万元时，扣除20%的偶然所得税后再派奖
					if(item.doubleValue() >= 10000) {
						item = item*0.8;
					}
					//end
					rewardList.add(item);
				} else {
					groupByRewardList(item,num-1,link, rewardList);
				}
			}
		}		
	}
	/**
	 * 获取playcode
	 * @param print
	 * @return
	 */
	private List<String> printStakePlayCodes(LotteryPrint print) {
		String stakes = print.getStakes();
		String[] split = stakes.split(";");
		List<String> playCodes = new ArrayList<String>(split.length);
		for(String str: split) {
			String[] split2 = str.split("\\|");
			String playCode = split2[1];
			playCodes.add(playCode);
		}
		return playCodes;
	}

	public int updatePrintStatusByTicketId(LotteryPrint lotteryPrint) {
		return lotteryPrintMapper.updatePrintStatusByTicketId(lotteryPrint);
	}
	
	/**
	 * 出票定时任务
	 */
	public void goPrintLottery() {
        List<LotteryPrint> lotteryPrintList = lotteryPrintMapper.lotteryPrintsByUnPrint();
        log.info("goPrintLottery 未出票数："+lotteryPrintList.size());
        if(CollectionUtils.isNotEmpty(lotteryPrintList)) {
        	log.info("lotteryPrintList size="+lotteryPrintList.size());
        	while(lotteryPrintList.size() > 0) {
        		int toIndex = lotteryPrintList.size() > 50?50:lotteryPrintList.size();
        		List<LotteryPrint> lotteryPrints = lotteryPrintList.subList(0, toIndex);
        		log.info(" go tostake size="+lotteryPrints.size());
        		Set<String> errOrderSns = this.gotoStak(lotteryPrints);
        		log.info("出票失败订单数："+errOrderSns.size());
        		lotteryPrintList.removeAll(lotteryPrints);
        	}
        }
	}
	
	/**
	 * 调用第三方出票
	 * @param successOrderSn
	 * @param lotteryPrintList
	 * @return 返回
	 */
	private Set<String> gotoStak(List<LotteryPrint> lotteryPrints) {
		DlToStakeParam dlToStakeParam = new DlToStakeParam();
		dlToStakeParam.setMerchant(lotteryPrints.get(0).getMerchant());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dlToStakeParam.setTimestamp(sdf.format(new Date()));
		dlToStakeParam.setVersion("1.0");
		List<PrintTicketOrderParam> printTicketOrderParams = new LinkedList<PrintTicketOrderParam>();
		Map<String, String> ticketIdOrderSnMap = new HashMap<String, String>();
		Set<String> allOrderSns = new HashSet<String>(lotteryPrints.size());
		Set<String> successOrderSn = new HashSet<String>(lotteryPrints.size());
		lotteryPrints.forEach(lp->{
			PrintTicketOrderParam printTicketOrderParam = new PrintTicketOrderParam();
			printTicketOrderParam.setTicketId(lp.getTicketId());
			printTicketOrderParam.setGame(lp.getGame());
			printTicketOrderParam.setIssue(lp.getIssue());
			printTicketOrderParam.setPlayType(lp.getPlayType());
			printTicketOrderParam.setBetType(lp.getBetType());
			printTicketOrderParam.setTimes(lp.getTimes());
			printTicketOrderParam.setMoney(lp.getMoney().intValue());
			printTicketOrderParam.setStakes(lp.getStakes());
			printTicketOrderParams.add(printTicketOrderParam);
			ticketIdOrderSnMap.put(lp.getTicketId(), lp.getOrderSn());
			allOrderSns.add(lp.getOrderSn());
		});
		dlToStakeParam.setOrders(printTicketOrderParams);
		DlToStakeDTO dlToStakeDTO = this.toStake(dlToStakeParam);
		if(null != dlToStakeDTO && CollectionUtils.isNotEmpty(dlToStakeDTO.getOrders())) {
			log.info("inf tostake orders");
			List<LotteryPrint> lotteryPrintErrors = new LinkedList<LotteryPrint>();
			List<LotteryPrint> lotteryPrintSuccess = new LinkedList<LotteryPrint>();
			for(BackOrderDetail backOrderDetail : dlToStakeDTO.getOrders()) {
				LotteryPrint lotteryPrint = new LotteryPrint();
				lotteryPrint.setTicketId(backOrderDetail.getTicketId());
				Integer errorCode = backOrderDetail.getErrorCode();
				if(errorCode != 0) {
					if(3002 == errorCode) {
						successOrderSn.add(ticketIdOrderSnMap.get(backOrderDetail.getTicketId()));
					}else {
						lotteryPrint.setErrorCode(errorCode);
						//出票失败
						lotteryPrint.setStatus(2);
						lotteryPrint.setPrintTime(new Date());
						lotteryPrintErrors.add(lotteryPrint);
					}
				} else {
					//出票中
					successOrderSn.add(ticketIdOrderSnMap.get(backOrderDetail.getTicketId()));
					lotteryPrint.setStatus(3);
					lotteryPrintSuccess.add(lotteryPrint);
				}
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintErrors)) {
				log.info("lotteryPrintErrors size = "+lotteryPrintErrors.size());
				long start = System.currentTimeMillis();
				int num = 0;
				for(LotteryPrint lotteryPrint:lotteryPrintErrors) {
					int rst = this.updatePrintStatusByTicketId(lotteryPrint);
					num+=rst<0?0:rst;
				}
				long end = System.currentTimeMillis();
				log.info("lotteryPrintErrors size = "+lotteryPrintErrors.size() +" rst size="+ num+ "  times=" + (end-start));
			}
			if(CollectionUtils.isNotEmpty(lotteryPrintSuccess)) {
				log.info("lotteryPrintSuccess size="+lotteryPrintSuccess.size());
				long start = System.currentTimeMillis();
				int num = 0;
				for(LotteryPrint lotteryPrint:lotteryPrintSuccess) {
					int rst = this.updatePrintStatusByTicketId(lotteryPrint);
					num+=rst<0?0:rst;
				}
				long end = System.currentTimeMillis();
				log.info("lotteryPrintSuccess size="+lotteryPrintSuccess.size()+" rst size="+ num + "  times=" + (end-start));
			}
		}
		allOrderSns.removeAll(successOrderSn);
		return allOrderSns;
	}
	/**
	 * 查询订单对应的出票状态
	 * @param orderSn
	 * @return 1：待出票，2出票失败，3待开奖
	 */
	public int printLotteryStatusByOrderSn(String orderSn) {
		List<LotteryPrint> byOrderSn = lotteryPrintMapper.getByOrderSn(orderSn);
		if(CollectionUtils.isNotEmpty(byOrderSn)) {
			Set<Integer> status = byOrderSn.stream().map(obj->obj.getStatus()).collect(Collectors.toSet());
			if(status.contains(0)) {//未完全出票
				return 1;
			}else if(status.contains(3)) {//出票中
				return 1;
			}else if(status.contains(1)) {//有成功出票
				return 3;
			}else {
				return 2;
			}
		}
		return -1;
	}
}
