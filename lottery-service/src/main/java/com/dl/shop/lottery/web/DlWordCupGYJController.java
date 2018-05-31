package com.dl.shop.lottery.web;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.BetPayInfoDTO;
import com.dl.lottery.dto.DIZQUserBetCellInfoDTO;
import com.dl.lottery.dto.DIZQUserBetInfoDTO;
import com.dl.lottery.dto.DlWordCupGJDTO;
import com.dl.lottery.dto.DlWordCupGYJDTO;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.ListWCgjParam;
import com.dl.lottery.param.ListWCgyjParam;
import com.dl.lottery.param.SaveWcBetInfoParam;
import com.dl.member.api.IUserBonusService;
import com.dl.member.api.IUserService;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.StrParam;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.model.DlWordCupGJ;
import com.dl.shop.lottery.model.DlWordCupGYJ;
import com.dl.shop.lottery.service.DlWordCupGJService;
import com.dl.shop.lottery.service.DlWordCupGYJService;
import com.dl.shop.lottery.utils.MD5;

import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/05/30.
*/
@RestController
@RequestMapping("/dl/wc")
public class DlWordCupGYJController {
    @Resource
    private DlWordCupGYJService dlWordCupGYJService;
    @Resource
    private DlWordCupGJService dlWordCupGJService;
    @Resource
    private IUserService userService;
    @Resource
    private IUserBonusService userBonusService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @ApiOperation(value = "获取冠亚军列表", notes = "获取冠亚军列表")
    @PostMapping("/gyjs")
    public BaseResult<List<DlWordCupGYJDTO>> list(@RequestBody ListWCgyjParam param) {
        List<DlWordCupGYJDTO> list = dlWordCupGYJService.getMatchList("1801", param.getCountryIds());
        return ResultGenerator.genSuccessResult("success",list);
    }
    
    @ApiOperation(value = "获取冠军列表", notes = "获取冠军列表")
    @PostMapping("/gjs")
    public BaseResult<List<DlWordCupGJDTO>> list(@RequestBody ListWCgjParam param) {
        List<DlWordCupGJDTO> list = dlWordCupGJService.getMatchList("1801");
        return ResultGenerator.genSuccessResult("success",list);
    }
    
    @ApiOperation(value = "保存投注信息", notes = "保存投注信息")
	@PostMapping("/saveBetInfo")
	public BaseResult<BetPayInfoDTO> saveWcBetInfo(@Valid @RequestBody SaveWcBetInfoParam param) {
    	String betIds = param.getBetIds();
    	if(StringUtils.isBlank(betIds)) {
    		return ResultGenerator.genResult(LotteryResultEnum.BET_CELL_EMPTY.getCode(), LotteryResultEnum.BET_CELL_EMPTY.getMsg());
    	}
    	Integer isGj = param.getIsGj();
    	if(isGj == null) {
    		return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
    	}
    	Integer times = param.getTimes();
    	int lotteryClassifyId = param.getLotteryClassifyId();
    	int lotteryPlayClassifyId = param.getLotteryPlayClassifyId();

    	Double betMoney = null ;
    	int bteNum = 0;
    	String issue = null;
    	String forecastMoney = null;
    	List<DIZQUserBetCellInfoDTO>  userBetCellInfos = new ArrayList<DIZQUserBetCellInfoDTO>(betIds.split(",").length);
    	if(isGj == 1) {//gyj
    		List<DlWordCupGYJ> findByIds = dlWordCupGYJService.findByIds(betIds);
    		bteNum = findByIds.size();
    		betMoney = bteNum*2.0*times;
    		issue = findByIds.get(0).getIssue();
    		String minOdds = findByIds.stream().min((item1,item2)->item1.getBetOdds().compareTo(item2.getBetOdds())).get().getBetOdds();
    		String maxOdds = findByIds.stream().max((item1,item2)->item1.getBetOdds().compareTo(item2.getBetOdds())).get().getBetOdds();
    		Double min = Double.valueOf(minOdds)*2*times;
    		Double max = Double.valueOf(maxOdds)*2*times;
    		forecastMoney = String.format("%.2f", min) + "~" + String.format("%.2f", max);
    		findByIds.forEach(item->{
    			DIZQUserBetCellInfoDTO dto = new DIZQUserBetCellInfoDTO();
    			dto.setLotteryClassifyId(lotteryClassifyId);
    			dto.setLotteryPlayClassifyId(lotteryPlayClassifyId);
    			dto.setMatchTeam(item.getHomeContryName()+"VS"+item.getVisitorContryName());
    			dto.setMatchId(item.getId());
    			Integer sortId = item.getSortId();
    			String sortIdStr = sortId<10?"0"+sortId.toString():sortId.toString();
    			dto.setChangci(item.getGame());
    			dto.setPlayCode(item.getIssue());
    			dto.setTicketData(sortIdStr + "@" + item.getBetOdds());
    			dto.setFixedodds("");
    			dto.setIsDan(0);
    			dto.setMatchTime(0);
    			userBetCellInfos.add(dto);
    		});
    	} else if(isGj ==0) {
    		List<DlWordCupGJ> findByIds = dlWordCupGJService.findByIds(betIds);
    		bteNum = findByIds.size();
    		betMoney = bteNum*2.0*times;
    		issue = findByIds.get(0).getIssue();
    		String minOdds = findByIds.stream().min((item1,item2)->item1.getBetOdds().compareTo(item2.getBetOdds())).get().getBetOdds();
    		String maxOdds = findByIds.stream().max((item1,item2)->item1.getBetOdds().compareTo(item2.getBetOdds())).get().getBetOdds();
    		Double min = Double.valueOf(minOdds)*2*times;
    		Double max = Double.valueOf(maxOdds)*2*times;
    		forecastMoney = String.format("%.2f", min) + "~" + String.format("%.2f", max);
    		findByIds.forEach(item->{
    			DIZQUserBetCellInfoDTO dto = new DIZQUserBetCellInfoDTO();
    			dto.setLotteryClassifyId(lotteryClassifyId);
    			dto.setLotteryPlayClassifyId(lotteryPlayClassifyId);
    			dto.setMatchTeam(item.getContryName());
    			dto.setMatchId(item.getId());
    			Integer sortId = item.getSortId();
    			String sortIdStr = sortId<10?"0"+sortId.toString():sortId.toString();
    			dto.setChangci(item.getGame());
    			dto.setPlayCode(item.getIssue());
    			dto.setTicketData(sortIdStr + "@" + item.getBetOdds());
    			dto.setFixedodds("");
    			dto.setIsDan(0);
    			dto.setMatchTime(0);
    			userBetCellInfos.add(dto);
    		});
    	}else {
    		return ResultGenerator.genResult(LotteryResultEnum.BET_PLAY_ENABLE.getCode(), LotteryResultEnum.BET_PLAY_ENABLE.getMsg());
    	}
    	Double orderMoney = betMoney;
    	//用户信息
    	StrParam strParam = new StrParam();
    	BaseResult<UserDTO> userInfoExceptPassRst = userService.userInfoExceptPass(strParam);
    	if(userInfoExceptPassRst.getCode() != 0 || null == userInfoExceptPassRst.getData()) {
    		return ResultGenerator.genResult(LotteryResultEnum.OPTION_ERROR.getCode(), LotteryResultEnum.OPTION_ERROR.getMsg());
    	}
    	String totalMoney = userInfoExceptPassRst.getData().getTotalMoney();
    	Double userTotalMoney = Double.valueOf(totalMoney);
    	//红包包
    	BonusLimitConditionParam bonusLimitConditionParam = new BonusLimitConditionParam();
    	bonusLimitConditionParam.setOrderMoneyPaid(BigDecimal.valueOf(orderMoney));
    	BaseResult<List<UserBonusDTO>> userBonusListRst = userBonusService.queryValidBonusList(bonusLimitConditionParam);
    	if(userBonusListRst.getCode() != 0) {
    		return ResultGenerator.genResult(LotteryResultEnum.OPTION_ERROR.getCode(), LotteryResultEnum.OPTION_ERROR.getMsg());
    	}

    	List<UserBonusDTO> userBonusList = userBonusListRst.getData();
    	UserBonusDTO userBonusDto = null;
    	if(!CollectionUtils.isEmpty(userBonusList)) {
    		if(param.getBonusId() != null && param.getBonusId().intValue() != 0) {//有红包id
    			if(param.getBonusId().intValue() != -1) {
    				Optional<UserBonusDTO> findFirst = userBonusList.stream().filter(dto->dto.getUserBonusId().equals(param.getBonusId())).findFirst();
    				userBonusDto = findFirst.isPresent()?findFirst.get():null;
    			}
    		}else {//没有传红包id
    			List<UserBonusDTO> userBonuses = userBonusList.stream().filter(dto->{
    				double minGoodsAmount = dto.getBonusPrice().doubleValue();
    				return orderMoney < minGoodsAmount ? false : true;
    			}).sorted((n1,n2)->n1.getBonusPrice().compareTo(n2.getBonusPrice()))
    					.collect(Collectors.toList());
    			if(userBonuses.size() > 0) {
    				userBonusDto = userBonuses.get(0);//userBonusDto == null?userBonuses.get(0):userBonusDto;
    			}
    		}
    	}
    	String bonusId = userBonusDto != null?userBonusDto.getUserBonusId().toString():null;
    	Double bonusAmount = userBonusDto!=null?userBonusDto.getBonusPrice().doubleValue():0.0;
    	Double amountTemp = orderMoney - bonusAmount;//红包扣款后的金额
    	Double surplus = 0.0;
    	Double thirdPartyPaid = 0.0;
    	if(amountTemp < 0) {//红包大于订单金额
    		bonusAmount = orderMoney;
    	}else {
    		surplus = userTotalMoney>amountTemp?amountTemp:userTotalMoney;
    		thirdPartyPaid = amountTemp - surplus;
    	}
    	//缓存订单支付信息
    	DIZQUserBetInfoDTO dto = new DIZQUserBetInfoDTO();
    	dto.setTimes(times);
    	dto.setBetType("01");
    	dto.setPlayType("0");
    	dto.setLotteryClassifyId(lotteryClassifyId);
    	dto.setLotteryPlayClassifyId(lotteryPlayClassifyId);
    	dto.setUserBetCellInfos(userBetCellInfos);
		dto.setBetNum(bteNum);
		dto.setTicketNum(1);
		dto.setMoney(orderMoney);
		dto.setBonusAmount(bonusAmount);
		dto.setBonusId(bonusId);
		dto.setSurplus(surplus);
		dto.setForecastMoney(forecastMoney);
		dto.setThirdPartyPaid(thirdPartyPaid);
		String requestFrom = "0";
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		if(userDevice != null) {
			requestFrom = userDevice.getPlat();
		}
		dto.setRequestFrom(requestFrom);
		dto.setUserId(SessionUtil.getUserId());
		dto.setIssue(issue);
		String dtoJson = JSONHelper.bean2json(dto);
		String keyStr = "bet_info_" + SessionUtil.getUserId() +"_"+ System.currentTimeMillis();
		String key = MD5.crypt(keyStr);
		stringRedisTemplate.opsForValue().set(key, dtoJson, ProjectConstant.BET_INFO_EXPIRE_TIME, TimeUnit.MINUTES);
		//返回页面信息
		BetPayInfoDTO betPlayInfoDTO = new BetPayInfoDTO();
		betPlayInfoDTO.setPayToken(key);
		betPlayInfoDTO.setBonusAmount(String.format("%.2f", bonusAmount));
		betPlayInfoDTO.setBonusId(bonusId);
		betPlayInfoDTO.setBonusList(userBonusList);
		betPlayInfoDTO.setOrderMoney(String.format("%.2f", orderMoney));
		betPlayInfoDTO.setSurplus(String.format("%.2f", surplus));
		betPlayInfoDTO.setThirdPartyPaid(String.format("%.2f", thirdPartyPaid));
		return ResultGenerator.genSuccessResult("success", betPlayInfoDTO);
    }
    
}
