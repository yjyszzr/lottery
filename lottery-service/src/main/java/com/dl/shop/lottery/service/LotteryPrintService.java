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
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.MD5Utils;
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
import com.dl.shop.lottery.dao.PeriodRewardDetailMapper;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.PeriodRewardDetail;
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
	
	/**
	 * 定时任务：更新出票信息
	 */
	public void updatePrintLotteryCompareStatus() {
		List<LotteryPrint> lotteryPrints = lotteryPrintMapper.lotteryPrintsByUnCompare();
		if(lotteryPrints == null) {
			log.info("updatePrintLotteryCompareStatus 没有获取到需要更新的出票数据");
			return;
		}
		//获取没有赛事结果比较的playcodes
		Set<String> unPlayCodes = new HashSet<String>();
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
			unPlayCodes.addAll(playCodes);
		}
		//获取赛事结果
		List<String> playCodes = new ArrayList<String>(unPlayCodes.size());
    	playCodes.addAll(unPlayCodes);
		List<DlLeagueMatchResult> matchResults = matchResultService.queryMatchResultsByPlayCodes(playCodes);
		if(CollectionUtils.isEmpty(matchResults)) {
			log.info("updatePrintLotteryCompareStatus 准备获取赛事结果的场次数："+playCodes.size() +" 没有获取到相应的赛事结果信息");
			return;
		}
		log.info("updatePrintLotteryCompareStatus 准备获取赛事结果的场次数："+playCodes.size() +" 获取到相应的赛事结果信息数："+matchResults.size());
		Map<String, List<DlLeagueMatchResult>> resultMap = new HashMap<String, List<DlLeagueMatchResult>>();
		for(DlLeagueMatchResult dto: matchResults) {
			String playCode = dto.getPlayCode();
			List<DlLeagueMatchResult> list = resultMap.get(playCode);
			if(list == null) {
				list = new ArrayList<DlLeagueMatchResult>(5);
				resultMap.put(playCode, list);
			}
			list.add(dto);
		}
		//
		List<LotteryPrint> updates = new ArrayList<LotteryPrint>(lotteryPrints.size());
		for(String playCode: resultMap.keySet()) {
			List<DlLeagueMatchResult> matchResultList = resultMap.get(playCode);
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
							//比赛结果获取中奖信息
							for(DlLeagueMatchResult rst : matchResultList) {
								if(rst.getPlayType().equals(Integer.valueOf(playTypeStr))) {
									String cellCode = rst.getCellCode();
									if(cellCodes.contains(cellCode)) {
										sbuf.append(";").append("0").append(rst.getPlayType()).append("|")
										.append(rst.getPlayCode()).append("|").append(rst.getCellCode())
										.append("@").append(rst.getOdds());
									}
									break;
								}
							}
						}
					}
					//中奖记录
					String reward = print.getRewardStakes();
					if(sbuf.length() > 0) {
						reward = reward==null?sbuf.substring(1, sbuf.length()):(reward+sbuf.toString());
						updatePrint.setRewardStakes(reward);
					}
					
					//彩票对票结束 
					if(stakePlayCodes.size() == comparedStakes.split(",").length) {
						updatePrint.setCompareStatus(ProjectConstant.FINISH_COMPARE);
						if(StringUtils.isNotBlank(reward)) {
							//彩票中奖金额
							log.info(reward);
							List<String> spList = Arrays.asList(reward.split(";"));
							List<Double> winSPList = spList.stream().map(s -> Double.valueOf(s.substring(s.indexOf("@") + 1))).collect(Collectors.toList());
							List<Double> rewardList = new ArrayList<Double>();
							this.groupByRewardList(Double.valueOf(2 * print.getTimes()), Integer.valueOf(print.getBetType()) / 10,winSPList, rewardList);
							double rewardSum = rewardList.stream().reduce(0.00, Double::sum);
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
	}

	/**
	 * 高速批量更新LotteryPrint 10万条数据 18s
	 * @param list
	 */
	public void updateBatchLotteryPrint(List<LotteryPrint> list) {
		log.info("updateBatchLotteryPrint 准备更新彩票信息到数据库：size" + list.size());
		try {
			Class.forName(dbDriver);
			Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName, dbPass);
			conn.setAutoCommit(false);
			String sql = "UPDATE dl_print_lottery  SET reward_stakes = ?,real_reward_money = ?,compare_status = ?, compared_stakes = ? where print_lottery_id = ?";
			PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int x = 0, size = list.size(); x < size; x++) {
				prest.setString(1, list.get(x).getRewardStakes());
				prest.setBigDecimal(2, list.get(x).getRealRewardMoney() == null?BigDecimal.ZERO:list.get(x).getRealRewardMoney());
				prest.setString(3, list.get(x).getCompareStatus());
				prest.setString(4,list.get(x).getComparedStakes());
				prest.setInt(5, list.get(x).getPrintLotteryId());
				prest.addBatch();
			}
			int[] rst = prest.executeBatch();
			conn.commit();
			conn.close();
			log.info("updateBatchLotteryPrint 更新彩票信息到数据库：size" + list.size() + "  入库返回：size=" + rst.length);
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			log.error(ex.getMessage());
		}
	}
	/**
	 * 组合中奖集合
	 * @param amount:初始值2*times
	 * @param num:几串几
	 * @param list:赔率
	 * @param rewardList:组合后的中奖金额list
	 */
	private void groupByRewardList(Double amount, int num, List<Double> list, List<Double> rewardList) {
		LinkedList<Double> link = new LinkedList<Double>(list);
		while(link.size() > 0) {
			Double remove = link.remove(0);
			Double item = amount*remove;
			if(num == 1) {
				rewardList.add(item);
			} else {
				groupByRewardList(item,num-1,link, rewardList);
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
}
