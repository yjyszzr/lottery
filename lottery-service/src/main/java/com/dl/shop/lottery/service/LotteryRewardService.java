package com.dl.shop.lottery.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.NetWorkUtil;
import com.dl.base.util.NuclearUtil;
import com.dl.lottery.dto.DlOrderDataDTO;
import com.dl.lottery.dto.DlQueryPrizeFileDTO;
import com.dl.lottery.dto.LotteryRewardByIssueDTO;
import com.dl.lottery.dto.RewardStakesWithSpDTO;
import com.dl.lottery.enums.MatchPlayTypeEnum;
import com.dl.lottery.enums.MatchResultCrsEnum;
import com.dl.lottery.enums.MatchResultHadEnum;
import com.dl.lottery.enums.MatchResultHafuEnum;
import com.dl.lottery.param.DlLotteryRewardByIssueParam;
import com.dl.lottery.param.DlQueryPrizeFileParam;
import com.dl.lottery.param.DlRewardParam;
import com.dl.lottery.param.DlToAwardingParam;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.DlLotteryPrintMoneyDTO;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryMatchMapper;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.dao.LotteryRewardMapper;
import com.dl.shop.lottery.dao.PeriodRewardDetailMapper;
import com.dl.shop.lottery.dao.PeriodRewardMapper;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.LotteryReward;
import com.dl.shop.lottery.model.PeriodReward;
import com.dl.shop.lottery.model.PeriodRewardDetail;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;

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
	
	@Value("${reward.url}")
	private String rewardUrl;
	
	@Value("${spring.datasource.druid.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.druid.username}")
	private String dbUserName;
	
	@Value("${spring.datasource.druid.password}")
	private String dbPass;
	
	@Value("${spring.datasource.druid.driver-class-name}")
	private String dbDriver;
	
	/**
	 * 根据场次id拉取中奖数据
	 * @param param
	 */
	public void saveRewardData(DlRewardParam param){
		JSONObject jo = queryRewardData(param.getChangCiId());
		if(null != jo) {
			List<JSONObject> jos = getRewardListData(jo);
			Condition condition = new Condition(LotteryMatch.class);
	        condition.createCriteria().andCondition("changci_id=", param.getChangCiId());
			List<LotteryMatch> lotteryMatchs = lotteryMatchMapper.selectByCondition(condition);
			if(CollectionUtils.isNotEmpty(lotteryMatchs)) {
				LotteryMatch lotteryMatch = lotteryMatchs.get(0);
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
		LotteryReward lr = new LotteryReward();
		lr.setIssue(param.getIssue());
		List<LotteryReward> rewards = lotteryRewardMapper.queryRewardByIssueBySelective(lr);
		if(CollectionUtils.isEmpty(rewards)) {
			log.info(new Date()+"没有开奖信息");
		}
		LotteryReward lotteryReward = rewards.get(0);
		//匹配中奖信息
		this.compareReward(lotteryReward);
		//更新订单及订单详情
		List<DlOrderDataDTO> dlOrderDataDTOs = lotteryPrintMapper.getRealRewardMoney(param.getIssue());
		if(CollectionUtils.isNotEmpty(dlOrderDataDTOs)) {
			DlLotteryPrintMoneyDTO dlLotteryPrintMoneyDTO = new DlLotteryPrintMoneyDTO();
			dlLotteryPrintMoneyDTO.setRewardLimit(lotteryReward.getRewardLimit());
			List<com.dl.order.dto.DlOrderDataDTO> dtos = new LinkedList<com.dl.order.dto.DlOrderDataDTO>();
			for(DlOrderDataDTO dto : dlOrderDataDTOs) {
				com.dl.order.dto.DlOrderDataDTO dlOrderDataDTO = new com.dl.order.dto.DlOrderDataDTO();
				try {
					BeanUtils.copyProperties(dlOrderDataDTO, dto);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				dtos.add(dlOrderDataDTO);
			}
			dlLotteryPrintMoneyDTO.setOrderDataDTOs(dtos);
			orderService.updateOrderInfoByExchangeReward(dlLotteryPrintMoneyDTO);
		}
		//更新用户账户，大于5000元的需要派奖
		
		
	}
	
	/**
	 * 根据期次，查询审核通过的开奖数据
	 * @param param
	 * @return
	 */
	public LotteryRewardByIssueDTO queryRewardByIssue(DlLotteryRewardByIssueParam param) {
		LotteryRewardByIssueDTO lotteryRewardByIssueDTO = new LotteryRewardByIssueDTO();
		LotteryReward lr = new LotteryReward();
		lr.setIssue(param.getIssue());
		List<LotteryReward> rewards = lotteryRewardMapper.queryRewardByIssueBySelective(lr);
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
		LotteryReward lotteryReward = new LotteryReward();
		lotteryReward.setMatchId(lotteryMatch.getMatchId());
		lotteryReward.setChangciId(lotteryMatch.getChangciId());
		lotteryReward.setChangci(lotteryMatch.getChangci());
		lotteryReward.setMatchTime(lotteryMatch.getMatchTime());
		lotteryReward.setRewardData(rewardData);
		lotteryReward.setStatus(ProjectConstant.AUDIT_STAY);
		lotteryReward.setCreateTime(DateUtil.getCurrentTimeLong());
		lotteryRewardMapper.insert(lotteryReward);
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
			String pre = sdf.format(lotteryMatch.getMatchTime());
			int weekDay = LocalWeekDate.getCode(lotteryMatch.getChangci().substring(0, 2));
			sb.append(pre + weekDay + lotteryMatch.getChangci().substring(2) + "|");
			if(MatchPlayTypeEnum.PLAY_TYPE_HHAD.getcode() == i || MatchPlayTypeEnum.PLAY_TYPE_HAD.getcode() == i) {
				sb.append(MatchResultHadEnum.getCode(jos.get(i-1).getString("prs_name")));
			} else if(MatchPlayTypeEnum.PLAY_TYPE_CRS.getcode() == i) {
				sb.append(MatchResultCrsEnum.getCode(jos.get(i-1).getString("prs_name")));
			} else if(MatchPlayTypeEnum.PLAY_TYPE_TTG.getcode() == i) {
				sb.append(jos.get(i-1).getString("prs_name"));
			} else if(MatchPlayTypeEnum.PLAY_TYPE_HAFU.getcode() == i) {
				sb.append(MatchResultHafuEnum.getCode(jos.get(i-1).getString("prs_name")));
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
	private JSONObject queryRewardData(String changCiId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mid", changCiId);
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
	public void compareReward(LotteryReward rewards) {
//		//获取某个期次的获奖信息
//		LotteryReward lr = new LotteryReward();
//		lr.setIssue(param.getIssue());
//		List<LotteryReward> rewards = lotteryRewardMapper.queryRewardByIssueBySelective(lr);
//		if(CollectionUtils.isEmpty(rewards)) {
//			log.info(new Date()+"没有开奖信息");
//		}
		//该期次的中奖号码
		String rewardStakes = rewards.getRewardData();
		
		//获取该期次的出票信息
		LotteryPrint lotteryPrintLessEqual = new LotteryPrint();
		lotteryPrintLessEqual.setIssue(rewards.getIssue());
		List<LotteryPrint> lotteryPrintList = lotteryPrintMapper.selectLessThanIssuePrint(lotteryPrintLessEqual);
		if(CollectionUtils.isEmpty(lotteryPrintList)) {
			log.info("没有期次为"+rewards.getIssue()+"的出票信息");
		}
		
		List<LotteryPrint> updatelotteryPrintList = new ArrayList<>();
		for(LotteryPrint lp:lotteryPrintList) {
			List<String> list1 = this.createStakesList1(lp.getStakes());
			List<String> list2 = this.createRewardStakesList2(rewardStakes);
			//匹配获奖信息:出票号码和开奖号码相同的 就是中奖号码
		    List<String> same = NuclearUtil.getSame(list1, list2);
		    if(CollectionUtils.isEmpty(same)) {
		    	continue;
		    }
		    
		    List<String> spList = Arrays.asList(lp.getRewardStakes().split(","));
		    List<String> alReadySp = spList.stream().map(s->s.substring(s.indexOf("@"))).collect(Collectors.toList());
		    same.addAll(alReadySp);
		    //构造获胜期次的赔率集合和带有期次@赔率的字符串
		    RewardStakesWithSpDTO rewardStakesWithSpDTO = this.createRewardStakesWithSp(same,lp.getPrintSp());
		    
		    //最后一期已经开奖,并且中奖 才计算这张彩票的中奖金额
		    LotteryPrint updatePrint = new LotteryPrint();
		    Double rewardSum = 0.00;//该订单的中奖金额
		    if(rewardStakes.contains(lp.getIssue())) {
		    	Integer rewardSize = same.size();//中奖号码有几个
		    	if(rewardSize >= 2) {//比对后的中奖号码 大于等于2 就说明一定中奖，然后计算该张彩票的中奖金额
		    		List<Double> rewardList = new ArrayList<Double>();
		    		this.groupByRewardList(Double.valueOf(2*lp.getTimes()), Integer.valueOf(lp.getBetType()), rewardStakesWithSpDTO.getWinSpList(), rewardList);
		    		rewardSum = rewardList.stream().reduce(0.0, Double::sum);
		    	}
		    	
		    	//保存第三方给计算的单张彩票的价格，对账用
		    	PeriodRewardDetail periodRewardDetail =  new PeriodRewardDetail();
		    	periodRewardDetail.setTicketId(lp.getTicketId());
		    	List<PeriodRewardDetail> tickets = periodRewardDetailMapper.queryPeriodRewardDetailBySelective(periodRewardDetail);
		    	if(!CollectionUtils.isEmpty(tickets)) {
		    		updatePrint.setThirdPartRewardMoney(new BigDecimal(tickets.get(0).getReward()));
		    		if(rewardSum.equals(Double.valueOf(tickets.get(0).getReward()))) {
		    			updatePrint.setCompareStatus(ProjectConstant.SAME_MONEY);
		    		}else {
		    			updatePrint.setCompareStatus(ProjectConstant.NOT_SAME_MONEY);
		    		}
		    	}
		    }
		    
		    updatePrint.setRealRewardMoney(BigDecimal.valueOf(rewardSum));
		    updatePrint.setRewardStakes(lp.getRewardStakes()+rewardStakesWithSpDTO.getWinIssueSpStr());
		    updatePrint.setPrintLotteryId(lp.getPrintLotteryId());
		    updatelotteryPrintList.add(updatePrint);
		}
		
		//更新结果：中奖号和中奖金额
		this.updateBatchLotteryPrint(updatelotteryPrintList);
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
	    List<String> spList = Arrays.asList(printSp.split(";"));
	    Map<String,String> spMap = new HashMap<String,String>();
	    for(String temp:spList) {
	    	if(temp.contains(",")) {
				String temp1 = temp.substring(0, temp.lastIndexOf("|"));
				String temp2 =  temp.substring(temp.lastIndexOf("|"));
				String[] tempArr = temp2.split(",");
				for(int j = 0;j < tempArr.length;j++) {
					String temp3 = temp1 + tempArr[j];
					spMap.put(temp3.substring(0,temp3.indexOf("@")), temp3.substring(temp3.indexOf("@")));
				}
	    	}
	    	spMap.put(temp.substring(0,temp.indexOf("@")), temp.substring(temp.indexOf("@")));
	    }
	    
	    RewardStakesWithSpDTO rewardStakesWithSpDTO = new RewardStakesWithSpDTO();
        for (String string : same) {
            String cc = spMap.get(string.substring(string.indexOf("|"),string.lastIndexOf("|")));
            if(cc!=null)
            {   
            	rewardSpList.add(string+spMap.get(cc));
            	winSpList.add(Double.valueOf(spMap.get(cc)));
                continue;
            }
        }
        
        rewardStakesWithSpDTO.setWinSpList(winSpList);
        rewardStakesWithSpDTO.setWinIssueSpStr(String.join(";", (String [])rewardSpList.toArray()));
        return rewardStakesWithSpDTO;
	}
	
	/**
	 * 把出票信息解析成List<String>
	 * @param stakes
	 * @return
	 */
	public List<String> createStakesList1(String stakes){
		List<String> list1 = new ArrayList<>();
		String[] stakesArr = stakes.split(";");
		for(int i= 0 ;i < stakesArr.length;i++) {
			if(stakesArr[i].contains(",")) {
				String temp1 = stakesArr[i].substring(0, stakesArr[i].lastIndexOf("|"));
				String temp2 =  stakesArr[i].substring(stakesArr[i].lastIndexOf("|"));
				String[] tempArr = temp2.split(",");
				for(int j = 0;j < tempArr.length;j++) {
					tempArr[j] = temp1 + tempArr[j];
				}
				List<String> tempList = Arrays.asList(tempArr);
				list1.addAll(tempList);
			}
			list1.add(stakesArr[i]);
		} 
		return list1;
	}
	
	/**
	 * 把开奖信息解析成List<String>
	 * @param rewardsTakes
	 * @return
	 */
	public List<String> createRewardStakesList2(String rewardsTakes) {
		String[] arr = rewardsTakes.split(";");
	    List<String> list2 = java.util.Arrays.asList(arr);
	    return list2;
	}
	
	/**
	 * 解析期次中奖文件:要采取定时任务
	 * @throws MalformedURLException 
	 */
	public void resovleRewardTxt()  {
		//判断当天的比赛是否都获取过中奖文件
		List<LotteryMatch> lotteryMatchList = lotteryMatchMapper.getMatchListToday();
		if(CollectionUtils.isEmpty(lotteryMatchList)) {
			log.info("未获取到今天的比赛");
		}
		
		List<String> matchSnList = lotteryMatchList.stream().map(s->s.getMatchSn()).collect(Collectors.toList());
		List<String> periodRewardIssueList = periodRewardMapper.queryPeriodRewardByIssues(matchSnList);
		if(matchSnList.containsAll(periodRewardIssueList)) {
			log.info(new Date()+"没有要更新的期次中奖文件");
		}
		
		List<DlQueryPrizeFileParam> paramList = new ArrayList<>();
		lotteryMatchList.forEach(s->{
			DlQueryPrizeFileParam param = new DlQueryPrizeFileParam();
			param.setGame("T51");
			param.setIssue(s.getMatchSn());
			paramList.add(param);
		});
		
		log.info(new Date()+"开始更新期次中奖文件");
		
		for(DlQueryPrizeFileParam param:paramList) {
			String prizeUrl = "";
			DlQueryPrizeFileDTO dlQueryPrizeFileDTO = lotteryPrintService.queryPrizeFile(param);
			if(null != dlQueryPrizeFileDTO && "0".equals(dlQueryPrizeFileDTO.getRetCode())) {
				prizeUrl = dlQueryPrizeFileDTO.getUrl();
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
		try {
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
		}
	}
	
	/**
	 * 高速批量更新LotteryPrint 10万条数据 18s
	 * @param list
	 */
	public void updateBatchLotteryPrint(List<LotteryPrint> list) {
		try {
			Class.forName(dbDriver);
			Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUserName, dbPass);
			conn.setAutoCommit(false);
			String sql = "UPDATE dl_print_lottery  SET reward_stakes = ?,real_reward_money = ? where print_lottery_id = ?";
			PreparedStatement prest = (PreparedStatement) conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int x = 0, size = list.size(); x < size; x++) {
				prest.setString(1, list.get(x).getRewardStakes());
				prest.setBigDecimal(2, list.get(x).getRealRewardMoney() == null?BigDecimal.ZERO:list.get(x).getRealRewardMoney());
				prest.setInt(3, list.get(x).getPrintLotteryId());
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
}
