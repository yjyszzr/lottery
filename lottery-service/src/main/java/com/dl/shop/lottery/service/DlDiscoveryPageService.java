package com.dl.shop.lottery.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.dl.base.util.DateUtil;
import com.dl.lottery.dto.ActiveCenterDTO;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLHotLeagueDTO;
import com.dl.lottery.dto.DlBannerForActive;
import com.dl.lottery.dto.DlDiscoveryHallClassifyDTO;
import com.dl.lottery.dto.DlDiscoveryPageDTO;
import com.dl.lottery.dto.DlLeagueContryDTO;
import com.dl.lottery.dto.DlLeagueDetailDTO;
import com.dl.lottery.dto.DlLeagueDetailForDiscoveryDTO;
import com.dl.lottery.dto.DlLeagueIntegralDTO;
import com.dl.lottery.dto.DlLeagueMatchDTO;
import com.dl.lottery.dto.DlLeagueSeason500wDTO;
import com.dl.lottery.dto.DlLeagueShooterDTO;
import com.dl.lottery.dto.DlLeagueTeamDTO;
import com.dl.lottery.dto.DlLotteryClassifyForOpenPrizeDTO;
import com.dl.lottery.dto.DlSeason500wDTO;
import com.dl.lottery.dto.DlSuperLottoDTO;
import com.dl.lottery.dto.DlSuperLottoDetailsDTO;
import com.dl.lottery.dto.DlSuperLottoRewardDetailsDTO;
import com.dl.lottery.dto.DlTopScorerDTO;
import com.dl.lottery.dto.DlTopScorerMemberDTO;
import com.dl.lottery.dto.InfoCatDTO;
import com.dl.lottery.param.DiscoveryPageParam;
import com.dl.lottery.param.LeagueDetailForDiscoveryParam;
import com.dl.lottery.param.LeagueDetailParam;
import com.dl.lottery.param.LeagueListByGroupIdParam;
import com.dl.lottery.param.LottoDetailsParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.model.DlArticleClassify;
import com.dl.shop.lottery.model.DlDiscoveryHallClassify;
import com.dl.shop.lottery.model.DlPhoneChannel;
import com.dl.shop.lottery.model.DlSeason500w;
import com.dl.shop.lottery.model.DlSorts;
import com.dl.shop.lottery.model.DlSuperLotto;
import com.dl.shop.lottery.model.DlSuperLottoReward;
import com.dl.shop.lottery.model.LotteryClassify;
import com.dl.shop.lottery.model.LotteryNavBanner;
import com.github.pagehelper.PageInfo;

@Service
@Slf4j
public class DlDiscoveryPageService {

	private final static Logger logger = LoggerFactory.getLogger(DlDiscoveryPageService.class);
	@Resource
	private DlDiscoveryHallClassifyService dlDiscoveryHallClassifyService;

	@Resource
	private LotteryConfig lotteryConfig;

	@Resource
	private DlArticleService dlArticleService;

	@Resource
	private LotteryClassifyService lotteryClassifyService;

	@Resource
	private DlSuperLottoService dlSuperLottoService;

	@Resource
	private LotteryNavBannerService lotteryNavBannerService;
	@Resource
	private DlMatchTeamScoreService dlMatchTeamScoreService;

	@Resource
	private DlLeagueInfoService dlLeagueInfoService;

	@Resource
	private DlLeagueShooterService dlLeagueShooterService;

	@Resource
	private DlFutureMatchService dlFutureMatchService;

	@Resource
	private DlLeagueTeamService dlLeagueTeamService;

	@Resource
	private DlSeason500wService dlSeason500wService;

	public DlDiscoveryPageDTO getHomePage() {
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
		return discoveryPage;
	}

	public PageInfo<DLArticleDTO> discoveryArticle(DiscoveryPageParam param) {
		List<InfoCatDTO> catList = createCat();
		PageInfo<DLArticleDTO> rst = new PageInfo<DLArticleDTO>();
		if (catList.size() == 0) {
			List<LotteryClassify> classifyList = lotteryClassifyService.selectAllLotteryClasses();
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

		return rst;
	}

	private List<InfoCatDTO> createCat() {
		List<InfoCatDTO> infoCatList = new ArrayList<InfoCatDTO>();
		// String channel = SessionUtil.getUserDevice().getChannel();
		String channel = "c16010";
		logger.info("channel===============================================" + channel);
		List<DlArticleClassify> articleClassifyCatList = dlArticleService.findArticleClassify();
		if (channel.equals("h5")) {
			for (int i = 0; i < articleClassifyCatList.size(); i++) {
				InfoCatDTO infoCat = new InfoCatDTO();
				infoCat.setCat(articleClassifyCatList.get(i).getId().toString());
				infoCat.setCatName(articleClassifyCatList.get(i).getClassifyName());
				infoCatList.add(infoCat);
			}
			logger.info("H5端的infoCatList===================================" + infoCatList);
		} else {
			List<DlPhoneChannel> phoneChannelList = dlArticleService.findPhoneChannel(channel);
			if (phoneChannelList.size() > 0) {
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
					List<String> sortStr = Arrays.asList(sortsStr.split(","));
					List<DlSorts> sortsList = new ArrayList<DlSorts>(sortStr.size());
					DlSorts sortsArr[] = new DlSorts[sortStr.size()];
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
					logger.info("资讯分类id不等于0时的infoCatList===================================" + infoCatList);
				}
			}
		}
		return infoCatList;
	}

	public List<DlLotteryClassifyForOpenPrizeDTO> openPrize() {
		// 获取彩种相关信息
		List<DlLotteryClassifyForOpenPrizeDTO> lotteryClassifyList = new ArrayList<DlLotteryClassifyForOpenPrizeDTO>();
		List<LotteryClassify> classifyList = lotteryClassifyService.selectAllLotteryClasses();
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
					String weekStr = dayForWeek(superLotto.getPrizeDate());
					lotteryClassifyForOpenPrize.setDate(superLotto.getPrizeDate() + "(" + weekStr + ")");
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
		return lotteryClassifyList;
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
	public static String dayForWeek(String pTime) {
		// 注意参数的样式为yyyy-MM-dd,必须让参数样式与所需样式统一
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			logger.info("日期到星期的转换异常====================");
			e.printStackTrace();
		}
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return weekDays[dayForWeek];
	}

	public ActiveCenterDTO activeCenter() {

		ActiveCenterDTO activeCenter = new ActiveCenterDTO();
		List<LotteryNavBanner> lotteryNavBannerList = lotteryNavBannerService.selectAll();
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
		return activeCenter;
	}

	public PageInfo<DlSuperLottoDTO> lottoList(DiscoveryPageParam param) {
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

				e.printStackTrace();
			}
			String str = superLottoList.get(i).getPrizeNum();
			String[] strArray = str.split(",");
			superLottoDTO.setRedPrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 0, 5)));
			superLottoDTO.setBluePrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 5, 7)));
			superLottoDTO.setTermNum(superLottoList.get(i).getTermNum());
			LottoDTOList.add(superLottoDTO);
		}
		PageInfo<DlSuperLottoDTO> superLottoPageList = new PageInfo<DlSuperLottoDTO>(LottoDTOList);
		return superLottoPageList;
	}

	public DlSuperLottoDetailsDTO lottoDetails(LottoDetailsParam param) {
		DlSuperLotto superLotto = dlSuperLottoService.findById(param.getTermNum());
		List<DlSuperLottoReward> superLottoRewardList = dlSuperLottoService.findByTermNum(superLotto.getTermNum());
		DlSuperLottoDetailsDTO superLottoDetailsDTO = new DlSuperLottoDetailsDTO();
		superLottoDetailsDTO.setSellAmount(superLotto.getSell().toString());
		String dateStr = superLotto.getPrizeDate();
		String period = dateStr.replaceAll("-", "");
		superLottoDetailsDTO.setPeriod(period + "期");
		String weekStr = dayForWeek(dateStr);
		superLottoDetailsDTO.setPrizeDate(dateStr.substring(5) + "(" + weekStr + ")");
		superLottoDetailsDTO.setPrizes(superLotto.getPrizes());
		String str = superLotto.getPrizeNum();
		String[] strArray = str.split(",");
		superLottoDetailsDTO.setRedPrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 0, 5)));
		superLottoDetailsDTO.setBluePrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 5, 7)));

		List<DlSuperLottoRewardDetailsDTO> superLottoRewardDetailsList = new ArrayList<DlSuperLottoRewardDetailsDTO>();
		for (int i = 0; i < superLottoRewardList.size(); i++) {
			DlSuperLottoRewardDetailsDTO superLottoRewardDetails = new DlSuperLottoRewardDetailsDTO();
			superLottoRewardDetails.setRewardLevel(superLottoRewardList.get(i).getRewardLevel());
			superLottoRewardDetails.setRewardNum1(superLottoRewardList.get(i).getRewardNum1());
			superLottoRewardDetails.setRewardNum2(superLottoRewardList.get(i).getRewardNum2());
			superLottoRewardDetails.setRewardPrice1(superLottoRewardList.get(i).getRewardPrice1());
			superLottoRewardDetails.setRewardPrice2(superLottoRewardList.get(i).getRewardPrice2());
			superLottoRewardDetailsList.add(superLottoRewardDetails);
		}
		superLottoDetailsDTO.setSuperLottoRewardDetailsList(superLottoRewardDetailsList);
		return superLottoDetailsDTO;
	}

	/**
	 * 赛事详情 乐得体育过包接口
	 * 
	 * @param param
	 * @return
	 */
	public DlLeagueDetailDTO leagueDetail(LeagueDetailParam param) {
		Integer leagueId = param.getLeagueId();
		if (leagueId == null) {
			return null;
		}
		DlLeagueDetailDTO leagueDetail = dlLeagueInfoService.leagueDetail(leagueId);
		if (leagueDetail == null) {
			return null;
		} else {
			// 积分榜
			DlLeagueIntegralDTO leagueIntegral = dlMatchTeamScoreService.getByleagueId(leagueId);
			leagueDetail.setLeagueScore(leagueIntegral);
			// 射手榜
			DlLeagueShooterDTO leagueShooter = dlLeagueShooterService.findByLeagueId(leagueId);
			leagueDetail.setLeagueShooter(leagueShooter);
			// 赛程
			DlLeagueMatchDTO leagueMatch = dlFutureMatchService.findByLeagueId(leagueId);
			leagueDetail.setLeagueMatch(leagueMatch);
			// 球队
			DlLeagueTeamDTO leagueTeam = dlLeagueTeamService.findByLeagueId(leagueId);
			leagueDetail.setLeagueTeam(leagueTeam);
		}
		return leagueDetail;
	}

	public List<DlLeagueContryDTO> leagueListByGroupId(LeagueListByGroupIdParam param) {
		Integer groupId = param.getGroupId();
		if (groupId == null) {
			groupId = 0;
		}
		List<DlLeagueContryDTO> contryLeagueList = dlLeagueInfoService.contryLeagueListByGroupId(groupId);
		return contryLeagueList;
	}

	public List<DlLeagueContryDTO> leagueHomePageByGroupId(LeagueListByGroupIdParam param) {
		Integer groupId = param.getGroupId();
		if (groupId == null) {
			groupId = 0;
		}
		List<DlLeagueContryDTO> contryLeagueList = dlLeagueInfoService.leagueHomePageByGroupId(groupId);
		return contryLeagueList;
	}

	/**
	 * 赛事详情 正式接口
	 * 
	 * @param param
	 * @return
	 */
	public DlLeagueDetailForDiscoveryDTO leagueDetailForDiscovery(LeagueDetailForDiscoveryParam param) {
		Integer leagueId = param.getLeagueId();
		if (leagueId == null) {
			return null;
		}
		DlLeagueDetailForDiscoveryDTO leagueDetail = dlLeagueInfoService.leagueDetailFrom500w(leagueId);
		if (leagueDetail == null) {
			return null;
		} else {
			Integer seasonId = param.getSeasonId();
			if (seasonId == null || seasonId == 0) {
				seasonId = dlSeason500wService.getlastSeasonByLeagueId(leagueId);
				if (seasonId == null) {
					return null;
				}
			}
			// 赛季
			List<DlSeason500w> seasonList = dlSeason500wService.findSeasonByLeagueId(leagueId);
			DlLeagueSeason500wDTO leagueSeason = new DlLeagueSeason500wDTO();
			List<DlSeason500wDTO> leagueSeasonInfoList = new ArrayList<DlSeason500wDTO>();
			for (int i = 0; i < seasonList.size(); i++) {
				DlSeason500wDTO season500wDTO = new DlSeason500wDTO();
				season500wDTO.setLeagueId(seasonList.get(i).getLeagueId());
				season500wDTO.setMatchSeason(seasonList.get(i).getMatchSeason());
				season500wDTO.setSeasonId(seasonList.get(i).getSeasonId());
				leagueSeasonInfoList.add(season500wDTO);
			}
			leagueSeason.setLeagueSeasonInfoList(leagueSeasonInfoList);
			leagueDetail.setLeagueSeason(leagueSeason);
			// 积分榜
			DlLeagueIntegralDTO leagueIntegral = dlMatchTeamScoreService.getByleagueId(leagueId);
			leagueDetail.setLeagueScore(leagueIntegral);
			// 射手榜
			DlLeagueShooterDTO leagueShooter = dlLeagueShooterService.findByLeagueIdAndSeasonId(seasonId);
			leagueDetail.setLeagueShooter(leagueShooter);
			// 赛程
			DlLeagueMatchDTO leagueMatch = dlFutureMatchService.findByLeagueId(leagueId);
			leagueDetail.setLeagueMatch(leagueMatch);
			// 球队
			DlLeagueTeamDTO leagueTeam = dlLeagueTeamService.findByLeagueId(leagueId);
			leagueDetail.setLeagueTeam(leagueTeam);
		}
		return leagueDetail;
	}

}
