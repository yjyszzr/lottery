package com.dl.shop.lottery.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.shop.lottery.model.DlArticle;

public interface DlArticleMapper extends Mapper<DlArticle> {

	List<DlArticle> findArticles();
}