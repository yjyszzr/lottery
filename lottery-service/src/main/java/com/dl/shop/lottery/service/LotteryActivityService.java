package com.dl.shop.lottery.service;
import com.dl.shop.lottery.model.LotteryActivity;

import tk.mybatis.mapper.entity.Condition;

import com.dl.shop.lottery.dao.LotteryActivityMapper;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.ActivityDTO;
import com.dl.lottery.dto.DlHallDTO.DlActivityDTO;
import com.dl.lottery.param.ActTypeParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional(value="transactionManager1")
public class LotteryActivityService extends AbstractService<LotteryActivity> {
    @Resource
    private LotteryActivityMapper lotteryActivityMapper;
    
    /**
     * 根据活动类型来查询活动集合
     * @param actTypeParam
     * @return
     */
    public BaseResult<List<ActivityDTO>> queryActivityByActType(@RequestBody ActTypeParam actTypeParam){
		DlActivityDTO dlActivityDTO = new DlActivityDTO();
		Condition condition = new Condition(LotteryActivity.class);
		condition.createCriteria().andCondition("act_type =",actTypeParam.getActType()).andCondition("is_finish=", 0).andCondition("status=", 1).andGreaterThan("endTime", DateUtil.getCurrentTimeLong()).andLessThanOrEqualTo("startTime", DateUtil.getCurrentTimeLong());
		List<LotteryActivity> lotteryActivitys = lotteryActivityMapper.selectByCondition(condition);
		
		List<ActivityDTO> lotteryActivityDTOs = new ArrayList<>();
		if(lotteryActivitys.size() >= 0) {
			lotteryActivitys.forEach(s->{
				ActivityDTO activityDTO = new ActivityDTO();
				activityDTO.setTitle(s.getActTitle());
				activityDTO.setDetail("");
				activityDTO.setIcon(s.getActImg());
				activityDTO.setActUrl(s.getActUrl());
				lotteryActivityDTOs.add(activityDTO);
			});
		}
		
		return ResultGenerator.genSuccessResult("success", lotteryActivityDTOs);
	}

}
