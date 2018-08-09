package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLArticleDetailDTO;
import com.dl.lottery.dto.DLFindListDTO;
import com.dl.lottery.dto.DlHallDTO.DlNavBannerDTO;
import com.dl.lottery.dto.InfoCatDTO;
import com.dl.lottery.enums.CatEnums;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.ArticleCatParam;
import com.dl.lottery.param.ArticleDetailParam;
import com.dl.lottery.param.ArticleIdsParam;
import com.dl.lottery.param.CatArticleParam;
import com.dl.lottery.param.ListArticleParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryNavBanner;
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

	@Resource
	private LotteryNavBannerMapper lotteryNavBannerMapper;
	
	@Resource
	private LotteryConfig lotteryConfig;

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
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticles("-1");
		return ResultGenerator.genSuccessResult(null, rst);
	}
	
	@ApiOperation(value = "发现页", notes = "发现页")
	@PostMapping("/findList")
	public BaseResult<DLFindListDTO> findList(@RequestBody CatArticleParam param) {
		List<DlNavBannerDTO> navBanners = new LinkedList<DlNavBannerDTO>();
		Condition condition = new Condition(LotteryClassify.class);
		condition.setOrderByClause("banner_sort asc");
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("start_time <=", DateUtil.getCurrentTimeLong());
		criteria.andCondition("end_time >", DateUtil.getCurrentTimeLong());
		criteria.andCondition("is_show=", 1);
		criteria.andCondition("show_position=", 1);
		List<LotteryNavBanner> lotteryNavBanners = lotteryNavBannerMapper.selectByCondition(condition);

		if (CollectionUtils.isNotEmpty(lotteryNavBanners)) {
			for (LotteryNavBanner lotteryNavBanner : lotteryNavBanners) {
				DlNavBannerDTO dlNavBannerDTO = new DlNavBannerDTO();
				dlNavBannerDTO.setBannerName(lotteryNavBanner.getBannerName());
				dlNavBannerDTO.setBannerImage(lotteryConfig.getBannerShowUrl() + lotteryNavBanner.getBannerImage());
				if ("1".equals(lotteryNavBanner.getBannerParam())) {
					dlNavBannerDTO.setBannerLink(lotteryConfig.getBanneLinkArticleUrl() + lotteryNavBanner.getBannerLink());// 资讯链接,后面跟资讯链接
				} else if ("2".equals(lotteryNavBanner.getBannerParam())) {
					dlNavBannerDTO.setBannerLink(lotteryConfig.getBanneLinkMatchUrl() + lotteryNavBanner.getBannerLink()); // 赛事链接,后面跟赛事ID
				} else {
					dlNavBannerDTO.setBannerLink(lotteryNavBanner.getBannerLink());// 活动链接,后面跟活动URL
				}
				navBanners.add(dlNavBannerDTO);
			}
		}		
		if("1".equals(param.getExtendCat())) {
			param.setExtendCat("-1");
		}
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticles(param.getExtendCat());
		
		//取其中图文并茂的文章前6篇
		List<DLArticleDTO> bigNewsList = new ArrayList<>();
		List<DLArticleDTO> dtos = rst.getList();
		for(DLArticleDTO dto:dtos) {
			if(1 == dto.getListStyle()) {
				if(bigNewsList.size() >= 6) {
					break;
				}
				bigNewsList.add(dto);
			}
		}
		
		DLFindListDTO findListDTO = new DLFindListDTO();
		findListDTO.setInfoCatList(createCat());
		findListDTO.setNavBanners(navBanners);
		findListDTO.setDlArticlePage(rst);
		findListDTO.setBigNewsList(bigNewsList);
		return ResultGenerator.genSuccessResult(null, findListDTO);
	}

	/**
	 * 目前没有对咨询分类建立表
	 * @return
	 */
	public List<InfoCatDTO> createCat() {
		List<InfoCatDTO> infoCatList = new ArrayList<>();
		for(CatEnums e:CatEnums.values()) {
			InfoCatDTO infoCatDTO = new InfoCatDTO();
			infoCatDTO.setCat(e.getCode());
			infoCatDTO.setCatName(e.getMsg());
			infoCatList.add(infoCatDTO);
		}
		return infoCatList;
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
		if(param.getPage() == null) {
			param.setPage(1);
		}
		if(param.getSize() == null) {
			param.setSize(10);
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
