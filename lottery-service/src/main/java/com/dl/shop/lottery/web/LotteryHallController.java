package com.dl.shop.lottery.web;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DlDiscoveryHallClassifyDTO;
import com.dl.lottery.dto.DlHallDTO;
import com.dl.lottery.dto.DlHallMixDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO;
import com.dl.lottery.param.DlPlayClassifyParam;
import com.dl.lottery.param.HallParam;
import com.dl.lottery.param.PageParam;
import com.dl.member.api.ISysConfigService;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.param.SysConfigParam;
import com.dl.shop.lottery.service.DlArticleService;
import com.dl.shop.lottery.service.LotteryHallService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lottery/hall")
public class LotteryHallController {

	@Resource
	private LotteryHallService lotteryHallService;
	@Resource
	private ISysConfigService sysConfigService;
	@Resource
	private DlArticleService articleService;

	@ApiOperation(value = "获取彩票大厅数据和咨询列表数据", notes = "获取彩票大厅数据和咨询列表数据")
	@PostMapping("/getHallMixData")
	public BaseResult<DlHallMixDTO> getHallDataMix(
			@Valid @RequestBody HallParam hallParam) {
		DlHallMixDTO dlHallMixDTO = new DlHallMixDTO();
		DlHallDTO dlHallDTO = new DlHallDTO();
		if (hallParam.getPage() == 1) {
			dlHallDTO = lotteryHallService.getHallData(hallParam);
		}

		PageHelper.startPage(hallParam.getPage(), hallParam.getSize());
		PageInfo<DLArticleDTO> pageInfo = articleService.findArticles("-1");

		dlHallMixDTO.setDlHallDTO(dlHallDTO);
		dlHallMixDTO.setDlArticlePage(pageInfo);

		return ResultGenerator.genSuccessResult("获取彩票大厅数据成功", dlHallMixDTO);
	}
	
	@ApiOperation(value = "获取彩票大厅数据", notes = "获取彩票大厅数据")
	@PostMapping("/getHallData")
	public BaseResult<DlHallDTO> getHallData(
			@Valid @RequestBody PageParam pageParam) {
		HallParam hallParam = new HallParam();
		DlHallMixDTO dlHallMixDTO = new DlHallMixDTO();
		DlHallDTO dlHallDTO = new DlHallDTO();
		if (pageParam.getPageNum() == 1) {
			dlHallDTO = lotteryHallService.getHallData(hallParam);
		}

		PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		PageInfo<DLArticleDTO> pageInfo = articleService.findArticles("-1");

		dlHallMixDTO.setDlHallDTO(dlHallDTO);
		dlHallMixDTO.setDlArticlePage(pageInfo);

		return ResultGenerator.genSuccessResult("获取彩票大厅数据成功", dlHallDTO);
	}

	@ApiOperation(value = "获取彩票大厅数据和咨询列表数据:全彩种菜单20180731新增", notes = "获取彩票大厅数据和咨询列表数据:全彩种菜单20180731新增")
	@PostMapping("/getAllLotteryInHallMixData")
	public BaseResult<DlHallMixDTO> getHallDataMixAllLottery(@Valid @RequestBody HallParam hallParam) {
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		String channel = "";
		String version = "";
		if(userDevice != null) {
			channel = userDevice.getChannel();
			version = userDevice.getAppv();
		}
		DlHallMixDTO dlHallMixDTO = new DlHallMixDTO();
		DlHallDTO dlHallDTO = new DlHallDTO();
		if (hallParam.getPage() == 1) {
			dlHallDTO = lotteryHallService.getHallDataAllLottery1(hallParam,channel,version);
		}

		PageHelper.startPage(hallParam.getPage(), hallParam.getSize());
		PageInfo<DLArticleDTO> pageInfo = articleService.findArticles("-1");

		dlHallMixDTO.setDlHallDTO(dlHallDTO);
		dlHallMixDTO.setDlArticlePage(pageInfo);
		
		//推广活动url
        SysConfigParam sysConfigParam = new SysConfigParam();
        sysConfigParam.setBusinessId(73);
        BaseResult<SysConfigDTO> configData = sysConfigService.querySysConfig(sysConfigParam);
		if(configData != null && configData.getData()!=null){
			dlHallMixDTO.setInviteUrl(configData.getData().getValueTxt());
        }
		return ResultGenerator.genSuccessResult("获取彩票大厅数据成功", dlHallMixDTO);
	}

	@ApiOperation(value = "大厅发现页面的查看更多", notes = "大厅发现页面的查看更多")
	@PostMapping("/moreDiscoveryClass")
	public BaseResult<List<DlDiscoveryHallClassifyDTO>> moreDiscoveryClass(@RequestBody EmptyParam param) {
		List<DlDiscoveryHallClassifyDTO> dlHallDTO = lotteryHallService.moreDiscoveryClass();
		return ResultGenerator.genSuccessResult("获取彩票大厅数据成功", dlHallDTO);
	}
	
	@ApiOperation(value = "获取彩票玩法列表", notes = "获取彩票玩法列表")
	@PostMapping("/getPlayClassifyList")
	public BaseResult<DlPlayClassifyDTO> getPlayClassifyList(
			@Valid @RequestBody DlPlayClassifyParam param) {
		DlPlayClassifyDTO dlPlayClassifyDTO = lotteryHallService
				.getPlayClassifyList(param);
		return ResultGenerator
				.genSuccessResult("获取彩票玩法列表成功", dlPlayClassifyDTO);
	}
}
