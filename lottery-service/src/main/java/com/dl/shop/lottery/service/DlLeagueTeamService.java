package com.dl.shop.lottery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dl.base.service.AbstractService;
import com.dl.base.util.NetWorkUtil;
import com.dl.base.util.PinyinUtil;
import com.dl.lottery.dto.DlLeagueTeamDTO;
import com.dl.lottery.dto.DlLeagueTeamInfoDTO;
import com.dl.shop.lottery.dao2.DlLeagueInfoMapper;
import com.dl.shop.lottery.dao2.DlLeagueTeamMapper;
import com.dl.shop.lottery.model.DlLeagueInfo;
import com.dl.shop.lottery.model.DlLeagueInfoTeamRef;
import com.dl.shop.lottery.model.DlLeagueTeam;
import com.dl.shop.lottery.model.DlTeam500W;

@Service
@Transactional(value = "transactionManager2")
public class DlLeagueTeamService extends AbstractService<DlLeagueTeam> {

	@Resource
	private DlLeagueTeamMapper dlLeagueTeamMapper;
	@Resource
	private DlLeagueInfoMapper dlLeagueInfoMapper;

	public Integer queryTeamIdBySpottyTeamId(Integer spottyTeamId) {
		Integer teamId = dlLeagueTeamMapper.queryTeamId(spottyTeamId);
		return teamId;
	}

	public void refreshMatchTeams(DlLeagueInfo league) {
		// List<DlLeagueInfo> leagueInfos = dlLeagueInfoMapper.selectAll();
		// for(DlLeagueInfo league: leagueInfos) {
		Integer leagueId = league.getLeagueId();
		System.out.println("开始联赛：" + league.getLeagueAddr());
		Set<String> teamIds = this.teamIds(leagueId);
		System.out.println("开始联赛：" + league.getLeagueAddr() + " get temaids size=" + teamIds.size());
		/*
		 * List<DlLeagueTeam> leagueTeams = this.getLeagueTeams(teamIds);
		 * for(DlLeagueTeam team: leagueTeams) { System.out.println("save " +
		 * team.getTeamId()); DlLeagueTeam byTeamId =
		 * dlLeagueTeamMapper.getByTeamId(team.getTeamId()); if(null ==
		 * byTeamId) { super.save(team); } }
		 */
		// this.save(leagueTeams);
		for (String teamId : teamIds) {
			try {
				DlLeagueInfoTeamRef ref = new DlLeagueInfoTeamRef();
				ref.setLeagueFrom(0);
				ref.setLeagueId(leagueId);
				ref.setTeamId(Integer.valueOf(teamId));
				dlLeagueInfoMapper.saveLeagueTeamRef(ref);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// System.out.println("结束联赛："+league.getLeagueAddr() + " temas size="+
		// leagueTeams.size());
		// }
	}

	private List<DlLeagueTeam> getLeagueTeams(Set<String> teamIds) {
		List<DlLeagueTeam> teams = new ArrayList<DlLeagueTeam>(teamIds.size());
		String preurl = "http://i.sporttery.cn/api/fb_match_info/get_team_data/?tid=";
		for (String teamId : teamIds) {
			String reqUrl = preurl + teamId;
			String json = NetWorkUtil.doGet(reqUrl, new HashMap<String, Object>(), "utf-8");
			if (StringUtils.isBlank(json)) {
				continue;
			}
			JSONObject jsonObject = JSONObject.parseObject(json);
			JSONObject resultObj = jsonObject.getJSONObject("result");
			if (null == resultObj) {
				continue;
			}
			DlLeagueTeam team = new DlLeagueTeam();
			team.setLeagueFrom(0);
			team.setSportteryTeamid(resultObj.getInteger("sporttery_teamid"));
			team.setTeamAddr(resultObj.getString("club_abbr_name") == null ? "" : resultObj.getString("club_abbr_name"));
			team.setTeamId(resultObj.getInteger("team_id"));
			team.setTeamName(resultObj.getString("club_name") == null ? "" : resultObj.getString("club_name"));
			team.setTeamPic(resultObj.getString("team_pic") == null ? "" : resultObj.getString("team_pic"));
			team.setTeamType(resultObj.getString("type") == null ? "" : resultObj.getString("type"));
			String string = resultObj.getString("competition_name");
			teams.add(team);
		}
		return teams;
	}

	private Set<String> teamIds(Integer leagueId) {
		Set<String> teamIds = new HashSet<String>(30);
		Document doc = null;
		String hrefUrl = "http://info.sporttery.cn/football/info/fb_team_info.php?tid=";
		try {
			Connection connect = Jsoup.connect("http://info.sporttery.cn/football/history/history_data.php?mid=" + leagueId);
			doc = connect.get();
			Elements elementsByClass = doc.getElementsByClass("integral");
			for (Element elem : elementsByClass) {
				Element elementById = elem.getElementById("jfg");
				if (null != elementById) {
					Elements elementsByTag = elem.getElementsByTag("a");
					for (Element ae : elementsByTag) {
						String attr = ae.attr("href");
						if (StringUtils.isNotBlank("attr") && attr.startsWith(hrefUrl)) {
							String teamIdStr = attr.substring(hrefUrl.length());
							teamIds.add(teamIdStr);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teamIds;
	}

	public DlLeagueTeamDTO findByLeagueId(Integer leagueId) {
		DlLeagueTeamDTO leagueTeamDTO = new DlLeagueTeamDTO();
		List<DlLeagueTeamInfoDTO> leagueTeamInfoDTOList = new ArrayList<DlLeagueTeamInfoDTO>();
		List<DlLeagueTeam> leagueTeamInfoList = dlLeagueTeamMapper.findByLeagueId(leagueId);
		for (int i = 0; i < leagueTeamInfoList.size(); i++) {
			DlLeagueTeamInfoDTO leagueTeamInfoDTO = new DlLeagueTeamInfoDTO();
			leagueTeamInfoDTO.setTeamAddr(leagueTeamInfoList.get(i).getTeamAddr());
			leagueTeamInfoDTO.setTeamPic(leagueTeamInfoList.get(i).getTeamPic());
			leagueTeamInfoDTOList.add(leagueTeamInfoDTO);
		}
		leagueTeamDTO.setLeagueTeamInfoDTOList(leagueTeamInfoDTOList);
		return leagueTeamDTO;
	}

	public DlLeagueTeamDTO findByLeagueIdFor500W(Integer leagueId) {
		DlLeagueTeamDTO leagueTeamDTO = new DlLeagueTeamDTO();
		List<DlLeagueTeamInfoDTO> leagueTeamInfoDTOList = new ArrayList<DlLeagueTeamInfoDTO>();
		List<DlTeam500W> leagueTeamInfoList = dlLeagueTeamMapper.findByLeagueIdFor500W(leagueId);
		for (int i = 0; i < leagueTeamInfoList.size(); i++) {
			DlLeagueTeamInfoDTO leagueTeamInfoDTO = new DlLeagueTeamInfoDTO();
			leagueTeamInfoDTO.setTeamAddr(leagueTeamInfoList.get(i).getTeamName());
			leagueTeamInfoDTO.setTeamPic(leagueTeamInfoList.get(i).getTeamPic());
			if (null != leagueTeamInfoList.get(i).getTeamName()) {
				leagueTeamInfoDTO.setTeamInitials(PinyinUtil.ToPinyin(leagueTeamInfoList.get(i).getTeamName()));
			}
			leagueTeamInfoDTO.setTeamId(leagueTeamInfoList.get(i).getTeamId());
			leagueTeamInfoDTOList.add(leagueTeamInfoDTO);
		}
		leagueTeamDTO.setLeagueTeamInfoDTOList(leagueTeamInfoDTOList);
		return leagueTeamDTO;
	}

	public DlTeam500W findTeamByTeamId(Integer teamId) {
		return dlLeagueTeamMapper.findTeamByTeamId(teamId);
	}
}
