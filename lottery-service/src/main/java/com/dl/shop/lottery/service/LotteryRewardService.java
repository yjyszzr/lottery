package com.dl.shop.lottery.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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
import com.dl.lottery.dto.DlQueryPrizeFileDTO;
import com.dl.lottery.enums.MatchPlayTypeEnum;
import com.dl.lottery.enums.MatchResultCrsEnum;
import com.dl.lottery.enums.MatchResultHadEnum;
import com.dl.lottery.enums.MatchResultHafuEnum;
import com.dl.lottery.param.DlQueryPrizeFileParam;
import com.dl.lottery.param.DlRewardParam;
import com.dl.lottery.param.DlToAwardingParam;
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
		JSONObject jo = getRewardData(param.getChangCiId());
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
		//根据兑奖期次，查询符合条件的出票订单
		//① 查询期次相等的出票订单，组装中奖数据，并可以进行派奖
		LotteryPrint lotteryPrintEqual = new LotteryPrint();
		lotteryPrintEqual.setIssue(param.getIssue());
		List<LotteryPrint> lotteryPrintEquals = lotteryPrintMapper.selectEqualsIssuePrint(lotteryPrintEqual);
		if(CollectionUtils.isNotEmpty(lotteryPrintEquals)) {
			
		}
		//② 查询当前期次小于数据库期次的出票订单，只组装中奖数据
		LotteryPrint lotteryPrintLessThan = new LotteryPrint();
		lotteryPrintLessThan.setIssue(param.getIssue());
		List<LotteryPrint> lotteryPrintLessThans = lotteryPrintMapper.selectLessThanIssuePrint(lotteryPrintLessThan);
		if(CollectionUtils.isNotEmpty(lotteryPrintLessThans)) {
			
		}
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
	private JSONObject getRewardData(String changCiId) {
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
		        
		        this.insertBatch(detailList, String.valueOf(periodId));
		        
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
	public void insertBatch(List<PeriodRewardDetail> list, String peroidId) {
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
}
