package com.dl.shop.lottery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlArticle;
import com.dl.shop.lottery.model.DlArticleClassify;
import com.dl.shop.lottery.model.DlPhoneChannel;

public interface DlArticleMapper extends Mapper<DlArticle> {

	List<DlArticle> findArticles();

	List<DlArticle> findArticlesByIds(@Param("articleIds") List<Integer> articleIds);

	List<DlArticle> findArticlesRelated(@Param("articleId") Integer articleId, @Param("extendCat") String extendCat, @Param("page") Integer page, @Param("size") Integer size);

	List<DlArticle> findArticlesByCat(@Param("extendCat") String extendCat);

	void updateClickNumberById(@Param("articleId") Integer articleId, @Param("clickNumber") Integer clickNumber);

	List<DlArticleClassify> findArticleClassify();

	List<DlPhoneChannel> findPhoneChannel(@Param("channel") String channel);

	List<DlArticle> findArticlesByCats(@Param("catarr") Integer[] catarr);

}