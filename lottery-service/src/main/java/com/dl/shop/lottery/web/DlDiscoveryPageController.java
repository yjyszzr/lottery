package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLHotLeagueDTO;
import com.dl.lottery.dto.DlDiscoveryHallClassifyDTO;
import com.dl.lottery.dto.DlDiscoveryPageDTO;
import com.dl.lottery.dto.DlTopScorerDTO;
import com.dl.lottery.dto.DlTopScorerMemberDTO;
import com.dl.lottery.dto.InfoCatDTO;
import com.dl.lottery.param.DiscoveryPageParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.dao.DlArticleMapper;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.model.DlDiscoveryHallClassify;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.service.DlArticleService;
import com.dl.shop.lottery.service.DlDiscoveryHallClassifyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/discoveryPage")
public class DlDiscoveryPageController {

	private final static Logger logger = LoggerFactory.getLogger(DlDiscoveryPageController.class);
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

	@ApiOperation(value = "发现页主页", notes = "发现页主页")
	@PostMapping("/homePage")
	public BaseResult<DlDiscoveryPageDTO> homePage(@RequestBody EmptyParam emprt) {
		Condition condition = new Condition(DlDiscoveryHallClassify.class);
		condition.setOrderByClause("sort asc");
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("status =", 1);
		criteria.andCondition("is_show =", 1);
		List<DlDiscoveryHallClassify> discoveryHallClassifyList = dlDiscoveryHallClassifyService.findByCondition(condition);
		List<DlDiscoveryHallClassifyDTO> discoveryHallClassifyDTOList = new ArrayList<>(discoveryHallClassifyList.size());
		for (DlDiscoveryHallClassify s : discoveryHallClassifyList) {
			DlDiscoveryHallClassifyDTO dto = new DlDiscoveryHallClassifyDTO();
			dto.setClassImg(lotteryConfig.getBannerShowUrl() + s.getClassImg());
			dto.setClassName(s.getClassName());
			dto.setRedirectUrl(s.getRedirectUrl());
			dto.setStatus(s.getStatus());
			dto.setStatusReason(s.getStatusReason());
			discoveryHallClassifyDTOList.add(dto);
		}
		DlDiscoveryPageDTO discoveryPage = new DlDiscoveryPageDTO();

		// 设置八个分类
		discoveryPage.setDiscoveryHallClassifyList(discoveryHallClassifyDTOList);
		List<DLHotLeagueDTO> hotLeagueList = new ArrayList<DLHotLeagueDTO>(8);
		for (int i = 0; i < 8; i++) {
			DLHotLeagueDTO hotLeague = new DLHotLeagueDTO();
			hotLeague.setActUrl("https://www.baidu.com");
			hotLeague.setDetail("联赛详情" + i);
			hotLeague.setIconImg(lotteryConfig.getBannerShowUrl() + "uploadImgs/20180731/daletou.png");
			hotLeague.setTitle("联赛标题" + i);
			hotLeagueList.add(hotLeague);
		}
		// 设置热门联赛
		discoveryPage.setHotLeagueList(hotLeagueList);
		List<DlTopScorerDTO> topScorerList = new ArrayList<DlTopScorerDTO>(5);
		for (int i = 0; i < 5; i++) {
			DlTopScorerDTO topScorer = new DlTopScorerDTO();
			List<DlTopScorerMemberDTO> topScorerMemberList = new ArrayList<DlTopScorerMemberDTO>(5);
			for (int j = 0; j < 5; j++) {
				DlTopScorerMemberDTO topScorerMember = new DlTopScorerMemberDTO();
				topScorerMember.setMemberName("成员" + j);
				topScorerMember.setRanking(j + 1);
				topScorerMember.setTopScorerTeam("球队" + j);
				topScorerMember.setTotalGoal(20 - j * 3);
				topScorerMemberList.add(topScorerMember);
			}
			topScorer.setLeagueName("射手联赛" + i);
			topScorer.setTopScorerMemberList(topScorerMemberList);
			topScorerList.add(topScorer);
		}
		// 设置去个射手榜
		discoveryPage.setTopScorerDTOList(topScorerList);
		return ResultGenerator.genSuccessResult(null, discoveryPage);
	}

	@ApiOperation(value = "发现页资讯", notes = "发现页资讯")
	@PostMapping("/discoveryArticle")
	public BaseResult<PageInfo<DLArticleDTO>> discoveryArticle(@RequestBody DiscoveryPageParam param) {
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		DlArticleController articleControllerTools = new DlArticleController();
		List<InfoCatDTO> catList = articleControllerTools.createCat();
		PageInfo<DLArticleDTO> rst = new PageInfo<DLArticleDTO>();
		if (catList.size() == 0) {
			List<LotteryClassify> classifyList = lotteryClassifyMapper.selectAllLotteryClasses();
			Integer[] catarr = new Integer[classifyList.size()];
			for (int i = 0; i < classifyList.size(); i++) {
				catarr[i] = classifyList.get(i).getLotteryClassifyId();
			}
			rst = dlArticleService.findArticlesByCats(catarr);
		} else {
			Integer[] catarr = new Integer[catList.size()];
			for (int i = 0; i < catList.size(); i++) {
				catarr[i] = Integer.parseInt(catList.get(i).getCat());
			}
			rst = dlArticleService.findArticlesByCats(catarr);
		}
		return ResultGenerator.genSuccessResult(null, rst);
	}

}
