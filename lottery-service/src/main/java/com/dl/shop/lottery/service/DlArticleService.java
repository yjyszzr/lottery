package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.dl.shop.lottery.model.DlArticleClassify;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(value = "transactionManager1")
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
	 * 
	 * @return
	 */
	/**
	 * 全部文章,catArticle:
	 * 
	 * @param catArticle
	 *            -1:全部文章，非-1：其他分类文章
	 * @return
	 */
	public PageInfo<DLArticleDTO> findArticles(String catArticle) {
		List<DLArticleDTO> dtos = new ArrayList<DLArticleDTO>(0);
		List<DlArticle> findAll = new ArrayList<>();
		if (catArticle.equals("-1")) {// 全部
			findAll = dlArticleMapper.findArticles();
		} else {
			findAll = dlArticleMapper.findArticlesByCat(catArticle);
		}

		// List<DlArticle> collect = findAll.stream().filter(item->1 ==
		// (item.getIsStick()==null?0:item.getIsStick())).collect(Collectors.toList());
		// if(CollectionUtils.isNotEmpty(collect)) {
		// collect.sort((item1,item2)->{
		// if(item2.getStickTime() == null)item2.setStickTime(0);
		// if(item1.getStickTime() == null)item1.setStickTime(0);
		// return item2.getStickTime().compareTo(item1.getStickTime());
		// });
		// findAll.removeAll(collect);
		// findAll.addAll(0, collect);
		// }

		PageInfo<DlArticle> pageInfo = new PageInfo<DlArticle>(findAll);

		if (null == findAll) {
			return new PageInfo<DLArticleDTO>();
		}
		List<DlArticleClassify> articleClassifyList = dlArticleMapper.findArticleClassify();
		Map<Integer, DlArticleClassify> articleClassifyMap = new HashMap<Integer, DlArticleClassify>(articleClassifyList.size());
		articleClassifyList.forEach(s -> articleClassifyMap.put(s.getId(), s));
		for (DlArticle article : findAll) {
			DLArticleDTO dto = this.articleDto(article);
			Integer extendCatValue = Integer.parseInt(dto.getExtendCat());
			DlArticleClassify articleClassify = articleClassifyMap.get(extendCatValue);
			if (null != articleClassify) {
				article.setExtendCat(articleClassify.getClassifyName());
			}
			dto.setAuthor(article.getAuthor());
			dto.setAddTime(article.getAddTime().toString());
			dto.setArticleThumb(dto.getArticleThumb());
			dto.setIsStick(String.valueOf(article.getIsStick()));
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

		List<DlArticle> findAll = new ArrayList<DlArticle>();
		List<DlArticle> findAllA = dlArticleMapper.findArticlesByIds(articleIds);

		for (int i = 0; i < articleIds.size(); i++) {
			for (int j = 0; j < findAllA.size(); j++) {
				if (articleIds.get(i).equals(findAllA.get(j).getArticleId())) {
					findAll.add(findAllA.get(j));
				}
			}
		}

		PageInfo<DlArticle> pageInfo = new PageInfo<DlArticle>(findAll);

		if (null == findAll) {
			return new PageInfo<DLArticleDTO>();
		}
		for (DlArticle article : findAll) {
			DLArticleDTO dto = this.articleDto(article);
			dto.setAddTime(article.getAddTime().toString());
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
	 * 
	 * @param articleCat
	 * @return
	 */
	public PageInfo<DLArticleDTO> findArticlesRelated(ArticleCatParam param) {
		Integer articleId = Integer.valueOf(param.getCurrentArticleId());
		List<DLArticleDTO> dtos = new ArrayList<DLArticleDTO>(0);
		DlArticle curArticle = this.findBy("articleId", articleId);
		if (null == curArticle) {
			return new PageInfo<DLArticleDTO>();
		}

		List<DlArticle> findAllRelated = null;
		if (null != curArticle.getExtendCat() && null != articleId) {
			// PageHelper.startPage(param.getPage(), param.getSize());
			try {
				findAllRelated = dlArticleMapper.findArticlesRelated(articleId, curArticle.getExtendCat(), (param.getPage() - 1) * param.getSize(), param.getSize());
			} finally {
				PageHelper.clearPage();
			}
		}

		PageInfo<DlArticle> pageInfo = new PageInfo<DlArticle>(findAllRelated);

		for (DlArticle article : findAllRelated) {
			DLArticleDTO dto = this.articleDto(article);
			dto.setAddTime(article.getAddTime().toString());
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

		List<String> articleThumbList = new ArrayList<String>();
		if (!StringUtils.isEmpty(article.getArticleThumb())) {
			List<String> picList = Arrays.asList(article.getArticleThumb().split(","));
			articleThumbList = picList.stream().map(s -> lotteryConfig.getBannerShowUrl() + s.toString()).collect(Collectors.toList());
		}

		dto.setArticleThumb(articleThumbList);
		dto.setClickNumber(article.getClickNumber());
		dto.setExtendCat(article.getExtendCat());
		dto.setKeywords(article.getKeywords());
		dto.setLink(lotteryConfig.getShareInfoUrl() + article.getArticleId());
		dto.setListStyle(article.getListStyle());
		dto.setMatchId(article.getMatchId());
		dto.setRelatedTeam(article.getRelatedTeam());
		dto.setTitle(article.getTitle());
		dto.setAuthor(article.getAuthor());
		dto.setSummary(article.getSummary());
		return dto;
	}

	public DLArticleDetailDTO findArticleById(Integer id) {
		Integer userId = SessionUtil.getUserId();
		DlArticle article = super.findById(id);
		if (null == article) {
			return null;
		}
		// 点击该文章 增加一次点击数
		Integer clickNumber = article.getClickNumber();
		if (clickNumber == null) {
			clickNumber = 0;
		}
		clickNumber += 1;
		dlArticleMapper.updateClickNumberById(article.getArticleId(), clickNumber);
		DLArticleDetailDTO dto = new DLArticleDetailDTO();

		// 是否收藏
		String isCollect = ProjectConstant.IS_NOT_COLLECT;
		if (null != userId) {
			ArticleIdParam articleIdParam = new ArticleIdParam();
			articleIdParam.setArticleId(id);
			BaseResult<String> rst = userCollectService.isCollect(articleIdParam);
			if (rst.getCode() != 0) {
				log.error("判断是否收藏失败:" + rst.getMsg());
				isCollect = ProjectConstant.IS_NOT_COLLECT;
			}
			isCollect = rst.getData();
		}

		dto.setAddTime(article.getAddTime().toString());
		dto.setArticleId(article.getArticleId());
		dto.setArticleThumb(article.getArticleThumb());
		dto.setClickNumber(article.getClickNumber());
		dto.setExtendCat(article.getExtendCat());
		dto.setKeywords(article.getKeywords());
		dto.setLink(lotteryConfig.getShareInfoUrl() + article.getArticleId());
		dto.setListStyle(article.getListStyle());
		dto.setMatchId(article.getMatchId());
		dto.setRelatedTeam(article.getRelatedTeam());
		dto.setTitle(article.getTitle());
		dto.setContent(article.getContent());
		dto.setIsCollect(isCollect);
		dto.setSummary(article.getSummary());
		dto.setAuthor(article.getAuthor());
		List<String> labelList = new ArrayList<String>();
		if (!StringUtils.isEmpty(article.getKeywords())) {
			labelList = Arrays.asList(article.getKeywords().split(","));
		}
		dto.setLabelsArr(labelList);
		List<DLArticleDTO> articles = new ArrayList<DLArticleDTO>();

		// 第一期通过 分类 来关联
		List<DlArticle> findAllRelated = dlArticleMapper.findArticlesRelated(article.getArticleId(), article.getExtendCat(), 1, 3);
		for (DlArticle a : findAllRelated) {
			DLArticleDTO d = this.articleDto(a);
			d.setAddTime(a.getAddTime().toString());
			articles.add(d);
		}
		dto.setArticles(articles);

		return dto;
	}

	public List<DlArticleClassify> findArticleClassify() {
		return dlArticleMapper.findArticleClassify();
	}

	public PageInfo<DLArticleDTO> findArticlesByCats(Integer[] catarr) {
		List<DlArticle> findAll = new ArrayList<>();
		List<DLArticleDTO> findAllDTO = new ArrayList<>();
		findAll = dlArticleMapper.findArticlesByCats(catarr);
		for (int i = 0; i < findAll.size(); i++) {
			findAllDTO.add(articleDto(findAll.get(i)));
		}
		PageInfo<DLArticleDTO> pageInfoRSt = new PageInfo<DLArticleDTO>(findAllDTO);
		return pageInfoRSt;
	}
}
