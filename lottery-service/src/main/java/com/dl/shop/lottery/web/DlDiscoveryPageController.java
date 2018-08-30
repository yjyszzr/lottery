package com.dl.shop.lottery.web;

import io.swagger.annotations.ApiOperation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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
import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.ActiveCenterDTO;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLHotLeagueDTO;
import com.dl.lottery.dto.DlBannerForActive;
import com.dl.lottery.dto.DlDiscoveryHallClassifyDTO;
import com.dl.lottery.dto.DlDiscoveryPageDTO;
import com.dl.lottery.dto.DlLeagueContryDTO;
import com.dl.lottery.dto.DlLeaguePageDTO;
import com.dl.lottery.dto.DlLotteryClassifyForOpenPrizeDTO;
import com.dl.lottery.dto.DlSuperLottoDTO;
import com.dl.lottery.dto.DlTopScorerDTO;
import com.dl.lottery.dto.DlTopScorerMemberDTO;
import com.dl.lottery.dto.InfoCatDTO;
import com.dl.lottery.dto.LeagueInfoDTO;
import com.dl.lottery.param.DiscoveryPageParam;
import com.dl.lottery.param.LottoDetailsParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.dao.DlArticleMapper;
import com.dl.shop.lottery.dao.LotteryClassifyMapper;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.model.DlDiscoveryHallClassify;
import com.dl.shop.lottery.model.DlLeagueContry;
import com.dl.shop.lottery.model.DlLeagueGroup;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlSuperLotto;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.dl.shop.lottery.service.DlArticleService;
import com.dl.shop.lottery.service.DlDiscoveryHallClassifyService;
import com.dl.shop.lottery.service.DlLeagueContryService;
import com.dl.shop.lottery.service.DlLeagueGroupService;
import com.dl.shop.lottery.service.DlLeagueInfoService;
import com.dl.shop.lottery.service.DlSuperLottoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/discoveryPage")
public class DlDiscoveryPageController {

	private final static Logger logger = LoggerFactory.getLogger(DlDiscoveryPageController.class);
	@Resource
	private DlArticleService dlArticleService;

	@Resource
	private DlSuperLottoService dlSuperLottoService;

	@Resource
	private DlLeagueGroupService dlLeagueGroupService;

	@Resource
	private DlLeagueContryService dlLeagueContryService;

	@Resource
	private DlLeagueInfoService dlLeagueInfoService;

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
				topScorerMember.setMemberName(i + "成员" + j);
				topScorerMember.setRanking(j + 1);
				topScorerMember.setTopScorerTeam(i + "球队" + j);
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

	@ApiOperation(value = "开奖结果", notes = "开奖结果")
	@PostMapping("/openPrize")
	public BaseResult<List<DlLotteryClassifyForOpenPrizeDTO>> openPrize(@RequestBody EmptyParam emprt) {
		// 获取彩种相关信息
		List<DlLotteryClassifyForOpenPrizeDTO> lotteryClassifyList = new ArrayList<DlLotteryClassifyForOpenPrizeDTO>();
		List<LotteryClassify> classifyList = lotteryClassifyMapper.selectAllLotteryClasses();
		for (LotteryClassify s : classifyList) {
			DlLotteryClassifyForOpenPrizeDTO lotteryClassifyForOpenPrize = new DlLotteryClassifyForOpenPrizeDTO();
			lotteryClassifyForOpenPrize.setLotteryId(s.getLotteryClassifyId());
			lotteryClassifyForOpenPrize.setLotteryName(s.getLotteryName());
			lotteryClassifyForOpenPrize.setLotteryIcon(lotteryConfig.getBannerShowUrl() + s.getLotteryImg());
			lotteryClassifyForOpenPrize.setPeriod("201808280001");
			if (s.getLotteryName().equals("竞彩篮球")) {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setHomeTeam("竞彩篮球主队");
				lotteryClassifyForOpenPrize.setScore("2:0");
				lotteryClassifyForOpenPrize.setVisitingTeam("竞彩篮球客队");
				lotteryClassifyForOpenPrize.setClassifyStatus(1);// 1代表是竞彩类
				lotteryClassifyForOpenPrize.setBallColor(0);// 代表篮球的颜色
			} else if (s.getLotteryName().equals("竞彩足球")) {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setHomeTeam("竞彩足球主队");
				lotteryClassifyForOpenPrize.setScore("2:5");
				lotteryClassifyForOpenPrize.setVisitingTeam("竞彩足球客队");
				lotteryClassifyForOpenPrize.setClassifyStatus(1);// 1代表是竞彩类别
				lotteryClassifyForOpenPrize.setBallColor(1);// 代表足球的颜色
			} else if (s.getLotteryName().equals("广东11选5")) {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
				List<String> listRed = new ArrayList<>();
				listRed.add("01");
				listRed.add("03");
				listRed.add("05");
				listRed.add("08");
				listRed.add("15");
				lotteryClassifyForOpenPrize.setRedBall(listRed);
			} else if (s.getLotteryName().equals("双色球")) {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
				List<String> listRed = new ArrayList<>();
				listRed.add("02");
				listRed.add("04");
				listRed.add("05");
				listRed.add("09");
				listRed.add("11");
				listRed.add("18");
				lotteryClassifyForOpenPrize.setRedBall(listRed);
				List<String> listBlue = new ArrayList<>();
				listBlue.add("08");
				lotteryClassifyForOpenPrize.setBlueBall(listBlue);
			} else if (s.getLotteryName().equals("快3")) {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
				List<String> listRed = new ArrayList<>();
				listRed.add("05");
				listRed.add("09");
				listRed.add("07");
				lotteryClassifyForOpenPrize.setRedBall(listRed);
			} else if (s.getLotteryName().equals("更多彩种")) {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
				List<String> listRed = new ArrayList<>();
				listRed.add("3");
				listRed.add("0");
				listRed.add("3");
				listRed.add("3");
				listRed.add("0");
				listRed.add("0");
				listRed.add("3");
				listRed.add("3");
				listRed.add("0");
				listRed.add("3");
				listRed.add("0");
				listRed.add("0");
				listRed.add("3");
				listRed.add("0");
				lotteryClassifyForOpenPrize.setRedBall(listRed);
			} else if (s.getLotteryName().equals("大乐透")) {
				DlSuperLotto superLotto = dlSuperLottoService.getLastNumLottos(1);
				if (null != superLotto) {
					lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别

					try {
						String str = dayForWeek(superLotto.getPrizeDate());
						lotteryClassifyForOpenPrize.setDate(superLotto.getPrizeDate() + "(" + str + ")");
					} catch (Exception e) {
						logger.info("日期转星期几转换异常====================");
						e.printStackTrace();
					}
					List<String> listRed = new ArrayList<>();
					String str = superLotto.getPrizeNum();
					String[] strArray = str.split(",");
					listRed = Arrays.asList(Arrays.copyOfRange(strArray, 0, 5));
					lotteryClassifyForOpenPrize.setRedBall(listRed);
					List<String> listBlue = new ArrayList<>();
					listBlue = Arrays.asList(Arrays.copyOfRange(strArray, 5, 7));
					lotteryClassifyForOpenPrize.setBlueBall(listBlue);
				}
			} else {
				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
				List<String> listRed = new ArrayList<>();
				listRed.add("06");
				listRed.add("04");
				listRed.add("05");
				listRed.add("09");
				listRed.add("23");
				listRed.add("11");
				lotteryClassifyForOpenPrize.setRedBall(listRed);
				List<String> listBlue = new ArrayList<>();
				listBlue.add("18");
				listBlue.add("28");
				lotteryClassifyForOpenPrize.setBlueBall(listBlue);
			}
			lotteryClassifyList.add(lotteryClassifyForOpenPrize);
		}
		return ResultGenerator.genSuccessResult(null, lotteryClassifyList);
	}

	@ApiOperation(value = "活动中心", notes = "活动中心")
	@PostMapping("/activeCenter")
	public BaseResult<ActiveCenterDTO> activeCenter(@RequestBody EmptyParam emprt) {
		ActiveCenterDTO activeCenter = new ActiveCenterDTO();
		List<LotteryNavBanner> lotteryNavBannerList = lotteryNavBannerMapper.selectAll();
		List<LotteryNavBanner> activeList = lotteryNavBannerList.stream().filter(s -> s.getBannerParam().equals("3")).collect(Collectors.toList());
		Integer time = DateUtil.getCurrentTimeLong();
		List<LotteryNavBanner> onlineActiveList = activeList.stream().filter(s -> s.getStartTime() <= time && s.getEndTime() > time).collect(Collectors.toList());
		List<DlBannerForActive> onlineBannerList = new ArrayList<DlBannerForActive>();
		List<LotteryNavBanner> offlineActiveList = activeList.stream().filter(s -> s.getEndTime() < time).collect(Collectors.toList());
		List<DlBannerForActive> offlineBannerList = new ArrayList<DlBannerForActive>();
		for (int i = 0; i < onlineActiveList.size(); i++) {
			DlBannerForActive navBanner = new DlBannerForActive();
			navBanner.setBannerImage(lotteryConfig.getBannerShowUrl() + onlineActiveList.get(i).getBannerImage());
			navBanner.setBannerLink(onlineActiveList.get(i).getBannerLink());
			navBanner.setBannerName(onlineActiveList.get(i).getBannerName());
			onlineBannerList.add(navBanner);
		}
		activeCenter.setOnlineList(onlineBannerList);
		for (int i = 0; i < offlineActiveList.size(); i++) {
			DlBannerForActive navBanner = new DlBannerForActive();
			navBanner.setBannerImage(lotteryConfig.getBannerShowUrl() + offlineActiveList.get(i).getBannerImage());
			navBanner.setBannerLink(offlineActiveList.get(i).getBannerLink());
			navBanner.setBannerName(offlineActiveList.get(i).getBannerName());
			offlineBannerList.add(navBanner);
		}
		activeCenter.setOfflineList(offlineBannerList);
		return ResultGenerator.genSuccessResult(null, activeCenter);
	}

	@ApiOperation(value = "大乐透列表", notes = "大乐透列表")
	@PostMapping("/lottoList")
	public BaseResult<PageInfo<DlSuperLottoDTO>> lottoList(@RequestBody DiscoveryPageParam param) {
		Integer page = param.getPage();
		page = null == page ? 1 : page;
		Integer size = param.getSize();
		size = null == size ? 20 : size;
		PageHelper.startPage(page, size);
		Condition condition = new Condition(DlSuperLotto.class);
		condition.setOrderByClause("term_num desc");
		List<DlSuperLotto> superLottoList = dlSuperLottoService.findByCondition(condition);
		List<DlSuperLottoDTO> LottoDTOList = new ArrayList<DlSuperLottoDTO>();
		for (int i = 0; i < superLottoList.size(); i++) {
			DlSuperLottoDTO superLottoDTO = new DlSuperLottoDTO();
			String dateStr = superLottoList.get(i).getPrizeDate();
			String period = dateStr.replaceAll("-", "");
			superLottoDTO.setPeriod(period + "期");
			try {
				String weekStr = dayForWeek(dateStr);
				superLottoDTO.setPrizeDate(dateStr.substring(5) + "(" + weekStr + ")");
			} catch (Exception e) {
				logger.info("日期转星期几转换异常====================");
				e.printStackTrace();
			}
			String str = superLottoList.get(i).getPrizeNum();
			String[] strArray = str.split(",");
			superLottoDTO.setRedPrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 0, 5)));
			superLottoDTO.setBluePrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 5, 7)));
			superLottoDTO.setTermNum(superLottoList.get(i).getTermNum());
			LottoDTOList.add(superLottoDTO);
		}
		PageInfo<DlSuperLottoDTO> DlSuperLottoPageList = new PageInfo<DlSuperLottoDTO>(LottoDTOList);
		return ResultGenerator.genSuccessResult(null, DlSuperLottoPageList);
	}

	@ApiOperation(value = "大乐透详情", notes = "大乐透详情")
	@PostMapping("/lottoDetails")
	public BaseResult<PageInfo<DlSuperLottoDTO>> lottoDetails(@RequestBody LottoDetailsParam param) {
		DlSuperLotto superLotto = dlSuperLottoService.findById(param.getTermNum());
		// List<> =dlSuperLottoService.findByTermNum(superLotto.getTermNum());
		return ResultGenerator.genSuccessResult(null, null);
	}

	@ApiOperation(value = "联赛列表", notes = "联赛列表")
	@PostMapping("/leagueList")
	public BaseResult<List<DlLeaguePageDTO>> leagueList(@RequestBody EmptyParam emprt) {
		List<DlLeaguePageDTO> leaguePageList = new ArrayList<DlLeaguePageDTO>();
		// 获取洲际分组
		List<DlLeagueGroup> leagueGroupList = dlLeagueGroupService.findAll();
		// 获取国家信息
		List<DlLeagueContry> leagueContryList = dlLeagueContryService.findAll();
		// 获取联赛信息
		List<DlLeagueInfo> leagueInfoList = dlLeagueInfoService.findAll();
		// 获取热门联赛信息
		List<DlLeagueInfo> leagueInfoHotList = leagueInfoList.stream().filter(s -> s.getIsHot() == 1).collect(Collectors.toList());
		// 转换成DTO
		List<LeagueInfoDTO> leagueInfoDTOList = new ArrayList<LeagueInfoDTO>();
		for (int i = 0; i < leagueInfoHotList.size(); i++) {
			LeagueInfoDTO leagueInfoDTO = new LeagueInfoDTO();
			leagueInfoDTO.setLeagueAddr(leagueInfoHotList.get(i).getLeagueAddr());
			leagueInfoDTO.setLeaguePic(leagueInfoHotList.get(i).getLeaguePic());
			leagueInfoDTO.setLeagueId(leagueInfoHotList.get(i).getLeagueId());
			leagueInfoDTO.setLeagueName(leagueInfoHotList.get(i).getLeagueName());
			leagueInfoDTOList.add(leagueInfoDTO);
		}
		for (int i = 0; i < leagueGroupList.size(); i++) {
			DlLeaguePageDTO leaguePage = new DlLeaguePageDTO();
			leaguePage.setGroupName(leagueGroupList.get(i).getGroupName());
			if (leagueGroupList.get(i).getGroupName().equals("热门")) {
				leaguePage.setLeagueInfoList(leagueInfoDTOList);
				leaguePage.setGroupStatus(0);
			} else {
				List<DlLeagueContryDTO> leagueContryDTOList = new ArrayList<>();
				for (int j = 0; j < leagueContryList.size(); j++) {
					if (leagueGroupList.get(i).getId().equals(leagueContryList.get(j).getGroupId())) {
						DlLeagueContryDTO leagueContryDTO = new DlLeagueContryDTO();
						leagueContryDTO.setContryName(leagueContryList.get(j).getContryName());
						leagueContryDTO.setContryPic(leagueContryList.get(j).getContryPic());
						leagueContryDTO.setGroupId(leagueContryList.get(j).getGroupId());
						leagueContryDTO.setId(leagueContryList.get(j).getId());
						leagueContryDTOList.add(leagueContryDTO);
					}
				}
				leaguePage.setGroupStatus(1);
				leaguePage.setLeagueContryList(leagueContryDTOList);
			}
			leaguePageList.add(leaguePage);
		}
		return ResultGenerator.genSuccessResult(null, leaguePageList);
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static String dayForWeek(String pTime) throws Exception {
		// 注意参数的样式为yyyy-MM-dd,必须让参数样式与所需样式统一
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return weekDays[dayForWeek];
	}
}
