package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.enums.CommonEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLArticleDetailDTO;
import com.dl.lottery.param.ArticleCatParam;
import com.dl.member.api.IUserCollectService;
import com.dl.member.param.ArticleIdParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.dao.DlArticleMapper;
import com.dl.shop.lottery.model.DlArticle;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DlArticleService extends AbstractService<DlArticle> {
    @Resource
    private DlArticleMapper dlArticleMapper;
    
    @Resource
    private IUserCollectService userCollectService;
    
    @Resource
    private LotteryConfig lotteryConfig;

    /**
     * 全部文章
     * @return
     */
	public PageInfo<DLArticleDTO> findArticles() {
		List<DLArticleDTO> dtos = new ArrayList<DLArticleDTO>(0);
		
		List<DlArticle> findAll = dlArticleMapper.findArticles();
		
		PageInfo<DlArticle> pageInfo = new PageInfo<DlArticle>(findAll);
		
		if(null == findAll) {
			return new PageInfo<DLArticleDTO>();
		}
		for(DlArticle article: findAll) {
			DLArticleDTO dto = this.articleDto(article);
			dtos.add(dto);
		}
		
		PageInfo<DLArticleDTO> result = new PageInfo<DLArticleDTO>();
		try {
			BeanUtils.copyProperties(pageInfo, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		result.setList(dtos);
		return result;
	}
	
	
	/**
	 * 根据文章id集合查询所有文章
	 */
	public PageInfo<DLArticleDTO> findArticlesByids(List<Integer> articleIds) {
		List<DLArticleDTO> dtos = new ArrayList<DLArticleDTO>(0);
		
		List<DlArticle> findAll = dlArticleMapper.findArticlesByIds(articleIds);
		
		PageInfo<DlArticle> pageInfo = new PageInfo<DlArticle>(findAll);
		
		if(null == findAll) {
			return new PageInfo<DLArticleDTO>();
		}
		for(DlArticle article: findAll) {
			DLArticleDTO dto = this.articleDto(article);
			dtos.add(dto);
		}
		
		PageInfo<DLArticleDTO> result = new PageInfo<DLArticleDTO>();
		try {
			BeanUtils.copyProperties(pageInfo, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		result.setList(dtos);
		return result;
	}
	
	
	/**
	 * 相关文章
	 * @param articleCat
	 * @return
	 */
	public PageInfo<DLArticleDTO> findArticlesRelated( ArticleCatParam param) {
        Integer articleId = Integer.valueOf(param.getCurrentArticleId());
		List<DLArticleDTO> dtos = new ArrayList<DLArticleDTO>(0);
		DlArticle curArticle = this.findBy("articleId", articleId);
		if(null == curArticle) {
			return new PageInfo<DLArticleDTO>();
		}
		
    	Integer page = param.getPage();
    	page = null == page?1:page;
    	Integer size = param.getSize();
    	size = null == size?20:size;
        PageHelper.startPage(page, size);
		List<DlArticle> findAllRelated = dlArticleMapper.findArticlesRelated(articleId,curArticle.getExtendCat());
		PageInfo<DlArticle> pageInfo = new PageInfo<DlArticle>(findAllRelated);
		if(null == findAllRelated) {
			return new PageInfo<DLArticleDTO>();
		}
		
		for(DlArticle article: findAllRelated) {
			DLArticleDTO dto = this.articleDto(article);
			dtos.add(dto);
		}
		
		PageInfo<DLArticleDTO> result = new PageInfo<DLArticleDTO>();
		try {
			BeanUtils.copyProperties(pageInfo, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		result.setList(dtos);
		return result;
	}
	

	private DLArticleDTO articleDto(DlArticle article) {
		DLArticleDTO dto = new DLArticleDTO();
		dto.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(article.getAddTime()), DateUtil.time_sdf));
		dto.setArticleId(article.getArticleId());
		List<String> picList = Arrays.asList(article.getArticleThumb().split(","));
		dto.setArticleThumb(picList);
		dto.setClickNumber(article.getClickNumber());
		dto.setExtendCat(CommonEnum.getName(Integer.valueOf(article.getExtendCat())));
		dto.setKeywords(article.getKeywords());
		dto.setLink(lotteryConfig.getShareInfoUrl()+article.getArticleId());
		dto.setListStyle(article.getListStyle());
		dto.setMatchId(article.getMatchId());
		dto.setRelatedTeam(article.getRelatedTeam());
		dto.setTitle(article.getTitle());
		dto.setSummary(article.getSummary());
		return dto;
	}

	public DLArticleDetailDTO findArticleById(Integer id) {
		Integer userId = SessionUtil.getUserId();
		DlArticle article = super.findById(id);
		if(null == article ) {
			return null;
		}
		DLArticleDetailDTO dto = new DLArticleDetailDTO();
		
		//是否收藏
		String isCollect = ProjectConstant.IS_NOT_COLLECT;
		if(null != userId) {
			ArticleIdParam articleIdParam = new ArticleIdParam();
			articleIdParam.setArticleId(id);
			BaseResult<String> rst =  userCollectService.isCollect(articleIdParam);
			if(rst.getCode() != 0) {
				log.error("判断是否收藏失败:"+rst.getMsg());
				isCollect = ProjectConstant.IS_NOT_COLLECT;
			}
			isCollect = rst.getData();
		}
		
		dto.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(article.getAddTime()), DateUtil.time_sdf));
		dto.setArticleId(article.getArticleId());
		dto.setArticleThumb(article.getArticleThumb());
		dto.setClickNumber(article.getClickNumber());
		dto.setExtendCat(article.getExtendCat());
		dto.setKeywords(article.getKeywords());
		dto.setLink(article.getLink());
		dto.setListStyle(article.getListStyle());
		dto.setMatchId(article.getMatchId());
		dto.setRelatedTeam(article.getRelatedTeam());
		dto.setTitle(article.getTitle());
		dto.setContent(article.getContent());
		dto.setIsCollect(isCollect);
		dto.setSummary(article.getSummary());
		List<String> labelList = Arrays.asList(article.getKeywords().split(","));
		dto.setLabelsArr(labelList);
		List<DLArticleDTO> articles = new ArrayList<DLArticleDTO>();
		
		//第一期通过 分类 来关联
		PageHelper.startPage(1, 3);
		List<DlArticle> findAllRelated = dlArticleMapper.findArticlesRelated(article.getArticleId(), article.getExtendCat());
		for(DlArticle a: findAllRelated) {
			DLArticleDTO d = this.articleDto(a);
			articles.add(d);
		}
		dto.setArticles(articles);
		
		return dto;
	}

}
