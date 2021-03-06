package com.dl.shop.lottery.service;

import com.dl.base.enums.LotteryClassifyEnum;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.util.DateUtil;
import com.dl.base.util.JSONHelper;
import com.dl.base.util.PinyinUtil;
import com.dl.base.util.SessionUtil;
import com.dl.lottery.dto.*;
import com.dl.lottery.dto.DlHallDTO.DlNavBannerDTO;
import com.dl.lottery.dto.DlMatchInfoFutureDTO.MatchInfoFutureDTO;
import com.dl.lottery.dto.DlRecentRecordDTO.RecentRecordInfoDTO;
import com.dl.lottery.enums.DiscoveryClassifyEnums;
import com.dl.lottery.enums.LottoRewardLevelEnums;
import com.dl.lottery.param.*;
import com.dl.member.api.ISwitchConfigService;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.param.StrParam;
import com.dl.shop.lottery.configurer.LotteryConfig;
import com.dl.shop.lottery.dao.DlArticleMapper;
import com.dl.shop.lottery.dao.LotteryNavBannerMapper;
import com.dl.shop.lottery.dao2.DlLeagueInfoMapper;
import com.dl.shop.lottery.dao2.DlMatchBasketballMapper;
import com.dl.shop.lottery.dao2.LotteryMatchMapper;
import com.dl.shop.lottery.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
	private DlArticleMapper dlArticleMapper;

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

	@Resource
	private LotteryMatchService lotteryMatchService;

	@Resource
	private LotteryMatchMapper lotteryMatchMapper;

	@Resource
	private DlTeamResult500WService dlTeamResult500WService;

	@Resource
	private DlLeaguePlayerService dlLeaguePlayerService;

	@Resource
	private DlTeamFuture500WService dlTeamFuture500WService;

	@Resource
	private DlTeamRecord500WService dlTeamRecord500WService;

	@Resource
	private DlSeasonGroupData500WService dlSeasonGroupData500WService;

	@Resource
	private LotteryNavBannerMapper lotteryNavBannerMapper;
	
	@Resource
	private	ISwitchConfigService iSwitchConfigService;

	@Resource
	private DlLeagueInfoMapper dlLeagueInfoMapper;
	
	@Resource
	private	DlMatchBasketballMapper dlMatchBasketballMapper;

	public DlDiscoveryPageDTO getHomePage() {
		Condition condition = new Condition(DlDiscoveryHallClassify.class);
		condition.setOrderByClause("sort asc");
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("is_show =", 1);
		List<DlDiscoveryHallClassify> discoveryHallClassifyList = dlDiscoveryHallClassifyService.findByCondition(condition);
		List<DlDiscoveryHallClassifyDTO> discoveryHallClassifyDTOList = new ArrayList<>(discoveryHallClassifyList.size());
		for (DlDiscoveryHallClassify s : discoveryHallClassifyList) {
			DlDiscoveryHallClassifyDTO dto = new DlDiscoveryHallClassifyDTO();
			dto.setClassifyId(String.valueOf(s.getClassifyId()));
			dto.setClassImg(lotteryConfig.getBannerShowUrl() + s.getClassImg());
			dto.setClassName(s.getClassName());
			dto.setRedirectUrl(s.getRedirectUrl());
			dto.setStatus(s.getStatus());
			dto.setStatusReason(s.getStatusReason());
			discoveryHallClassifyDTOList.add(dto);
		}
		DlDiscoveryPageDTO discoveryPage = new DlDiscoveryPageDTO();

		List<DlDiscoveryHallClassifyDTO> discoveryClassifyDTOs =  this.filterDiscoveryClassifyByDealVersion(discoveryHallClassifyDTOList);

		discoveryPage.setDiscoveryHallClassifyList(discoveryClassifyDTOs);
		List<DlLeagueInfo500W> hotLeagues = dlLeagueInfoMapper.getHotLeagues();
		List<LeagueInfoDTO> leagueInfos = new ArrayList<LeagueInfoDTO>(hotLeagues.size());
		for (int i = 0; i < hotLeagues.size(); i++) {
			DlLeagueInfo500W league = hotLeagues.get(i);
			LeagueInfoDTO dto = new LeagueInfoDTO();
			dto.setLeagueAddr(league.getLeagueAbbr());
			dto.setLeagueId(league.getLeagueId());
			dto.setLeagueName(league.getLeagueName());
			dto.setLeaguePic(league.getLeaguePic());
			if (null != league.getLeagueAbbr()) {
				dto.setLeagueInitials(PinyinUtil.ToPinyin(league.getLeagueAbbr()));
			}
			leagueInfos.add(dto);
			if (i >= 8) {
				break;
			}
		}

		// 设置热门联赛
		discoveryPage.setHotLeagueList(leagueInfos);

		// List<DlTopScorerDTO> topScorerList = new
		// ArrayList<DlTopScorerDTO>(5);
		// for (int i = 0; i < 5; i++) {
		// DlTopScorerDTO topScorer = new DlTopScorerDTO();
		// List<DlTopScorerMemberDTO> topScorerMemberList = new
		// ArrayList<DlTopScorerMemberDTO>(5);
		// for (int j = 0; j < 5; j++) {
		// DlTopScorerMemberDTO topScorerMember = new DlTopScorerMemberDTO();
		// topScorerMember.setMemberName(i + "成员" + j);
		// topScorerMember.setRanking(j + 1);
		// topScorerMember.setTopScorerTeam(i + "球队" + j);
		// topScorerMember.setTotalGoal(20 - j * 3);
		// topScorerMemberList.add(topScorerMember);
		// }
		// topScorer.setLeagueName("射手联赛" + i);
		// topScorer.setTopScorerMemberList(topScorerMemberList);
		// topScorerList.add(topScorer);
		// }
		List<DlLeagueInfo500W> dlLeagueInfos = dlLeagueInfoService.get5LeagueMatch();
		List<Integer> leaguesList = new ArrayList<Integer>(dlLeagueInfos.size());
		Map<Integer, String> leagueMap = new HashMap<Integer, String>();
		for (int i = 0; i < dlLeagueInfos.size(); i++) {
			leaguesList.add(dlLeagueInfos.get(i).getLeagueId());
			leagueMap.put(dlLeagueInfos.get(i).getLeagueId(), dlLeagueInfos.get(i).getLeagueAbbr());
		}
		List<DlSeason500w> dlSeason500ws = dlSeason500wService.getSeasonBy5LeagueId(leaguesList);
		// 设置射手榜
		List<DlTopScorerDTO> topScorerList = new ArrayList<DlTopScorerDTO>(dlSeason500ws.size());
		for (int i = 0; i < dlSeason500ws.size(); i++) {
			DlTopScorerDTO topScorerDTO = new DlTopScorerDTO();
			topScorerDTO.setLeagueName(leagueMap.get(dlSeason500ws.get(i).getLeagueId()));
			DlLeagueShooterDTO shooterList = dlLeagueShooterService.findBySeasonId(dlSeason500ws.get(i).getSeasonId());
			topScorerDTO.setLeagueShooterInfoList(shooterList.getLeagueShooterInfoList());
			topScorerList.add(topScorerDTO);
		}
		discoveryPage.setTopScorerDTOList(topScorerList);
		return discoveryPage;
	}

	//
	// public PageInfo<DLArticleDTO> discoveryArticle(DiscoveryPageParam param)
	// {
	// List<InfoCatDTO> catList = createCat();
	// PageInfo<DLArticleDTO> rst = new PageInfo<DLArticleDTO>();
	// if (catList.size() == 0) {
	// List<LotteryClassify> classifyList =
	// lotteryClassifyService.selectAllLotteryClasses();
	// Integer[] catarr = new Integer[classifyList.size()];
	// for (int i = 0; i < classifyList.size(); i++) {
	// catarr[i] = classifyList.get(i).getLotteryClassifyId();
	// }
	// rst = dlArticleService.findArticlesByCats(catarr);
	// } else {
	// Integer[] catarr = new Integer[catList.size()];
	// for (int i = 0; i < catList.size(); i++) {
	// catarr[i] = Integer.parseInt(catList.get(i).getCat());
	// }
	// rst = dlArticleService.findArticlesByCats(catarr);
	// }
	//
	// return rst;
	// }

	/**
	 * 资讯版发现页分类只有 "资讯信息","联赛资料"
	 * @param discoveryHallClassifyDTOList
	 * @return
	 */
	public List<DlDiscoveryHallClassifyDTO> filterDiscoveryClassifyByDealVersion(List<DlDiscoveryHallClassifyDTO> discoveryHallClassifyDTOList) {
		Integer turnOn = 0;// 1-交易开，0-交易关，默认关
		StrParam strParam = new StrParam();
		strParam.setStr("");
		BaseResult<SwitchConfigDTO> switchConfigDTORst = iSwitchConfigService.querySwitch(strParam);
		if(switchConfigDTORst.getCode() == 0) {
			SwitchConfigDTO switchDto = switchConfigDTORst.getData();
			turnOn = switchDto.getTurnOn();
		}
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		String platform = userDevice.getPlat();
		if("h5".equals(platform)) {
			turnOn = 1;
		}
		if(0  == turnOn) {//交易版关
			List<DlDiscoveryHallClassifyDTO> list = new ArrayList<>();
			List<String> notDealList = new ArrayList<>();
			notDealList.add(DiscoveryClassifyEnums.Articles.getCode());
			notDealList.add(DiscoveryClassifyEnums.Leagues.getCode());
			list = discoveryHallClassifyDTOList.stream().filter(s->notDealList.contains(s.getClassifyId())).collect(Collectors.toList());
			return list;
		}else {
			return discoveryHallClassifyDTOList;
		}
	}
	
	
	public DLFindListDTO discoveryArticle(@RequestBody CatArticleParam param) {
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

		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		String channel = userDevice.getChannel();
		List<InfoCatDTO> infoCatList = createCat(channel);
		DLFindListDTO findListDTO = new DLFindListDTO();
		String extendCatParam = param.getExtendCat();
		if (param.equals("-1")) {
			extendCatParam = infoCatList.get(0).getCat();
		}
		PageInfo<DLArticleDTO> rst = dlArticleService.findArticles(extendCatParam);
		List<DLArticleDTO> bigNews = this.createBigNewsList(extendCatParam);
		findListDTO.setDlArticlePage(rst);
		findListDTO.setBigNewsList(bigNews);
		findListDTO.setInfoCatList(infoCatList);
		findListDTO.setNavBanners(navBanners);
		return findListDTO;
	}

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

	public List<DlLotteryClassifyForOpenPrizeDTO> openPrize() {
		// 获取彩种相关信息
		List<DlLotteryClassifyForOpenPrizeDTO> lotteryClassifyList = new ArrayList<DlLotteryClassifyForOpenPrizeDTO>();
		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		String appCodeNameStr = userDeviceInfo.getAppCodeName();
		Integer appCodeName = com.alibaba.druid.util.StringUtils.isEmpty(appCodeNameStr)?10:Integer.valueOf(appCodeNameStr);
		List<LotteryClassify> classifyList = lotteryClassifyService.selectAllLotteryClasses(appCodeName);
		for (LotteryClassify s : classifyList) {
			DlLotteryClassifyForOpenPrizeDTO lotteryClassifyForOpenPrize = new DlLotteryClassifyForOpenPrizeDTO();
			if (null != s.getLotteryName()) {
				lotteryClassifyForOpenPrize.setLotteryInitials(PinyinUtil.ToPinyin(s.getLotteryName()));
			}
			if(appCodeName==11) {//生和彩店
				s.setLotteryClassifyId(s.getLotteryClassifyId()/1000); //例如：数据库id是1000  ，  判断中枚举值为1
			}
			lotteryClassifyForOpenPrize.setLotteryId(s.getLotteryClassifyId());
			lotteryClassifyForOpenPrize.setLotteryName(s.getLotteryName());
			lotteryClassifyForOpenPrize.setLotteryIcon(lotteryConfig.getBannerShowUrl() + s.getLotteryImg());
			if (LotteryClassifyEnum.JC_FOOTBALL.getcode() == s.getLotteryClassifyId()) {
				LotteryMatch dlMatch = lotteryMatchMapper.queryLatestMatch();
				String mmdd = DateUtil.getCurrentTimeString(DateUtil.getTimeSomeDate(dlMatch.getMatchTime()).longValue(), DateUtil.hh_mm_sdf);
				String zhouji = DateUtil.getWeekByDateStr(DateUtil.getCurrentTimeString(DateUtil.getTimeSomeDate(dlMatch.getMatchTime()).longValue(), DateUtil.date_sdf));
				lotteryClassifyForOpenPrize.setDate(mmdd + "(" + zhouji + ")");
				lotteryClassifyForOpenPrize.setHomeTeam(dlMatch.getHomeTeamAbbr());
				lotteryClassifyForOpenPrize.setScore(dlMatch.getWhole());
				lotteryClassifyForOpenPrize.setChangci(dlMatch.getChangci());
				lotteryClassifyForOpenPrize.setVisitingTeam(dlMatch.getLeagueAddr());
				lotteryClassifyForOpenPrize.setClassifyStatus(1);// 1代表是竞彩类别
				lotteryClassifyForOpenPrize.setBallColor(1);// 代表足球的颜色
				lotteryClassifyList.add(lotteryClassifyForOpenPrize);
			}  else if (LotteryClassifyEnum.SUPER_LOTTO.getcode() == s.getLotteryClassifyId()) {
				DlSuperLotto superLotto = dlSuperLottoService.getLastNumLottos(1);
				if (null != superLotto) {
					lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
			    	String mmdd = DateUtil.toStringDateByFormat(superLotto.getPrizeDate(),"MM-dd");
					String zhouji = DateUtil.getWeekByDateStr(superLotto.getPrizeDate());
					lotteryClassifyForOpenPrize.setDate(mmdd + "(" + zhouji + ")");
					List<String> listRed = new ArrayList<>();
					String str = superLotto.getPrizeNum();
					String[] strArray = str.split(",");
					listRed = Arrays.asList(Arrays.copyOfRange(strArray, 0, 5));
					lotteryClassifyForOpenPrize.setRedBall(listRed);
					List<String> listBlue = new ArrayList<>();
					listBlue = Arrays.asList(Arrays.copyOfRange(strArray, 5, 7));
					lotteryClassifyForOpenPrize.setBlueBall(listBlue);
					String dateStr = superLotto.getPrizeDate();
					String period = dateStr.replaceAll("-", "");
					lotteryClassifyForOpenPrize.setPeriod(period + "期");
				}
				lotteryClassifyList.add(lotteryClassifyForOpenPrize);
			} else if (LotteryClassifyEnum.JC_BASKETBALL.getcode() == s.getLotteryClassifyId()) {
				List<DlMatchBasketball> lotteryMatchList = dlMatchBasketballMapper.queryLatestMatch();
				DlMatchBasketball basketMatch = lotteryMatchList.get(0);
				String mmdd = DateUtil.getCurrentTimeString(DateUtil.getTimeSomeDate(basketMatch.getMatchTime()).longValue(), DateUtil.hh_mm_sdf);
				String zhouji = DateUtil.getWeekByDateStr(DateUtil.getCurrentTimeString(DateUtil.getTimeSomeDate(basketMatch.getMatchTime()).longValue(), DateUtil.date_sdf));
				lotteryClassifyForOpenPrize.setDate(mmdd + "(" + zhouji + ")");
				lotteryClassifyForOpenPrize.setHomeTeam(basketMatch.getHomeTeamAbbr());
				lotteryClassifyForOpenPrize.setChangci(basketMatch.getChangci());
				lotteryClassifyForOpenPrize.setScore(basketMatch.getWhole());
				lotteryClassifyForOpenPrize.setVisitingTeam(basketMatch.getVisitingTeamAbbr());
				lotteryClassifyForOpenPrize.setClassifyStatus(1);// 1代表是竞彩类
				lotteryClassifyForOpenPrize.setBallColor(0);// 代表篮球的颜色
				lotteryClassifyList.add(lotteryClassifyForOpenPrize);
			} 
//			else if (LotteryClassifyEnum.GD_5IN11.getcode() == s.getLotteryClassifyId()) {
//				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
//				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
//				List<String> listRed = new ArrayList<>();
//				listRed.add("01");
//				listRed.add("03");
//				listRed.add("05");
//				listRed.add("08");
//				listRed.add("15");
//				lotteryClassifyForOpenPrize.setRedBall(listRed);
//			} else if (LotteryClassifyEnum.DOUBLE_BALL.getcode() == s.getLotteryClassifyId()) {
//				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
//				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
//				List<String> listRed = new ArrayList<>();
//				listRed.add("02");
//				listRed.add("04");
//				listRed.add("05");
//				listRed.add("09");
//				listRed.add("11");
//				listRed.add("18");
//				lotteryClassifyForOpenPrize.setRedBall(listRed);
//				List<String> listBlue = new ArrayList<>();
//				listBlue.add("08");
//				lotteryClassifyForOpenPrize.setBlueBall(listBlue);
//			} else if (LotteryClassifyEnum.KUAI3.getcode() == s.getLotteryClassifyId()) {
//				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
//				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
//				List<String> listRed = new ArrayList<>();
//				listRed.add("05");
//				listRed.add("09");
//				listRed.add("07");
//				lotteryClassifyForOpenPrize.setRedBall(listRed);
//			} else if (LotteryClassifyEnum.BJ_SINGLE.getcode() == s.getLotteryClassifyId()) {
//				LotteryMatch dlMatch = lotteryMatchMapper.queryLatestMatch();
//				String yyyyMM = DateUtil.getCurrentTimeString(DateUtil.getTimeSomeDate(dlMatch.getMatchTime()).longValue(), DateUtil.hh_mm_sdf);
//				String zhouji = DateUtil.getWeekByDateStr(DateUtil.getCurrentTimeString(DateUtil.getTimeSomeDate(dlMatch.getMatchTime()).longValue(), DateUtil.date_sdf));
//				lotteryClassifyForOpenPrize.setDate(yyyyMM + "(" + zhouji + ")"); // "08-28(星期二)"
//				lotteryClassifyForOpenPrize.setHomeTeam(dlMatch.getHomeTeamAbbr());
//				lotteryClassifyForOpenPrize.setScore(dlMatch.getWhole());
//				lotteryClassifyForOpenPrize.setVisitingTeam(dlMatch.getLeagueAddr());
//				lotteryClassifyForOpenPrize.setClassifyStatus(1);// 1代表是竞彩类别
//				lotteryClassifyForOpenPrize.setBallColor(1);// 代表足球的颜色
//			} else if (LotteryClassifyEnum.MORE_L.getcode() == s.getLotteryClassifyId()) {
//				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
//				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
//				List<String> listRed = new ArrayList<>();
//				listRed.add("3");
//				listRed.add("0");
//				listRed.add("3");
//				listRed.add("3");
//				listRed.add("0");
//				listRed.add("0");
//				listRed.add("3");
//				listRed.add("3");
//				listRed.add("0");
//				listRed.add("3");
//				listRed.add("0");
//				listRed.add("0");
//				listRed.add("3");
//				listRed.add("0");
//				lotteryClassifyForOpenPrize.setRedBall(listRed);
//			}  else {
//				lotteryClassifyForOpenPrize.setDate("08-28(星期二)");
//				lotteryClassifyForOpenPrize.setClassifyStatus(0);// 0代表是数字彩类别
//				List<String> listRed = new ArrayList<>();
//				listRed.add("06");
//				listRed.add("04");
//				listRed.add("05");
//				listRed.add("09");
//				listRed.add("23");
//				listRed.add("11");
//				lotteryClassifyForOpenPrize.setRedBall(listRed);
//				List<String> listBlue = new ArrayList<>();
//				listBlue.add("18");
//				listBlue.add("28");
//				lotteryClassifyForOpenPrize.setBlueBall(listBlue);
//			}
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
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
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

	public SZCPrizeDTO szcDetailList(DiscoveryPageParam szcParam) {
		SZCPrizeDTO dto = new SZCPrizeDTO();
		PageInfo<DlSZCDTO> p = new PageInfo<DlSZCDTO>();
		if (LotteryClassifyEnum.SUPER_LOTTO.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			p = this.lottoDetailList(szcParam);
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.SUPER_LOTTO.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.SUPER_LOTTO.getMsg());
		} else if (LotteryClassifyEnum.KUAI3.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.KUAI3.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.KUAI3.getMsg());
		} else if (LotteryClassifyEnum.DOUBLE_BALL.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.DOUBLE_BALL.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.DOUBLE_BALL.getMsg());
		} else if (LotteryClassifyEnum.GD_5IN11.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.GD_5IN11.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.GD_5IN11.getMsg());
		}
		dto.setSzcPrizePageInfo(p);
		return dto;
	}

	public PageInfo<DlSZCDTO> lottoDetailList(DiscoveryPageParam param) {
		Condition condition = new Condition(DlSuperLotto.class);
		condition.setOrderByClause("term_num desc");
		List<DlSuperLotto> superLottoList = dlSuperLottoService.findByCondition(condition);
		PageInfo<DlSuperLotto> pageInfo = new PageInfo<DlSuperLotto>(superLottoList);
		List<DlSZCDTO> LottoDTOList = new ArrayList<DlSZCDTO>();
		for (int i = 0; i < superLottoList.size(); i++) {
			DlSZCDTO dto = new DlSZCDTO();
			String dateStr = superLottoList.get(i).getPrizeDate();
			String period = dateStr.replaceAll("-", "");
			dto.setPeriod(period + "期");
			try {
				String weekStr = dayForWeek(dateStr);
				dto.setPrizeDate(dateStr.substring(5) + "(" + weekStr + ")");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String str = superLottoList.get(i).getPrizeNum();
			String[] strArray = str.split(",");
			dto.setRedPrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 0, 5)));
			dto.setBluePrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 5, 7)));
			dto.setTermNum(superLottoList.get(i).getTermNum());
			LottoDTOList.add(dto);
		}

		PageInfo<DlSZCDTO> result = new PageInfo<DlSZCDTO>();
		try {
			BeanUtils.copyProperties(pageInfo, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		result.setList(LottoDTOList);
		return result;
	}

	/**
	 * 数字彩开奖详情
	 * 
	 * @param szcParam
	 * @return
	 */
	public SZCResultDTO szcDetail(SZCQueryParam szcParam) {
		SZCResultDTO szcDTO = new SZCResultDTO();
		if (LotteryClassifyEnum.SUPER_LOTTO.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			szcDTO = this.lottoDetail(szcParam);
			szcDTO.setLotteryClassify(String.valueOf(LotteryClassifyEnum.SUPER_LOTTO.getcode()));
			szcDTO.setLotteryName(LotteryClassifyEnum.SUPER_LOTTO.getMsg());
		} else if (LotteryClassifyEnum.KUAI3.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			szcDTO.setLotteryClassify(String.valueOf(LotteryClassifyEnum.KUAI3.getcode()));
			szcDTO.setLotteryName(LotteryClassifyEnum.KUAI3.getMsg());
		} else if (LotteryClassifyEnum.DOUBLE_BALL.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			szcDTO.setLotteryClassify(String.valueOf(LotteryClassifyEnum.DOUBLE_BALL.getcode()));
			szcDTO.setLotteryName(LotteryClassifyEnum.DOUBLE_BALL.getMsg());
		} else if (LotteryClassifyEnum.GD_5IN11.getcode() == Integer.valueOf(szcParam.getLotteryClassify())) {
			szcDTO.setLotteryClassify(String.valueOf(LotteryClassifyEnum.GD_5IN11.getcode()));
			szcDTO.setLotteryName(LotteryClassifyEnum.GD_5IN11.getMsg());
		}
		return szcDTO;
	}

	/**
	 * 大乐透开奖详情
	 * 
	 * @param param
	 * @return
	 */
	public SZCResultDTO lottoDetail(SZCQueryParam param) {
		DlSuperLotto superLotto = dlSuperLottoService.findById(param.getTermNum());
		List<DlSuperLottoReward> superLottoRewardList = dlSuperLottoService.findByTermNum(superLotto.getTermNum());
		SZCResultDTO szcDTO = new SZCResultDTO();
		szcDTO.setSellAmount(superLotto.getSell().toString());
		String dateStr = superLotto.getPrizeDate();
		String period = dateStr.replaceAll("-", "");
		szcDTO.setPeriod(period + "期");
		String weekStr = dayForWeek(dateStr);
		szcDTO.setPrizeDate(dateStr.substring(5) + "(" + weekStr + ")");
		szcDTO.setPrizes(superLotto.getPrizes());
		String str = superLotto.getPrizeNum();
		String[] strArray = str.split(",");
		szcDTO.setRedPrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 0, 5)));
		szcDTO.setBluePrizeNumList(Arrays.asList(Arrays.copyOfRange(strArray, 5, 7)));

		List<DlSuperLottoRewardDetailsDTO> superLottoRewardDetailsList = new ArrayList<DlSuperLottoRewardDetailsDTO>();
		for (int i = 0; i < superLottoRewardList.size(); i++) {
			DlSuperLottoReward slr = superLottoRewardList.get(i);
			DlSuperLottoRewardDetailsDTO basicDetails = new DlSuperLottoRewardDetailsDTO();
			DlSuperLottoRewardDetailsDTO appendDetails = new DlSuperLottoRewardDetailsDTO();
			basicDetails.setRewardLevel(slr.getRewardLevel());
			basicDetails.setRewardLevelName(LottoRewardLevelEnums.getbasicNameByCode(slr.getRewardLevel()));
			basicDetails.setRewardNum(slr.getRewardNum1());
			basicDetails.setRewardPrice(slr.getRewardPrice1());
			appendDetails.setRewardLevel(slr.getRewardLevel());
			appendDetails.setRewardLevelName(LottoRewardLevelEnums.getappendNameByCode(slr.getRewardLevel()));
			appendDetails.setRewardNum(slr.getRewardNum2());
			appendDetails.setRewardPrice(slr.getRewardPrice2());
			superLottoRewardDetailsList.add(basicDetails);
			superLottoRewardDetailsList.add(appendDetails);
		}
		szcDTO.setSuperLottoRewardDetailsList(superLottoRewardDetailsList);
		return szcDTO;
	}

	public DlSuperLottoDetailsDTO lottoDetails(SZCQueryParam param) {
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
			DlSuperLottoReward slr = superLottoRewardList.get(i);
			DlSuperLottoRewardDetailsDTO basicDetails = new DlSuperLottoRewardDetailsDTO();
			DlSuperLottoRewardDetailsDTO appendDetails = new DlSuperLottoRewardDetailsDTO();
			basicDetails.setRewardLevel(slr.getRewardLevel());
			basicDetails.setRewardLevelName(LottoRewardLevelEnums.getbasicNameByCode(slr.getRewardLevel()));
			basicDetails.setRewardNum(slr.getRewardNum1());
			basicDetails.setRewardPrice(slr.getRewardPrice1());
			appendDetails.setRewardLevel(slr.getRewardLevel());
			appendDetails.setRewardLevelName(LottoRewardLevelEnums.getappendNameByCode(slr.getRewardLevel()));
			appendDetails.setRewardNum(slr.getRewardNum2());
			appendDetails.setRewardPrice(slr.getRewardPrice2());
			superLottoRewardDetailsList.add(basicDetails);
			superLottoRewardDetailsList.add(appendDetails);
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

	/**
	 * 联赛列表页
	 * 
	 * @param param
	 * @return
	 */
	public GroupLeagueDTO leagueHomePageByGroupId(LeagueListByGroupIdParam param) {
		Integer groupId = param.getGroupId();
		if (groupId == null) {
			groupId = 0;
		}
		return dlLeagueInfoService.leagueHomePageByGroupId(groupId);
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
		DlLeagueDetailForDiscoveryDTO leagueDetail = new DlLeagueDetailForDiscoveryDTO();
		DlLeagueInfo500W leagueInfo = dlLeagueInfoService.leagueDetailFrom500w(leagueId);

		if (leagueInfo == null) {
			return null;
		} else {
			leagueDetail.setLeagueAddr(leagueInfo.getLeagueAbbr());
			leagueDetail.setLeagueId(leagueInfo.getLeagueId());
			leagueDetail.setLeagueName(leagueInfo.getLeagueName());
			leagueDetail.setLeaguePic(leagueInfo.getLeaguePic());
			leagueDetail.setLeagueRule(leagueInfo.getLeagueRule());
			leagueDetail.setLeagueType(leagueInfo.getIsLeague());
			Integer seasonId = param.getSeasonId();
			if (seasonId == null || seasonId == 0) {
				seasonId = dlSeason500wService.getlastSeasonByLeagueId(leagueId);
				if (seasonId == null) {
					return null;
				}
			}
			// 赛季
			DlLeagueSeason500wDTO leagueSeason = getSeason(leagueId);
			leagueDetail.setLeagueSeason(leagueSeason);
			// 积分榜
			DlLeagueScoreDTO leagueIntegral = dlMatchTeamScoreService.findByLeagueIdAndSeasonId(seasonId, leagueDetail.getLeagueType(), leagueInfo.getLeagueId());
			leagueDetail.setLeagueScore(leagueIntegral);
			// 射手榜
			DlLeagueShooterDTO leagueShooter = dlLeagueShooterService.findBySeasonId(seasonId);
			leagueDetail.setLeagueShooter(leagueShooter);
			// 赛程
			DlMatchGroupData500WDTO dlMatchGroupData500WDTO = dlSeasonGroupData500WService.findByLeagueIdAndSeasonId(seasonId, leagueInfo.getLeagueId());
			leagueDetail.setMatchGroupData(dlMatchGroupData500WDTO);
			// 球队
			DlLeagueTeamDTO leagueTeam = dlLeagueTeamService.findByLeagueIdFor500W(leagueId);
			leagueDetail.setLeagueTeam(leagueTeam);
		}
		return leagueDetail;
	}

	private DlLeagueSeason500wDTO getSeason(Integer leagueId) {
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
		return leagueSeason;
	}

	/**
	 * 查询竞彩类开奖结果
	 * 
	 * @param jcParam
	 * @return
	 */
	public JCResultDTO queryJCResult(JCQueryParam jcParam) {
		JCResultDTO dto = new JCResultDTO();
		List<LeagueMatchResultDTO> list = new ArrayList<>();
		if (LotteryClassifyEnum.JC_FOOTBALL.getcode() == Integer.valueOf(jcParam.getLotteryClassify())) {
			list = lotteryMatchService.queryJcOpenPrizesByDate(jcParam);
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.JC_FOOTBALL.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.JC_FOOTBALL.getMsg());
		} else if (LotteryClassifyEnum.JC_BASKETBALL.getcode() == Integer.valueOf(jcParam.getLotteryClassify())) {
			list = lotteryMatchService.queryJLJcOpenPrizesByDate(jcParam);
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.JC_BASKETBALL.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.JC_BASKETBALL.getMsg());
		} else if (LotteryClassifyEnum.BJ_SINGLE.getcode() == Integer.valueOf(jcParam.getLotteryClassify())) {
			dto.setLotteryClassify(String.valueOf(LotteryClassifyEnum.BJ_SINGLE.getcode()));
			dto.setLotteryName(LotteryClassifyEnum.BJ_SINGLE.getMsg());
		}

		String dateStr = jcParam.getDateStr();
		if (StringUtil.isEmpty(dateStr)) {
			dateStr = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		}
		String prizeMatchStr = list.size() + "场比赛已开奖";
		dto.setDateStr(dateStr);
		dto.setPrizeMatchStr(prizeMatchStr);
		dto.setList(list);
		return dto;
	}

	public DlTeamDetailForDiscoveryDTO teamDetailForDiscovery(TeamParam param) {
		// 查询球队名称以及logo
		DlTeam500W team500w = dlLeagueTeamService.findTeamByTeamId(param.getTeamId());
		if (null != team500w) {
			DlTeamDetailForDiscoveryDTO teamDetailForDiscovery = new DlTeamDetailForDiscoveryDTO();
			// 查询球队各项信息
			DlTeamResult500W dlTeamResult500W = dlTeamResult500WService.findByTeamId(param.getTeamId());
			teamDetailForDiscovery.setTeamId(team500w.getTeamId());
			teamDetailForDiscovery.setTeamAddr(team500w.getTeamName());
			teamDetailForDiscovery.setTeamPic(team500w.getTeamPic());
			if (null != dlTeamResult500W) {
				teamDetailForDiscovery.setCity(dlTeamResult500W.getCity());
				teamDetailForDiscovery.setContry(dlTeamResult500W.getContry());
				teamDetailForDiscovery.setTeamCapacity(dlTeamResult500W.getTeamCapacity());
				teamDetailForDiscovery.setTeamTime(dlTeamResult500W.getTeamTime());
				teamDetailForDiscovery.setTeamValue(dlTeamResult500W.getTeamValue());
				teamDetailForDiscovery.setCourt(dlTeamResult500W.getCourt());
			}

			// 设置球员信息
			DlPlayerDTO players = this.getTeamInfo(param);
			teamDetailForDiscovery.setPlayerlist(players);
			// 未来赛事
			DlMatchInfoFutureDTO futureMatch = this.getFutureMatch(param);
			teamDetailForDiscovery.setFutureMatch(futureMatch);
			// 近期战绩
			DlRecentRecordDTO recentRecord = this.getTeamRecord(team500w.getTeamId(), team500w.getTeamName());
			teamDetailForDiscovery.setRecentRecord(recentRecord);

			return teamDetailForDiscovery;
		}
		return null;
	}

	// 近期战绩
	private DlRecentRecordDTO getTeamRecord(Integer teamId, String teamName) {
		List<DlTeamRecord500W> recentRecordList = dlTeamRecord500WService.findByTeamId(teamId);
		DlRecentRecordDTO recentRecord = new DlRecentRecordDTO();
		List<RecentRecordInfoDTO> recentRecordDTOList = new ArrayList<RecentRecordInfoDTO>();
		int win = 0, flat = 0, negative = 0;
		for (int i = 0; i < recentRecordList.size(); i++) {
			RecentRecordInfoDTO recentRecordInfoDTO = new RecentRecordInfoDTO();
			DlTeamRecord500W dlTeamRecord500W = recentRecordList.get(i);
			recentRecordInfoDTO.setDate(dlTeamRecord500W.getMatchTime());
			recentRecordInfoDTO.setHTeam(dlTeamRecord500W.getHomeTeam());
			recentRecordInfoDTO.setVTeam(dlTeamRecord500W.getVisitingTeam());
			recentRecordInfoDTO.setMatch(dlTeamRecord500W.getLeagueName());
			String result = dlTeamRecord500W.getResult();
			recentRecordInfoDTO.setStatus(result);
			if ("胜".equals(result)) {
				win++;
			} else if ("负".equals(result)) {
				negative++;
			} else {
				flat++;
			}
			recentRecordInfoDTO.setScore(dlTeamRecord500W.getScore());
			recentRecordDTOList.add(recentRecordInfoDTO);
		}
		recentRecord.setMatchCount(recentRecordList.size());
		recentRecord.setHomeTeam(teamName);
		recentRecord.setWin(win);
		recentRecord.setFlat(flat);
		recentRecord.setNegative(negative);
		recentRecord.setRecentRecordList(recentRecordDTOList);
		return recentRecord;
	}

	// 球队未来赛事
	private DlMatchInfoFutureDTO getFutureMatch(TeamParam param) {
		DlMatchInfoFutureDTO futureMatch = new DlMatchInfoFutureDTO();
		List<DlTeamFuture500W> dlTeamFuture500WList = dlTeamFuture500WService.findByTeamId(param.getTeamId());
		List<MatchInfoFutureDTO> matchInfoFutureList = new ArrayList<MatchInfoFutureDTO>();
		for (int i = 0; i < dlTeamFuture500WList.size(); i++) {
			MatchInfoFutureDTO matchInfoFutureDTO = new MatchInfoFutureDTO();
			String str = dlTeamFuture500WList.get(i).getMatchTime();
			if (null != str) {
				str = str.substring(0, 10);
			}
			matchInfoFutureDTO.setDate(str);
			matchInfoFutureDTO.setHTeam(dlTeamFuture500WList.get(i).getHomeAbbr());
			matchInfoFutureDTO.setMatchName(dlTeamFuture500WList.get(i).getLeagueAbbr());
			matchInfoFutureDTO.setVTeam(dlTeamFuture500WList.get(i).getVisitingAbbr());
			matchInfoFutureList.add(matchInfoFutureDTO);
		}
		futureMatch.setMatchInfoFutureList(matchInfoFutureList);
		return futureMatch;
	}

	// 球员信息获取
	private DlPlayerDTO getTeamInfo(TeamParam param) {
		List<DlLeaguePlayer> dlLeaguePlayerList = dlLeaguePlayerService.findByTeamId(param.getTeamId());
		log.info("method getTeamInfo : param=" + param.getTeamId() + "  players:" + dlLeaguePlayerList.size());
		DlPlayerDTO players = new DlPlayerDTO();
		if (CollectionUtils.isNotEmpty(dlLeaguePlayerList)) {
			List<DlPlayerInfoDTO> playerInfo0List = new ArrayList<DlPlayerInfoDTO>(10);
			List<DlPlayerInfoDTO> playerInfo1List = new ArrayList<DlPlayerInfoDTO>(10);
			List<DlPlayerInfoDTO> playerInfo2List = new ArrayList<DlPlayerInfoDTO>(10);
			List<DlPlayerInfoDTO> playerInfo3List = new ArrayList<DlPlayerInfoDTO>(10);
			for (DlLeaguePlayer leaguePlayer : dlLeaguePlayerList) {
				DlPlayerInfoDTO playerDto = new DlPlayerInfoDTO();
				playerDto.setPlayerName(leaguePlayer.getPlayerName());
				playerDto.setPlayerNo(leaguePlayer.getPlayerNo());
				Integer playerType = leaguePlayer.getPlayerType();
				if (0 == playerType) {
					playerInfo0List.add(playerDto);
				} else if (1 == playerType) {
					playerInfo1List.add(playerDto);
				} else if (2 == playerType) {
					playerInfo2List.add(playerDto);
				} else if (3 == playerType) {
					playerInfo3List.add(playerDto);
				}
			}
			players.getGoalKeepers().setPlayerList(playerInfo0List);
			players.getBackPlayers().setPlayerList(playerInfo1List);
			players.getMidPlayers().setPlayerList(playerInfo2List);
			players.getForwards().setPlayerList(playerInfo3List);
		}
		log.info("method getTeamInfo : param=" + param.getTeamId() + "  rst:" + JSONHelper.bean2json(players));
		return players;
	}

	public List<DlBannerForActive> getNoviceClassroom() {
		List<LotteryNavBanner> lotteryNavBannerList = lotteryNavBannerService.selectAll();
		List<LotteryNavBanner> activeList = lotteryNavBannerList.stream().filter(s -> s.getBannerParam().equals("4")).collect(Collectors.toList());
		List<DlBannerForActive> list = new ArrayList<DlBannerForActive>();
		for (int i = 0; i < activeList.size(); i++) {
			DlBannerForActive bannerForActive = new DlBannerForActive();
			bannerForActive.setBannerImage(lotteryConfig.getBannerShowUrl() + activeList.get(i).getBannerImage());
			bannerForActive.setBannerLink(activeList.get(i).getBannerLink());
			bannerForActive.setBannerName(activeList.get(i).getBannerName());
			list.add(bannerForActive);
		}
		return list;
	}

}
