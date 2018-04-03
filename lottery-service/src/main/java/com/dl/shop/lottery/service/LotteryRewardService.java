package com.dl.shop.lottery.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.dl.enums.MatchPlayTypeEnum;
import com.dl.enums.MatchResultCrsEnum;
import com.dl.enums.MatchResultHadEnum;
import com.dl.enums.MatchResultHafuEnum;
import com.dl.param.DlRewardParam;
import com.dl.param.DlToAwardingParam;
import com.dl.shop.lottery.core.LocalWeekDate;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.LotteryMatchMapper;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.dao.LotteryRewardMapper;
import com.dl.shop.lottery.model.LotteryMatch;
import com.dl.shop.lottery.model.LotteryPrint;
import com.dl.shop.lottery.model.LotteryReward;

import tk.mybatis.mapper.entity.Condition;

@Service
public class LotteryRewardService extends AbstractService<LotteryReward> {
	
	@Resource
	private LotteryMatchMapper lotteryMatchMapper;
	
	@Resource
	private LotteryRewardMapper lotteryRewardMapper;
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Value("${reward.url}")
	private String rewardUrl;
	
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
}
