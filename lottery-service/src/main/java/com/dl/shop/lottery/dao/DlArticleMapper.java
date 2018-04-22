package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlArticle;

public interface DlArticleMapper extends Mapper<DlArticle> {

	List<DlArticle> findArticles();
	
	List<DlArticle> findArticlesRelated(@Param("articleId") Integer articleId,@Param("extendCat") String extendCat);
	
}