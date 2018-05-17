package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLArticleDetailDTO;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.ArticleCatParam;
import com.dl.lottery.param.ArticleDetailParam;
import com.dl.lottery.param.ArticleIdsParam;
import com.dl.lottery.param.ListArticleParam;
import com.dl.shop.lottery.service.DlArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by CodeGenerator on 2018/04/19.
 */
@RestController
@RequestMapping("/dl/article")
public class DlArticleController {
	@Resource
	private DlArticleService dlArticleService;

	/*
	 * @PostMapping("/add") public BaseResult add(DlArticle dlArticle) {
	 * dlArticleService.save(dlArticle); return
	 * ResultGenerator.genSuccessResult(); }
	 * 
	 * @PostMapping("/delete") public BaseResult delete(@RequestParam Integer
	 * id) { dlArticleService.deleteById(id); return
	 * ResultGenerator.genSuccessResult(); }
	 * 
	 * @PostMapping("/update") public BaseResult update(DlArticle dlArticle) {
	 * dlArticleService.update(dlArticle); return
	 * ResultGenerator.genSuccessResult(); }
	 */

	@ApiOperation(value = "资讯详情", notes = "资讯详情")
	@PostMapping("/detail")
	public BaseResult<DLArticleDetailDTO> detail(@RequestBody ArticleDetailParam param) {
		DLArticleDetailDTO dlArticle = dlArticleService.findArticleById(param.getArticleId());
		if(dlArticle == null) {
			return ResultGenerator.genResult(LotteryResultEnum.ARTICLE_NULL.getCode(), LotteryResultEnum.ARTICLE_NULL.getMsg());
		}
		String extendCatValue = dlArticle.getExtendCat();
		if ("1".equals(extendCatValue)) {
			dlArticle.setExtendCat("今日关注");
		} else if ("2".equals(extendCatValue)) {
			dlArticle.setExtendCat("竞彩预测");
		} else if ("3".equals(extendCatValue)) {
			dlArticle.setExtendCat("牛人分析");
		} else if ("4".equals(extendCatValue)) {
			dlArticle.setExtendCat("其他");
		}
		return ResultGenerator.genSuccessResult(null, dlArticle);
	}

	@ApiOperation(value = "资讯首页", notes = "资讯首页")
	@PostMapping("/list")
	public BaseResult<PageInfo<DLArticleDTO>> list(@RequestBody ListArticleParam param) {
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticles();
		return ResultGenerator.genSuccessResult(null, rst);
	}

	/**
	 * 根据当前文章的分类查找相关文章
	 * 
	 * @param param
	 * @return
	 */
	@ApiOperation(value = "相关文章", notes = "相关文章")
	@PostMapping("/relatedArticles")
	public BaseResult<PageInfo<DLArticleDTO>> relatedArticles(@RequestBody ArticleCatParam param) {
		if(StringUtils.isBlank(param.getCurrentArticleId())) {
			return ResultGenerator.genResult(LotteryResultEnum.ARTICLE_ID_NULL.getCode(), LotteryResultEnum.ARTICLE_ID_NULL.getMsg());
		}
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticlesRelated(param);
		return ResultGenerator.genSuccessResult(null, rst);
	}

	/**
	 * 根据文章id集合查询文章列表
	 */
	@ApiOperation(value = "前端不用，根据文章id集合查询文章列表", notes = "前端不用，根据文章id集合查询文章列表")
	@PostMapping("/queryArticlesByIds")
	public BaseResult<PageInfo<DLArticleDTO>> queryArticlesByIds(@RequestBody ArticleIdsParam param) {
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticlesByids(param.getArticleIds());
		return ResultGenerator.genSuccessResult(null, rst);
	}

}
