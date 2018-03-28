package com.dl.shop.lottery.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.MD5Utils;
import com.dl.dto.DlQueryAccountDTO;
import com.dl.dto.DlQueryIssueDTO;
import com.dl.dto.DlQueryIssueDTO.QueryIssue;
import com.dl.dto.DlQueryPrizeFileDTO;
import com.dl.dto.DlQueryStakeDTO;
import com.dl.dto.DlQueryStakeDTO.BackQueryStake;
import com.dl.dto.DlQueryStakeFileDTO;
import com.dl.dto.DlToStakeDTO;
import com.dl.dto.DlToStakeDTO.BackOrderDetail;
import com.dl.param.DlQueryAccountParam;
import com.dl.param.DlQueryIssueParam;
import com.dl.param.DlQueryPrizeFileParam;
import com.dl.param.DlQueryStakeFileParam;
import com.dl.param.DlQueryStakeParam;
import com.dl.param.DlToStakeParam;
import com.dl.shop.lottery.dao.LotteryPrintMapper;
import com.dl.shop.lottery.model.LotteryPrint;

import net.sf.json.JSONObject;

@Service
public class LotteryPrintService extends AbstractService<LotteryPrint> {
	
	@Resource
	private LotteryPrintMapper lotteryPrintMapper;
	
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;  

	@Value("${print.ticket.url}")
	private String printTicketUrl;
	
	@Value("${print.ticket.merchant}")
	private String merchant;
	
	@Value("${print.ticket.merchantPassword}")
	private String merchantPassword;
	
	/**
	 * 投注接口（竞彩足球，game参数都是T51）
	 * @return
	 */
	public DlToStakeDTO toStake(DlToStakeParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/stake");
		JSONObject backJo = JSONObject.fromObject(backStr);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("orders", BackOrderDetail.class);
		DlToStakeDTO dlToStakeDTO = (DlToStakeDTO) JSONObject.toBean(backJo, DlToStakeDTO.class, mapClass); 
		return dlToStakeDTO;
	}
	
	/**
	 * 投注结果查询
	 * @return
	 */
	public DlQueryStakeDTO queryStake(DlQueryStakeParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/stake_query");
		JSONObject backJo = JSONObject.fromObject(backStr);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("orders", BackQueryStake.class);
		DlQueryStakeDTO dlQueryStakeDTO = (DlQueryStakeDTO) JSONObject.toBean(backJo, DlQueryStakeDTO.class, mapClass); 
		return dlQueryStakeDTO;
	}
	
	/**
	 * 期次查询（暂时不支持）
	 * @return
	 */
	public DlQueryIssueDTO queryIssue(DlQueryIssueParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/issue_query");
		JSONObject backJo = JSONObject.fromObject(backStr);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("issue", QueryIssue.class);
		DlQueryIssueDTO dlQueryIssueDTO = (DlQueryIssueDTO) JSONObject.toBean(backJo, DlQueryIssueDTO.class, mapClass); 
		return dlQueryIssueDTO;
	}
	
	/**
	 * 账户余额查询
	 * @return
	 */
	public DlQueryAccountDTO queryAccount(DlQueryAccountParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/account");
		JSONObject backJo = JSONObject.fromObject(backStr);
		DlQueryAccountDTO dlQueryAccountDTO = (DlQueryAccountDTO) JSONObject.toBean(backJo, DlQueryAccountDTO.class); 
		return dlQueryAccountDTO;
	}
	
	/**
	 * 期次投注对账文件查询
	 * @return
	 */
	public DlQueryStakeFileDTO queryStakeFile(DlQueryStakeFileParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/ticket_file");
		JSONObject backJo = JSONObject.fromObject(backStr);
		DlQueryStakeFileDTO dlQueryStakeFileDTO = (DlQueryStakeFileDTO) JSONObject.toBean(backJo, DlQueryStakeFileDTO.class); 
		return dlQueryStakeFileDTO;
	}
	
	/**
	 * 期次中奖文件查询
	 * @return
	 */
	public DlQueryPrizeFileDTO queryPrizeFile(DlQueryPrizeFileParam param) {
		param.setTimestamp(DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
		JSONObject jo = JSONObject.fromObject(param);
		String backStr = getBackDateByJsonData(jo, "/prize_file");
		JSONObject backJo = JSONObject.fromObject(backStr);
		DlQueryPrizeFileDTO dlQueryPrizeFileDTO = (DlQueryPrizeFileDTO) JSONObject.toBean(backJo, DlQueryPrizeFileDTO.class); 
		return dlQueryPrizeFileDTO;
	}
	
	/**
	 * 获取返回信息
	 * @param jo
	 * @return
	 */
	private String getBackDateByJsonData(JSONObject jo, String inter) {
		String authStr = merchant + merchantPassword + jo.toString();
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		String authorization = MD5Utils.MD5(authStr);
		headers.add("Authorization", authorization);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(jo, headers);
        return rest.postForObject(printTicketUrl + inter, requestEntity, String.class);
	}
}
