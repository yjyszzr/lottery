package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DlHallDTO;
import com.dl.lottery.dto.DlHallMixDTO;
import com.dl.lottery.dto.DlPlayClassifyDTO;
import com.dl.lottery.param.DlPlayClassifyParam;
import com.dl.lottery.param.HallParam;
import com.dl.lottery.param.PageParam;
import com.dl.shop.lottery.service.DlArticleService;
import com.dl.shop.lottery.service.LotteryHallService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/lottery/hall")
public class LotteryHallController {

	@Resource
	private LotteryHallService lotteryHallService;

	@Resource
	private DlArticleService articleService;

	@ApiOperation(value = "获取彩票大厅数据和咨询列表数据", notes = "获取彩票大厅数据和咨询列表数据")
	@PostMapping("/getHallMixData")
	public BaseResult<DlHallMixDTO> getHallDataMix(
			@Valid @RequestBody HallParam hallParam) {
		DlHallMixDTO dlHallMixDTO = new DlHallMixDTO();
		DlHallDTO dlHallDTO = new DlHallDTO();
		if (hallParam.getPageNum() == 1) {
			dlHallDTO = lotteryHallService.getHallData(hallParam);
		}

		PageInfo<DLArticleDTO> pageInfo = articleService.findArticles();

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

		PageInfo<DLArticleDTO> pageInfo = articleService.findArticles();

		dlHallMixDTO.setDlHallDTO(dlHallDTO);
		dlHallMixDTO.setDlArticlePage(pageInfo);

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
