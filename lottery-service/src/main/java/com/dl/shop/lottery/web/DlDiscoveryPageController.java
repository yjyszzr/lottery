package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.ActiveCenterDTO;
import com.dl.lottery.dto.DLFindListDTO;
import com.dl.lottery.dto.DlBannerForActive;
import com.dl.lottery.dto.DlDiscoveryPageDTO;
import com.dl.lottery.dto.DlLeagueContryDTO;
import com.dl.lottery.dto.DlLeagueDetailDTO;
import com.dl.lottery.dto.DlLeagueDetailForDiscoveryDTO;
import com.dl.lottery.dto.DlLotteryClassifyForOpenPrizeDTO;
import com.dl.lottery.dto.DlNoviceClassroomDTO;
import com.dl.lottery.dto.DlSuperLottoDTO;
import com.dl.lottery.dto.DlSuperLottoDetailsDTO;
import com.dl.lottery.dto.DlTeamDetailForDiscoveryDTO;
import com.dl.lottery.dto.GroupLeagueDTO;
import com.dl.lottery.dto.JCResultDTO;
import com.dl.lottery.dto.SZCPrizeDTO;
import com.dl.lottery.dto.SZCResultDTO;
import com.dl.lottery.param.CatArticleParam;
import com.dl.lottery.param.DiscoveryPageParam;
import com.dl.lottery.param.JCQueryParam;
import com.dl.lottery.param.LeagueDetailForDiscoveryParam;
import com.dl.lottery.param.LeagueDetailParam;
import com.dl.lottery.param.LeagueListByGroupIdParam;
import com.dl.lottery.param.SZCQueryParam;
import com.dl.lottery.param.TeamParam;
import com.dl.shop.lottery.service.DlDiscoveryPageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/discoveryPage")
public class DlDiscoveryPageController {

	@Resource
	private DlDiscoveryPageService dlDiscoveryPageService;

	@ApiOperation(value = "发现页主页", notes = "发现页主页")
	@PostMapping("/homePage")
	public BaseResult<DlDiscoveryPageDTO> homePage(@RequestBody EmptyParam emprt) {
		DlDiscoveryPageDTO discoveryPage = dlDiscoveryPageService.getHomePage();
		return ResultGenerator.genSuccessResult(null, discoveryPage);
	}

	@ApiOperation(value = "发现页资讯", notes = "发现页资讯")
	@PostMapping("/discoveryArticle")
	public BaseResult<DLFindListDTO> discoveryArticle(@RequestBody CatArticleParam param) {
		DLFindListDTO findListDTO = dlDiscoveryPageService.discoveryArticle(param);
		return ResultGenerator.genSuccessResult(null, findListDTO);
	}

	@ApiOperation(value = "开奖结果", notes = "开奖结果")
	@PostMapping("/openPrize")
	public BaseResult<List<DlLotteryClassifyForOpenPrizeDTO>> openPrize(@RequestBody EmptyParam emprt) {
		List<DlLotteryClassifyForOpenPrizeDTO> lotteryClassifyForOpenPrizelist = dlDiscoveryPageService.openPrize();
		return ResultGenerator.genSuccessResult(null, lotteryClassifyForOpenPrizelist);
	}

	@ApiOperation(value = "活动中心", notes = "活动中心")
	@PostMapping("/activeCenter")
	public BaseResult<ActiveCenterDTO> activeCenter(@RequestBody EmptyParam emprt) {
		ActiveCenterDTO activeCenter = dlDiscoveryPageService.activeCenter();
		return ResultGenerator.genSuccessResult(null, activeCenter);
	}

	@ApiOperation(value = "小白课堂", notes = "小白课堂")
	@PostMapping("/noviceClassroom")
	public BaseResult<DlNoviceClassroomDTO> noviceClassroom(@RequestBody EmptyParam emprt) {
		DlNoviceClassroomDTO noviceClassroomDTO = new DlNoviceClassroomDTO();
		List<DlBannerForActive> bannerForActiveList = dlDiscoveryPageService.getNoviceClassroom();
		noviceClassroomDTO.setNoviceClassroomList(bannerForActiveList);
		return ResultGenerator.genSuccessResult(null, noviceClassroomDTO);
	}

	@ApiOperation(value = "大乐透列表，演示用", notes = "大乐透列表，演示用")
	@PostMapping("/lottoList")
	public BaseResult<PageInfo<DlSuperLottoDTO>> lottoList(@RequestBody DiscoveryPageParam param) {
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		PageInfo<DlSuperLottoDTO> pageInfo = dlDiscoveryPageService.lottoList(param);
		return ResultGenerator.genSuccessResult(null, pageInfo);
	}

	@ApiOperation(value = "大乐透详情，演示用", notes = "大乐透详情，演示用")
	@PostMapping("/lottoDetails")
	public BaseResult<DlSuperLottoDetailsDTO> lottoDetails(@RequestBody SZCQueryParam param) {
		DlSuperLottoDetailsDTO superLottoDetails = dlDiscoveryPageService.lottoDetails(param);
		return ResultGenerator.genSuccessResult(null, superLottoDetails);
	}

	@ApiOperation(value = "国家联赛列表<乐德>", notes = "国家联赛列表<乐德>")
	@PostMapping("/leagueListByGroupId")
	public BaseResult<List<DlLeagueContryDTO>> leagueListByGroupId(@RequestBody LeagueListByGroupIdParam param) {
		List<DlLeagueContryDTO> leagueContryList = dlDiscoveryPageService.leagueListByGroupId(param);
		return ResultGenerator.genSuccessResult(null, leagueContryList);
	}

	@ApiOperation(value = "联赛主页列表", notes = "联赛主页列表")
	@PostMapping("/leagueHomePageByGroupId")
	public BaseResult<GroupLeagueDTO> leagueHomePageByGroupId(@RequestBody LeagueListByGroupIdParam param) {
		GroupLeagueDTO groupLeagueDto = dlDiscoveryPageService.leagueHomePageByGroupId(param);
		return ResultGenerator.genSuccessResult(null, groupLeagueDto);
	}

	@ApiOperation(value = "联赛详情<乐德>", notes = "联赛详情<乐德>")
	@PostMapping("/leagueDetail")
	public BaseResult<DlLeagueDetailDTO> leagueDetail(@RequestBody LeagueDetailParam param) {
		DlLeagueDetailDTO leagueDetail = dlDiscoveryPageService.leagueDetail(param);
		return ResultGenerator.genSuccessResult(null, leagueDetail);
	}

	@ApiOperation(value = "联赛详情", notes = "联赛详情")
	@PostMapping("/leagueDetailForDiscovery")
	public BaseResult<DlLeagueDetailForDiscoveryDTO> leagueDetailForDiscovery(@RequestBody LeagueDetailForDiscoveryParam param) {
		DlLeagueDetailForDiscoveryDTO leagueDetail = dlDiscoveryPageService.leagueDetailForDiscovery(param);
		return ResultGenerator.genSuccessResult(null, leagueDetail);
	}

	@ApiOperation(value = "球队详情", notes = "球队详情")
	@PostMapping("/teamDetailForDiscovery")
	public BaseResult<DlTeamDetailForDiscoveryDTO> teamDetailForDiscovery(@RequestBody TeamParam param) {
		DlTeamDetailForDiscoveryDTO teamDetail = dlDiscoveryPageService.teamDetailForDiscovery(param);
		return ResultGenerator.genSuccessResult(null, teamDetail);
	}

	@ApiOperation(value = "竞彩类开奖结果详情", notes = "竞彩类开奖结果详情")
	@PostMapping("/queryJcOpenPrizesByDate")
	public BaseResult<JCResultDTO> queryJcOpenPrizesByDate(@Valid @RequestBody JCQueryParam jcParam) {
		JCResultDTO dto = dlDiscoveryPageService.queryJCResult(jcParam);
		return ResultGenerator.genSuccessResult("success", dto);
	}

	@ApiOperation(value = "数字彩类开奖结果详情", notes = "数字彩类开奖结果详情")
	@PostMapping("/querySzcOpenPrizesByDate")
	public BaseResult<SZCResultDTO> querySzcOpenPrizesByDate(@Valid @RequestBody SZCQueryParam szcParam) {
		SZCResultDTO dto = dlDiscoveryPageService.szcDetail(szcParam);
		return ResultGenerator.genSuccessResult("success", dto);
	}

	@ApiOperation(value = "数字彩类详情列表", notes = "数字彩类详情列表")
	@PostMapping("/szcDetailList")
	public BaseResult<SZCPrizeDTO> szcDetailList(@Valid @RequestBody DiscoveryPageParam param) {
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		SZCPrizeDTO dto = dlDiscoveryPageService.szcDetailList(param);
		return ResultGenerator.genSuccessResult(null, dto);
	}

	
	@ApiOperation(value = "数字彩类详情列表", notes = "数字彩类详情列表")
	@PostMapping("/szcDetailListByStore")
	public BaseResult<SZCPrizeDTO> szcDetailListByStore(@Valid @RequestBody DiscoveryPageParam param) {
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		SZCPrizeDTO dto = dlDiscoveryPageService.szcDetailList(param);
		return ResultGenerator.genSuccessResult(null, dto);
	}
	
}
