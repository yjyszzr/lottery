package com.dl.shop.lottery.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLArticleDetailDTO;
import com.dl.shop.lottery.dao.DlArticleMapper;
import com.dl.shop.lottery.model.DlArticle;

@Service
@Transactional
public class DlArticleService extends AbstractService<DlArticle> {
    @Resource
    private DlArticleMapper dlArticleMapper;

	public List<DLArticleDTO> findArticles() {
		List<DLArticleDTO> dtos = new ArrayList<DLArticleDTO>(0);
		List<DlArticle> findAll = dlArticleMapper.findArticles();
		if(null == findAll) {
			return dtos;
		}
		for(DlArticle article: findAll) {
			DLArticleDTO dto = this.articleDto(article);
			dtos.add(dto);
		}
		return dtos;
	}

	private DLArticleDTO articleDto(DlArticle article) {
		DLArticleDTO dto = new DLArticleDTO();
		dto.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(article.getAddTime()), DateUtil.time_sdf));
		dto.setArticleId(article.getArticleId());
		List<String> picList = Arrays.asList(article.getArticleThumb().split(","));
		dto.setArticleThumb(picList);
		dto.setClickNumber(article.getClickNumber());
		dto.setExtendCat(article.getExtendCat());
		dto.setKeywords(article.getKeywords());
		dto.setLink(article.getLink());
		dto.setListStyle(article.getListStyle());
		dto.setMatchId(article.getMatchId());
		dto.setRelatedTeam(article.getRelatedTeam());
		dto.setTitle(article.getTitle());
		dto.setSummary(article.getSummary());
		return dto;
	}

	public DLArticleDetailDTO findArticleById(Integer id) {
		DlArticle article = super.findById(id);
		if(null == article ) {
			return null;
		}
		DLArticleDetailDTO dto = new DLArticleDetailDTO();
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
		dto.setSummary(article.getSummary());
		List<DLArticleDTO> articles = new ArrayList<DLArticleDTO>();
		dto.setArticles(articles);
		return dto;
	}

}
