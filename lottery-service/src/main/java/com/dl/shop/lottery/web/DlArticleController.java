package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLArticleDetailDTO;
import com.dl.lottery.dto.DLFindListDTO;
import com.dl.lottery.dto.DlHallDTO.DlNavBannerDTO;
import com.dl.lottery.dto.InfoCatDTO;
import com.dl.lottery.enums.LotteryResultEnum;
import com.dl.lottery.param.ArticleCatParam;
import com.dl.lottery.param.ArticleDetailParam;
import com.dl.lottery.param.ArticleIdsParam;
import com.dl.lottery.param.CatArticleParam;
import com.dl.lottery.param.ListArticleParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.dao.DlArticleMapper;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.model.DlArticle;
import com.dl.shop.lottery.model.DlArticleClassify;
import com.dl.shop.lottery.model.DlPhoneChannel;
import com.dl.shop.lottery.model.DlSorts;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.service.DlArticleService;
import com.dl.shop.lottery.service.DlDiscoveryHallClassifyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by CodeGenerator on 2018/04/19.
 */
@RestController
@RequestMapping("/dl/article")
public class DlArticleController {
	private final static Logger logger = LoggerFactory.getLogger(DlArticleController.class);
	@Resource
	private DlArticleService dlArticleService;

	@Resource
	private LotteryNavBannerMapper lotteryNavBannerMapper;

	@Resource
	private LotteryConfig lotteryConfig;

	@Resource
	private DlArticleMapper dlArticleMapper;
	@Resource
	private LotteryClassifyMapper lotteryClassifyMapper;

	@Resource
	private DlDiscoveryHallClassifyService dlDiscoveryHallClassifyService;

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
		if (dlArticle == null) {
			return ResultGenerator.genResult(LotteryResultEnum.ARTICLE_NULL.getCode(), LotteryResultEnum.ARTICLE_NULL.getMsg());
		}
		List<DlArticleClassify> articleClassifyList = dlArticleService.findArticleClassify();
		Map<Integer, DlArticleClassify> articleClassifyMap = new HashMap<Integer, DlArticleClassify>(articleClassifyList.size());
		articleClassifyList.forEach(s -> articleClassifyMap.put(s.getId(), s));
		Integer extendCatValue = Integer.parseInt(dlArticle.getExtendCat());
		DlArticleClassify articleClassify = articleClassifyMap.get(extendCatValue);
		if (null != articleClassify) {
			dlArticle.setExtendCat(articleClassify.getClassifyName());
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

	@ApiOperation(value = "发现页", notes = "发现页(老版本发现页)")
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
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticles(param.getExtendCat());

		// 临时需求,可以删除,20180809 周丹提出的临时需求:取其中文章前6篇,临时支持显示样式5,全屏图
		List<DLArticleDTO> bigNews = this.createBigNewsList(param.getExtendCat());
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		String channel = userDevice.getChannel();
		// String channel = "c10021";
		if (StringUtils.isNotEmpty(channel)) {
			if ("c26013".equals(channel)) {// 临时需求,可以删除,乐得体育的样式
				if (bigNews.size() > 0) {
					bigNews = bigNews.subList(0, 1);
				}
			}
		}
		DLFindListDTO findListDTO = new DLFindListDTO();
		findListDTO.setInfoCatList(createCat(channel));
		findListDTO.setNavBanners(navBanners);
		findListDTO.setDlArticlePage(rst);
		findListDTO.setBigNewsList(bigNews);
		return ResultGenerator.genSuccessResult(null, findListDTO);
	}

	/*
	 * 20180809 周丹提出的临时需求:取其中文章前6篇,临时支持显示样式5,全屏图
	 */
	public List<DLArticleDTO> createBigNewsList(String extendCat) {
		List<DLArticleDTO> bigNewsList = new ArrayList<>();
		List<DlArticle> findAll = dlArticleMapper.findArticlesByCat(extendCat);
		for (DlArticle article : findAll) {
			if (null == article.getIsStick()) {
				continue;
			}
			if (1 == article.getListStyle() && 1 == article.getIsStick()) {
				DLArticleDTO newDTO = new DLArticleDTO();
				if (bigNewsList.size() >= 6) {
					break;
				}
				newDTO.setListStyle(5);
				newDTO.setArticleId(article.getArticleId());
				newDTO.setLink(article.getLink());
				newDTO.setAuthor(article.getAuthor());
				newDTO.setTitle(article.getTitle());
				newDTO.setIsStick(String.valueOf(article.getIsStick()));
				List<String> articleThumbList = new ArrayList<String>();
				if (!StringUtils.isEmpty(article.getArticleThumb())) {
					List<String> picList = Arrays.asList(article.getArticleThumb().split(","));
					articleThumbList = picList.stream().map(s -> lotteryConfig.getBannerShowUrl() + s.toString()).collect(Collectors.toList());
				}
				newDTO.setArticleThumb(articleThumbList);
				bigNewsList.add(newDTO);
			}
		}
		return bigNewsList;
	}

	/**
	 * 目前没有对咨询分类建立表
	 * 
	 * @return
	 */
	public List<InfoCatDTO> createCat(String channel) {
		List<InfoCatDTO> infoCatList = new ArrayList<InfoCatDTO>();
		logger.info("channel===============================================" + channel);
		List<DlArticleClassify> articleClassifyCatList = dlArticleMapper.findArticleClassify();
		if (channel.equals("h5")) {
			for (int i = 0; i < articleClassifyCatList.size(); i++) {
				InfoCatDTO infoCat = new InfoCatDTO();
				infoCat.setCat(articleClassifyCatList.get(i).getId().toString());
				infoCat.setCatName(articleClassifyCatList.get(i).getClassifyName());
				infoCatList.add(infoCat);
			}
			logger.info("H5端的infoCatList===================================" + infoCatList);
		} else {
			List<DlPhoneChannel> phoneChannelList = dlArticleMapper.findPhoneChannel(channel);
			if (phoneChannelList.size() > 0) {
				// 获取该渠道的资讯列表
				List<String> resultStr = Arrays.asList(phoneChannelList.get(0).getArticleClassifyIds().split(","));
				logger.info("ArticleClassifyIds======================================" + resultStr);
				if (resultStr.size() == 1 && resultStr.get(0).equals("0")) {
					for (int i = 0; i < articleClassifyCatList.size(); i++) {
						InfoCatDTO infoCat = new InfoCatDTO();
						infoCat.setCat(articleClassifyCatList.get(i).getId().toString());
						infoCat.setCatName(articleClassifyCatList.get(i).getClassifyName());
						infoCatList.add(infoCat);
					}
					logger.info("资讯分类id等于0时的infoCatList===================================" + infoCatList);
				} else {
					Map<Integer, DlArticleClassify> map = new HashMap<Integer, DlArticleClassify>(articleClassifyCatList.size());
					articleClassifyCatList.forEach(s -> map.put(s.getId(), s));
					String sortsStr = null == phoneChannelList.get(0).getSorts() ? "0" : phoneChannelList.get(0).getSorts();

					// 等于0代表没有排序
					if (sortsStr.equals("0")) {
						for (int i = 0; i < resultStr.size(); i++) {
							DlArticleClassify articleClassifyMap = map.get(Integer.parseInt(resultStr.get(i)));
							InfoCatDTO infoCat = new InfoCatDTO();
							infoCat.setCat(articleClassifyMap.getId().toString());
							infoCat.setCatName(articleClassifyMap.getClassifyName());
							infoCatList.add(infoCat);
						}
					} else {
						List<String> sortStr = Arrays.asList(sortsStr.split(","));
						List<DlSorts> sortsList = new ArrayList<DlSorts>(sortStr.size());
						DlSorts sortsArr[] = new DlSorts[sortStr.size()];
						// 组装并排序
						for (int i = 0; i < sortStr.size(); i++) {
							String[] arrStr = sortStr.get(i).split(":");
							DlSorts sorts;
							if (arrStr.length > 1) {
								sorts = new DlSorts(Integer.parseInt(arrStr[0]), Integer.parseInt(arrStr[1]));
							} else {
								sorts = new DlSorts(Integer.parseInt(arrStr[0]), 0);
							}
							sortsArr[i] = sorts;
							sortsList.add(sorts);
						}
						Arrays.sort(sortsArr);
						for (int i = 0; i < sortsArr.length; i++) {
							DlSorts sorts = sortsArr[i];
							DlArticleClassify articleClassifyMap = map.get(sorts.getClassifyId());
							if (null != articleClassifyMap) {
								InfoCatDTO infoCat = new InfoCatDTO();
								infoCat.setCat(articleClassifyMap.getId().toString());
								infoCat.setCatName(articleClassifyMap.getClassifyName());
								infoCatList.add(infoCat);
							}
						}
					}
					logger.info("资讯分类id不等于0时的infoCatList===================================" + infoCatList);
				}
			}
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
		if (StringUtils.isBlank(param.getCurrentArticleId())) {
			return ResultGenerator.genResult(LotteryResultEnum.ARTICLE_ID_NULL.getCode(), LotteryResultEnum.ARTICLE_ID_NULL.getMsg());
		}
		if (param.getPage() == null) {
			param.setPage(1);
		}
		if (param.getSize() == null) {
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
