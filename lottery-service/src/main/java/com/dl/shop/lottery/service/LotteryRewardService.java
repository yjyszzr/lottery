package com.dl.shop.lottery.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dl.base.constant.CommonConstants;
import com.dl.base.enums.MatchPlayTypeEnum;
import com.dl.base.enums.MatchResultCrsEnum;
import com.dl.base.enums.MatchResultHadEnum;
import com.dl.base.enums.MatchResultHafuEnum;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.base.util.NuclearUtil;
import com.dl.lottery.dto.DlOrderDataDTO;
import com.dl.lottery.dto.DlQueryPrizeFileDTO;
import com.dl.lottery.dto.LotteryRewardByIssueDTO;
import com.dl.lottery.dto.RewardStakesWithSpDTO;
import com.dl.lottery.dto.TicketRewardDTO;
import com.dl.lottery.param.DlLotteryRewardByIssueParam;
import com.dl.lottery.param.DlQueryPrizeFileParam;
import com.dl.lottery.param.DlRewardParam;
import com.dl.lottery.param.DlToAwardingParam;
import com.dl.member.api.ISysConfigService;
import com.dl.member.api.IUserAccountService;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserIdAndRewardDTO;
import com.dl.member.param.SysConfigParam;
import com.dl.member.param.UserIdAndRewardListParam;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderWithUserDTO;
import com.dl.order.param.LotteryPrintMoneyParam;
import com.dl.order.param.OrderDataParam;
import com.dl.order.param.OrderQueryParam;
import com.dl.order.param.OrderWithUserParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.dao.LotteryRewardMapper;
import com.dl.shop.lottery.dao.PeriodRewardDetailMapper;
import com.dl.shop.lottery.dao.PeriodRewardMapper;
import com.dl.shop.lottery.dao2.LotteryMatchMapper;
import com.dl.shop.lottery.model.DlLeagueMatchResult;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.LotteryReward;
import com.dl.shop.lottery.model.PeriodReward;
import com.dl.shop.lottery.model.PeriodRewardDetail;
import com.google.common.base.Joiner;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.util.StringUtil;

@Service
@Slf4j
public class LotteryRewardService extends AbstractService<LotteryReward> {
	
	@Resource
	private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private LotteryRewardMapper lotteryRewardMapper;
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private PeriodRewardService periodRewardService;
	
	@Resource
	private LotteryPrintService lotteryPrintService;
	
	@Resource
	private PeriodRewardDetailService periodRewardDetailService;
	
	@Resource
	private PeriodRewardDetailMapper periodRewardDetailMapper;
	
	@Resource
	private PeriodRewardMapper periodRewardMapper;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Resource
	private DlLeagueMatchResultService leagueMatchResultService;
	
	
	@Value("${reward.url}")
	private String rewardUrl;
	
	/*@Value("${spring.datasource.druid.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.druid.username}")
	private String dbUserName;
	
	@Value("${spring.datasource.druid.password}")
	private String dbPass;
	
	@Value("${spring.datasource.druid.driver-class-name}")
	private String dbDriver;*/
	
	/**
	 * 根据场次id拉取中奖数据
	 * @param param
	 */
	public void saveRewardData(DlRewardParam param){
		Condition condition = new Condition(LotteryMatch.class);
		condition.createCriteria().andCondition("changci_id=", param.getChangCiId());
		List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.selectByCondition(condition);
		this.saveRewardInfos(lotteryMatchs);
	}

	/**
	 * 保存比赛结果信息
	 * @param lotteryMatchs
	 */
	public void saveRewardInfos(List<LotteryMatch> lotteryMatchs) {
		if(CollectionUtils.isEmpty(lotteryMatchs)) {
			return;
		}
		for(LotteryMatch lotteryMatch : lotteryMatchs) {
			JSONObject jo = queryRewardData(lotteryMatch.getChangciId());
			if(null != jo) {
				List<JSONObject> jos = getRewardListData(jo);
				String rewardData = getAssembleRewardData(lotteryMatch, jos);
				if(StringUtils.isNotEmpty(rewardData)) {
					insertRewardData(lotteryMatch, rewardData);
				}
			}
		}
	}
	
	/**
	 * 兑奖接口
	 * @param param
	 */
	public void toAwarding(DlToAwardingParam param) {
		
		//获取某个期次的获奖信息
		List<DlLeagueMatchResult> matchResultList = leagueMatchResultService.queryMatchResultByPlayCode(param.getIssue());
		
		if(CollectionUtils.isEmpty(matchResultList)) {
			log.info(new Date()+"没有开奖信息");
			return;
		}
		
		//匹配中奖信息
		this.compareReward(matchResultList);
		
	}
	
	/**
	 * 根据开奖期次更新订单的状态，中奖金额 等
	 * @param issue
	 */
	public void updateOrderAfterOpenReward() {
		
		//查询订单状态是待开奖的，查询是否每笔订单锁包含的彩票都已经比对完成
		OrderQueryParam orderQueryParam = new OrderQueryParam();
		orderQueryParam.setOrderStatus(ProjectConstant.ORDER_STATUS_STAY);
		orderQueryParam.setPayStatus(ProjectConstant.PAY_STATUS_ALREADY);
		BaseResult<List<String>> rst = orderService.queryOrderSnListByStatus(orderQueryParam);
		log.info("获取待开奖订单返回：code="+rst.getCode()+" msg="+rst.getMsg());
		if(rst.getCode() != 0) {
			log.error(rst.getMsg());
			return;
		}
		
		List<String> orderSnList = rst.getData();
		log.info("待开奖数据： size="+orderSnList.size());
		if(CollectionUtils.isEmpty(orderSnList)) {
			return;
		}		
		while(orderSnList.size() > 0) {
			int num = orderSnList.size()>20?20:orderSnList.size();
			List<String> subList = orderSnList.subList(0, num);
			List<LotteryPrint> dlOrderDataDTOs = lotteryPrintMapper.getPrintLotteryListByOrderSns(subList);
			log.info("获取可开奖彩票信息："+dlOrderDataDTOs.size());
			if(CollectionUtils.isNotEmpty(dlOrderDataDTOs)) {
				Map<String, Double> map = new HashMap<String, Double>();
				Set<String> unOrderSns = new HashSet<String>();
				List<LotteryPrint> errorPrints = new ArrayList<LotteryPrint>(0);
				for(LotteryPrint dto: dlOrderDataDTOs) {
					int printStatus = dto.getPrintStatus();
					String printSp = dto.getPrintSp();
					if(printStatus == ProjectConstant.PRINT_STATUS_SUCCESS || StringUtils.isNotBlank(printSp)) {//出票成功
						String orderSn = dto.getOrderSn();
						String compareStatus = dto.getCompareStatus();
						if(StringUtils.isBlank(compareStatus) || !"1".equals(compareStatus)) {
							unOrderSns.add(orderSn);
						}
						if(unOrderSns.contains(orderSn)) {
							map.remove(orderSn);
							continue;
						}
						Double double1 = map.get(orderSn);
						BigDecimal realRewardMoney = dto.getRealRewardMoney();
						double realReward = realRewardMoney == null?0:realRewardMoney.doubleValue();
						double1 = double1==null?realReward:(double1+realReward);
						map.put(orderSn, double1);
					}else if(printStatus == ProjectConstant.PRINT_STATUS_FAIL){
						errorPrints.add(dto);
					}
				}
				
				log.info("*********8可开奖订单及资金数："+map.size());
				LotteryPrintMoneyParam lotteryPrintMoneyDTO = new LotteryPrintMoneyParam();
				List<OrderDataParam> dtos = new ArrayList<OrderDataParam>(map.size());
				for(String orderSn: map.keySet()) {
					OrderDataParam dlOrderDataDTO = new OrderDataParam();
					dlOrderDataDTO.setOrderSn(orderSn);
					BigDecimal realReward = BigDecimal.valueOf(map.get(orderSn));
					dlOrderDataDTO.setRealRewardMoney(realReward);
					
					if(realReward.compareTo(BigDecimal.ZERO) == 0) {//未中奖
						dlOrderDataDTO.setOrderStatus(ProjectConstant.ORDER_STATUS_NOT);
					}else if(realReward.compareTo(BigDecimal.ZERO) > 0) {//已中奖
						dlOrderDataDTO.setOrderStatus(ProjectConstant.ORDER_STATUS_REWARDING);
					}
					
					if(realReward.compareTo(BigDecimal.ZERO) < 0) {//中奖金额为负数，过滤掉
						continue;
					}
					
					dtos.add(dlOrderDataDTO);
				}
				log.info("%%%%%%准备执行开奖订单数："+dtos.size());
				if(dtos.size() > 0) {
					lotteryPrintMoneyDTO.setOrderDataDTOs(dtos);
					BaseResult<Integer> result = orderService.updateOrderInfoByExchangeReward(lotteryPrintMoneyDTO);
					log.info("更新订单中奖状态和中奖金额updateOrderInfoByExchangeReward param size="+dtos.size() + "  result :"+ result.getCode());
					if(result.getCode() == 0) {
						log.info("更新订单中奖状态和中奖金额updateOrderInfoByExchangeReward param size="+dtos.size() + "  result :"+ result.getCode()+"  result size="+result.getData());
					}
				}
				if(errorPrints.size() > 0) {
					for(LotteryPrint lotteryPrint:errorPrints) {
						lotteryPrint.setStatus(2);
						lotteryPrint.setErrorCode(55555);
						lotteryPrintMapper.updatePrintStatusByTicketId(lotteryPrint);
					}
				}
			}
			orderSnList.removeAll(subList);
		}
	}
	/**
	 * 根据期次，查询审核通过的开奖数据
	 * @param param
	 * @return
	 */
	public LotteryRewardByIssueDTO queryRewardByIssue(DlLotteryRewardByIssueParam param) {
		LotteryRewardByIssueDTO lotteryRewardByIssueDTO = new LotteryRewardByIssueDTO();
		List<LotteryReward> rewards = lotteryRewardMapper.queryRewardByIssueBySelective(param.getIssue());
		if(CollectionUtils.isEmpty(rewards)) {
			log.info(new Date()+"没有开奖信息");
			return null;
		}
		LotteryReward lotteryReward = rewards.get(0);
		lotteryRewardByIssueDTO.setIssue(lotteryReward.getIssue());
		lotteryRewardByIssueDTO.setRewardData(lotteryReward.getRewardData());
		return lotteryRewardByIssueDTO;
	}
	
	/**
	 * 保存开奖信息
	 * @param lotteryMatch
	 * @param rewardData
	 */
	private void insertRewardData(LotteryMatch lotteryMatch, String rewardData) {
		LotteryReward queryRewardByChangciId = lotteryRewardMapper.queryRewardByChangciId(lotteryMatch.getChangciId());
		if(null == queryRewardByChangciId) {
			LotteryReward lotteryReward = new LotteryReward();
			lotteryReward.setMatchId(lotteryMatch.getMatchId());
			lotteryReward.setChangciId(lotteryMatch.getChangciId());
			lotteryReward.setChangci(lotteryMatch.getChangci());
			lotteryReward.setMatchTime(lotteryMatch.getMatchTime());
			lotteryReward.setRewardData(rewardData);
			lotteryReward.setIssue(lotteryMatch.getMatchSn());
			lotteryReward.setStatus(ProjectConstant.AUDIT_STAY);
			lotteryReward.setCreateTime(DateUtil.getCurrentTimeLong());
			lotteryRewardMapper.insert(lotteryReward);
		}
	}
	
	/**
	 * 组装开奖信息
	 * @param lotteryMatch
	 * @param jos
	 * @return
	 */
	private String getAssembleRewardData(LotteryMatch lotteryMatch, List<JSONObject> jos) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuilder strBuilder = new StringBuilder();
		for(int i=1;i<=jos.size();i++) {
			StringBuilder sb = new StringBuilder();
			if((i + "").length() == 1) {
				sb.append("0" + i + "|");
			}else {
				sb.append(i + "|");
			}
			String pre = sdf.format(lotteryMatch.getShowTime());
			int weekDay = LocalWeekDate.getCode(lotteryMatch.getChangci().substring(0, 2));
			sb.append(pre + weekDay + lotteryMatch.getChangci().substring(2) + "|");
			JSONObject jsonObject = jos.get(i-1);
			if(null == jsonObject) {
				continue;
			}
			if(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode() == i || MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode() == i) {
				String rsCode = jsonObject.getString("pool_rs");
				if("h".equals(rsCode)) {
					rsCode = MatchResultHadEnum.HAD_H.getCode().toString();
				}else if("a".equals(rsCode)) {
					rsCode = MatchResultHadEnum.HAD_A.getCode().toString();
				}else {
					rsCode = MatchResultHadEnum.HAD_D.getCode().toString();
				}
				sb.append(rsCode);
			} else if(MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode() == i) {
				sb.append(MatchResultCrsEnum.getCode(jsonObject.getString("prs_name")));
			} else if(MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode() == i) {
				sb.append(jsonObject.getString("prs_name"));
			} else if(MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode() == i) {
				sb.append(MatchResultHafuEnum.getCode(jsonObject.getString("prs_name")));
			}
			strBuilder.append(sb.toString() + ";");
		}
		return strBuilder.substring(0, strBuilder.length()-1);
	}
	
	/**
	 * 获取有用的开奖信息
	 * @param jo
	 * @return
	 */
	private List<JSONObject> getRewardListData(JSONObject jo){
		List<JSONObject> jos = new LinkedList<JSONObject>();
		//让球胜平负
		JSONObject hhadJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getMsg());
		jos.add(hhadJo);
		//胜平负
		JSONObject hadJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HAD.getMsg());
		jos.add(hadJo);
		//比分
		JSONObject crsJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_CRS.getMsg());
		jos.add(crsJo);
		//总进球
		JSONObject ttgJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_TTG.getMsg());
		jos.add(ttgJo);
		//半全场
		JSONObject hafuJo = jo.getJSONObject(MatchPlayTypeEnum.PLAY_TYPE_HAFU.getMsg());
		jos.add(hafuJo);
		return jos;
	}
	
	/**
	 * 拉取开奖信息
	 * @return
	 */
	private JSONObject queryRewardData(Integer changCiId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mid", changCiId.toString());
		String json = NetWorkUtil.doGet(rewardUrl, map, "UTF-8");
	    if (json.contains("error")) {
	        throw new ServiceException(RespStatusEnum.FAIL.getCode(), changCiId + "，中奖信息查询失败");
	    }
	    JSONObject jsonObject = JSONObject.parseObject(json);
	    JSONObject jo = jsonObject.getJSONObject("result");
	    jo = jo.getJSONObject("pool_rs");
	    return jo;
	}
	
	/**
	 * 单个开奖结果匹配中奖-- 可以采用定时任务 或 提供给后台管理调用
	 */
	public void compareReward(List<DlLeagueMatchResult> matchResultList) {
		// 该期次的开奖结果
		List<String> rewardStakesList = this.createRewardStakesList2(matchResultList);
		// 期次
		String issue = matchResultList.get(0).getPlayCode();
		// 获取该期次未与开奖信息比较的出票集合
		List<LotteryPrint> lotteryPrintList = lotteryPrintMapper.selectPrintsIncludeCurIssue(issue);
		if (CollectionUtils.isEmpty(lotteryPrintList)) {
			return;
		}

		List<LotteryPrint> updatelotteryPrintList = new ArrayList<>();
		for (LotteryPrint lp : lotteryPrintList) {
			//防止重复派奖
			List<String> comparedStakes = Arrays.asList((String[])lp.getComparedStakes().split(","));
			if(comparedStakes.contains(issue)) {
				continue;
			}
			
			//出票的集合
			List<String> stakesList = this.createStakesList1(lp.getStakes());
			
			//出票表中是否包含该其次:包含记录比较过，不包含不比较
			List<String> comparedStakesList = new ArrayList<String>();
			if(lp.getStakes().contains(issue)) {
				comparedStakesList = this.recordStakes(issue, lp);
			}else {				
				continue;
			}
	
			// 匹配获奖信息:出票号码和开奖号码相同的 就是中奖号码
			List<String> same = NuclearUtil.getSame(stakesList, rewardStakesList);
			if (CollectionUtils.isEmpty(same)) {
				continue;
			}
			
			// 构造获胜期次的赔率集合和带有期次@赔率的字符串
			RewardStakesWithSpDTO rewardStakesWithSpDTO = this.createRewardStakesWithSp(same, lp.getPrintSp());
			if(null == rewardStakesWithSpDTO) {
				continue;
			}
			
			// 一张彩票中投注信息中包含的期次所有都已经被派过奖,并且中奖,才计算这张彩票的中奖金额
			TicketRewardDTO ticketRewardDTO = this.createTicketRewardDTO(comparedStakesList,lp,issue,rewardStakesWithSpDTO);
			LotteryPrint updatePrint = new LotteryPrint();
			updatePrint.setRealRewardMoney(ticketRewardDTO.getRewardSum());
			updatePrint.setThirdPartRewardMoney(ticketRewardDTO.getThirdPartRewardMoney());
			updatePrint.setCompareStatus(ticketRewardDTO.getCompareStatus());
			String winIssueSp = rewardStakesWithSpDTO.getWinIssueSpStr();
			updatePrint.setRewardStakes(StringUtils.isEmpty(lp.getRewardStakes())?winIssueSp:lp.getRewardStakes() + ";" + winIssueSp);
			updatePrint.setPrintLotteryId(lp.getPrintLotteryId());
			updatePrint.setComparedStakes(StringUtils.isEmpty(lp.getComparedStakes())?issue:lp.getComparedStakes()+","+issue);
			updatelotteryPrintList.add(updatePrint);
		}

		if(CollectionUtils.isEmpty(updatelotteryPrintList)) {
			return;
		}
		// 更新结果：中奖号和中奖金额
		lotteryPrintService.updateBatchLotteryPrint(updatelotteryPrintList);
	}
	
	/**
	 * 记录比较过的期次
	 * @param issue
	 * @param lp
	 * @return
	 */
	public List<String> recordStakes(String issue,LotteryPrint lp){
		List<String> comparedStakes1 = new ArrayList<String>();
		if(!StringUtils.isEmpty(lp.getComparedStakes())) {
			comparedStakes1 = Arrays.asList((String[])lp.getComparedStakes().split(","));
		}
		
		List<String> comparedStakesList = new ArrayList<String>(comparedStakes1);
		if(!comparedStakesList.contains(issue)) {
			comparedStakesList.add(issue);
		}
		return comparedStakesList;
	}
	/**
	 *  一张彩票中投注信息中包含的期次所有都已经被派过奖，并且中奖 才计算这张彩票的中奖金额
	 * @param rewardStakes
	 * @param lp
	 * @param issue
	 * @param rewardStakesWithSpDTO
	 * @return
	 */
	public TicketRewardDTO createTicketRewardDTO(List<String> comparedStakesList,LotteryPrint lp,String issue,RewardStakesWithSpDTO rewardStakesWithSpDTO) {
		TicketRewardDTO ticketRewardDTO = new TicketRewardDTO();
		Double rewardSum = 0.00;
		String compareStatus = ProjectConstant.NOT_FINISH_COMPARE;
		BigDecimal thirdPartRewardMoney = BigDecimal.ZERO;
		
		List<String> includeIssues = this.createIssueListWithStakes(lp.getStakes());
		if(comparedStakesList.size() == includeIssues.size()) {
			Boolean sameBz = NuclearUtil.isSame(comparedStakesList, includeIssues);//一张彩票中投注信息中包含的期次所有都已经被派过奖
			if(true == sameBz) {
				compareStatus = ProjectConstant.FINISH_COMPARE;
				List<Double> winSPList = new ArrayList<>();
				if (!StringUtils.isEmpty(lp.getRewardStakes())) {// 计算价格要累计上已匹配中奖的号码
					List<String> spList = Arrays.asList(lp.getRewardStakes().split(";"));
					List<Double> alReadySp = spList.stream().map(s -> Double.valueOf(s.substring(s.indexOf("@") + 1))).collect(Collectors.toList());
					winSPList.addAll(alReadySp);
				}
				winSPList.addAll(rewardStakesWithSpDTO.getWinSpList());//加上本期中奖号码的赔率
				
				Integer rewardSize = winSPList.size();// 中奖号码有几个
				Integer betType = Integer.valueOf(lp.getBetType().substring(0, 1));
				if (rewardSize == betType) {// 比对后的中奖号码 大于等于几串几的几 就说明一定中奖，然后计算该张彩票的中奖金额
					List<Double> rewardList = new ArrayList<Double>();
					this.groupByRewardList(Double.valueOf(2 * lp.getTimes()), Integer.valueOf(lp.getBetType()) / 10,winSPList, rewardList);
					rewardSum = rewardList.stream().reduce(0.00, Double::sum);
					log.info("彩票id:"+lp.getTicketId()+"中奖，中奖金额为:"+rewardSum+"元");
				}
				
				

				// 保存第三方给计算的单张彩票的价格
				PeriodRewardDetail periodRewardDetail = new PeriodRewardDetail();
				periodRewardDetail.setTicketId(lp.getTicketId());
				List<PeriodRewardDetail> tickets = periodRewardDetailMapper.queryPeriodRewardDetailBySelective(periodRewardDetail);
				if (!CollectionUtils.isEmpty(tickets)) {
					thirdPartRewardMoney = BigDecimal.valueOf(tickets.get(0).getReward());
				}
			}else {
				log.error("出票表中的彩票id为:"+lp.getTicketId()+"投注记录包含的期次与以往派过奖的期次不相同,请检查数据");
			}
		}
		
		ticketRewardDTO.setRewardSum(BigDecimal.valueOf(rewardSum));
		ticketRewardDTO.setCompareStatus(compareStatus);
		ticketRewardDTO.setThirdPartRewardMoney(thirdPartRewardMoney);
		return ticketRewardDTO;
	}
	
	
	/**
	 * 组合中奖集合
	 * @param amount:初始值2*times
	 * @param num:几串几
	 * @param list:赔率
	 * @param rewardList:组合后的中奖金额list
	 */
	public void groupByRewardList(Double amount, int num, List<Double> list, List<Double> rewardList) {
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
	 * 匹配了真实赔率的中奖号
	 * @param printSp
	 * @return
	 */
	public RewardStakesWithSpDTO createRewardStakesWithSp(List<String> same,String printSp){
		List<String> rewardSpList = new ArrayList<String>();
		List<Double> winSpList = new ArrayList<Double>();
		if(StringUtil.isEmpty(printSp)) {
			return null;
		}
		
	    List<String> spList = Arrays.asList(printSp.split(";"));
	    Map<String,String> spMap = new HashMap<String,String>();
	    for(String temp:spList) {
	    	if(temp.contains(",")) {
				String temp1 = temp.substring(0, temp.lastIndexOf("|"));
				String temp2 =  temp.substring(temp.lastIndexOf("|")+1);
				String[] tempArr = temp2.split(",");
				for(int j = 0;j < tempArr.length;j++) {
					String temp3 = temp1 + "|" + tempArr[j];
					spMap.put(temp3.substring(0,temp3.indexOf("@")), temp3.substring(temp3.indexOf("@")+1));
				}
	    	}else {
	    		spMap.put(temp.substring(0,temp.indexOf("@")), temp.substring(temp.indexOf("@")+1));
	    	}
	    }
	    
	    RewardStakesWithSpDTO rewardStakesWithSpDTO = new RewardStakesWithSpDTO();
	    for (String string : same) {
	        String cc = spMap.get(string.substring(string.indexOf("|")+1));
	        if(cc!=null)
	        {   
	        	rewardSpList.add(string+"@"+cc);
	        	winSpList.add(Double.valueOf(cc));
	            continue;
	        }
	    }
	    
	    rewardStakesWithSpDTO.setWinSpList(winSpList);
	    rewardStakesWithSpDTO.setWinIssueSpStr(Joiner.on(",").join(rewardSpList));
	    return rewardStakesWithSpDTO;
	}
	
	/**
	 * 把出票信息解析成List<String>
	 * @param stakes
	 * @return
	 */
	public List<String> createStakesList1(String stakes){
		List<String> list1 = new ArrayList<>();
//		List<String> list2 = new ArrayList<>();
		String[] stakesArr = stakes.split(";");
		for(int i= 0 ;i < stakesArr.length;i++) {
			if(stakesArr[i].contains(",")) {
				String temp1 = stakesArr[i].substring(0, stakesArr[i].lastIndexOf("|")+1);
				String temp2 =  stakesArr[i].substring(stakesArr[i].lastIndexOf("|")+1);
				String[] tempArr = temp2.split(",");
				for(int j = 0;j < tempArr.length;j++) {
					tempArr[j] = temp1 + tempArr[j];
				}
				List<String> tempList = Arrays.asList(tempArr);
				list1.addAll(tempList);
			}else {
				list1.add(stakesArr[i]);
			}
		} 
		return list1;
	}
	
	/**
	 * 把开奖信息解析成List<String>
	 * @param rewardsTakes
	 * @return
	 */
	public List<String> createRewardStakesList2(List<DlLeagueMatchResult> matchResultList) {
		List<String> rewardStakesList = matchResultList.stream().map(s->{
			return "0"+s.getPlayType()+"|"+s.getPlayCode()+"|"+s.getCellCode();
		}).collect(Collectors.toList());
		
		return rewardStakesList;
	}
	
	
	/**
	 * 把根据投注信息解析出包含的期次
	 * @param stakes
	 * @return
	 */
	public List<String> createIssueListWithStakes(String stakes){
		String[] stakesArr = stakes.split(";");
		List<String> stakesList = Arrays.asList(stakesArr);
		List<String> issueList = stakesList.stream().map(s->s.substring(s.indexOf("|")+1, s.lastIndexOf("|"))).collect(Collectors.toList());
		return issueList;
	}
	
	/**
	 * 解析期次中奖文件:要采取定时任务
	 * @throws MalformedURLException 
	 */
	public void resovleRewardTxt()  {
		//查询当天已比完赛的赛事
		List<LotteryMatch> lotteryMatchList = lotteryMatchMapper.getMatchListToday();
		if(CollectionUtils.isEmpty(lotteryMatchList)) {
			log.info("未获取到今天比完的赛事，不需调用获取中奖文件的接口");
			return;
		}
		
		List<String> matchSnList = lotteryMatchList.stream().map(s->s.getMatchSn()).collect(Collectors.toList());
		List<String> periodRewardIssueList = periodRewardMapper.queryPeriodRewardByIssues(matchSnList);
		if(!CollectionUtils.isEmpty(periodRewardIssueList) && matchSnList.containsAll(periodRewardIssueList)) {
			log.info(new Date()+"没有要更新的期次中奖文件");
			return;
		}
		
		List<DlQueryPrizeFileParam> paramList = new ArrayList<>();
		lotteryMatchList.forEach(s->{
			DlQueryPrizeFileParam param = new DlQueryPrizeFileParam();
			param.setGame("T51");
			param.setIssue(s.getMatchSn());
			param.setMerchant("180326");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			param.setTimestamp(df.format(new Date()));
			param.setVersion("1.0");
			paramList.add(param);
		});
		
		log.info(new Date()+"开始更新期次中奖文件");
		
		for(DlQueryPrizeFileParam param:paramList) {
			String prizeUrl = "";
			DlQueryPrizeFileDTO dlQueryPrizeFileDTO = lotteryPrintService.queryPrizeFile(param);
			if(null != dlQueryPrizeFileDTO && "0".equals(dlQueryPrizeFileDTO.getRetCode())) {
				prizeUrl = dlQueryPrizeFileDTO.getUrl();
				if(StringUtils.isEmpty(prizeUrl)) {
					continue;
				}
			}
			
			String sCurrentLine = "";
			String[] segments = null;
			List<PeriodRewardDetail> detailList = new ArrayList<PeriodRewardDetail>();
			Integer periodId = null;
			try {
				//String prizeUrl = "http://1.192.90.178:9085/files/180326/prize/issue/T51/T51_201803283002_180326_prize.txt";
				//String prizeUrl = "http://1.192.90.178:9085/files/180326/prize/issue/T51/T51_201804021002_180326_prize.txt";
				URL url = new URL(prizeUrl);
		        HttpURLConnection conn;
				conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(50000);  
				conn.setReadTimeout(50000);  
		        conn.connect();
		        InputStream is = conn.getInputStream();
		        InputStreamReader isr = new InputStreamReader(is,"UTF-8");
		        BufferedReader br = new BufferedReader(isr);
		        for(int i= 0; (sCurrentLine = br.readLine()) != null;i++) {
		        	segments = sCurrentLine.split("\\s+");
		        	if(i == 0) {
		        		PeriodReward periodReward = new PeriodReward();
		        		periodReward.setMerchant(segments[0]);
		        		periodReward.setGame(segments[1]);
		        		periodReward.setIssue(segments[2]);
		        		periodReward.setSmallReward(Integer.valueOf(segments[3]));
		        		periodReward.setSmallRewardNum(Integer.valueOf(segments[4]));
		        		periodReward.setBigReward(Integer.valueOf(segments[5]));
		        		periodReward.setBigRewardNum(Integer.valueOf(segments[6]));
		        		periodReward.setUnReward(Integer.valueOf(segments[7]));
		        		periodReward.setUnRewardNum(Integer.valueOf(segments[8]));
		        		periodReward.setPrizeUrl(prizeUrl);
		        		periodId = periodRewardMapper.insertPeriodReward(periodReward);
		        		
		        	}else {
		        		PeriodRewardDetail periodRewardDetail = new PeriodRewardDetail();
		        		periodRewardDetail.setPlatformId(segments[0]);
		        		periodRewardDetail.setPeroidId(String.valueOf(periodId));
		        		periodRewardDetail.setTicketId(segments[1]);
		        		periodRewardDetail.setReward(Integer.valueOf(segments[2]));
		        		periodRewardDetail.setStatus(segments[3]);
		        		detailList.add(periodRewardDetail);
		        	}
		        }
		        
		        this.insertBatchRewardDetail(detailList, String.valueOf(periodId));
		        
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		log.info(new Date()+"更新期次中奖文件完成");
	}
	
	
	/**
	 * 高速批量插入PeriodRewardDetail 10万条数据 18s
	 * @param list
	 * @param peroidId
	 */
	public void insertBatchRewardDetail(List<PeriodRewardDetail> list, String peroidId) {
		for(PeriodRewardDetail detail: list) {
			detail.setPeroidId(peroidId);
		}
		periodRewardDetailService.save(list);
		/*try {
			Class.forName(dbDriver);
			Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName, dbPass);
			conn.setAutoCommit(false);
			String sql = "INSERT INTO dl_period_reward_detail(peroid_id,platform_id,ticket_id,reward,status) VALUES(?,?,?,?,?)";
			PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int x = 0, size = list.size(); x < size; x++) {
				prest.setString(1, peroidId);
				prest.setString(2, list.get(x).getPlatformId());
				prest.setString(3, list.get(x).getTicketId());
				prest.setString(4, list.get(x).getStatus());
				prest.setInt(5, list.get(x).getReward());
				prest.addBatch();
			}
			prest.executeBatch();
			conn.commit();
			conn.close();
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			log.error(ex.getMessage());
		}*/
	}
	
}
