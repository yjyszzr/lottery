package com.dl.shop.lottery.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dl.base.util.DateUtil;
import com.dl.base.util.MD5Utils;
import com.dl.lottery.dto.XianDlToStakeDTO;
import com.dl.lottery.dto.XianDlToStakeDTO.XianBackOrderDetail;
import com.google.common.collect.Lists;
@Slf4j
public class XianPrintLottery {
	private String hostName="http://devcapi.bjzhongteng.com";
	private String create="/order/create";
	private String query="/order/query";
	private String accountBalanceUrl="/account/balance";
	private String accountUser="caixiaomi_dev";
	private String accountPwd="Udohdup9shoh0Pee";
	private RestTemplate restTemplate = getRestTemplate();
	public static void main(String[] args){
		XianPrintLottery printLottery = new XianPrintLottery();
		JSONObject jsonParams = new JSONObject();
		printLottery.setCommonParams(jsonParams);
//		printLottery.queryAccountBalance(jsonParams);
		printLottery.printlotteryCreate(jsonParams);
		printLottery.printlotteryQuery(jsonParams);
	}
	public void printlotteryCreate(JSONObject jsonParams){
		log.info("开始出票调用");
		List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();
		Map<String,Object> order1 = new HashMap<String, Object>();
		order1.put("ticketId", "2018062216323623330087");
		order1.put("game", "T51");
		order1.put("issue", "201806226026");
		order1.put("playType", "02");
		order1.put("times", "99");
		order1.put("money", "19800");
		order1.put("betType", "21");
		order1.put("stakes", "02|201806225025|0;02|201806225026|0");
		orderList.add(order1);
		jsonParams.put("orders", orderList);
		String response = send(hostName+create,jsonParams);
		log.info("创建出票信息响应={}",response);
	}
    public void printlotteryQuery(JSONObject jsonParams){
		log.info("查询待出票结果");
		String plantFormId="mjzfXP";
		List<String> orders = new ArrayList<String>();
		orders.add("2018062216323623330082");
		jsonParams.put("orders", orders);
		String response = send(hostName+query,jsonParams);
		log.info("查询待出票响应={}",response);
	}
	public void queryAccountBalance(JSONObject queryAccountBalanceParams){
		String response = send(hostName+accountBalanceUrl,queryAccountBalanceParams);
		log.info("查询账号信息响应={}",response);
	}
	private String send(String sendUrl,JSONObject params){
		String authStr = accountUser + accountPwd + params.toString();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		String authorization = MD5Utils.MD5(authStr);
		log.info("authorization={}",authorization);
		headers.add("Authorization", authorization);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(params, headers);
		String requestUrl = sendUrl;
		String response = restTemplate.postForObject(requestUrl, requestEntity, String.class);
		log.info("请求地址={},header={},参数={},响应={}",sendUrl,headers.toString(),params,new String(response.getBytes()));
		JSONObject backJo = JSONObject.fromObject(response);
		@SuppressWarnings("rawtypes")
		Map<String,Class> mapClass = new HashMap<String,Class>();
		mapClass.put("orders", XianBackOrderDetail.class);
		XianDlToStakeDTO printTestDTO = (XianDlToStakeDTO) JSONObject.toBean(backJo, XianDlToStakeDTO.class, mapClass);
		log.info("orderTicketId={}",printTestDTO.getOrders().get(0).getTicketId());
		log.info("error={}",printTestDTO.getOrders().get(0).getErrMsg()+"="+printTestDTO.getOrders().get(0).getErrorMsg());
		return response;
	}
	private void setCommonParams(JSONObject params){
		params.put("merchant", accountUser);
		params.put("version", "1.0");
		params.put("timestamp", DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong().longValue(), DateUtil.datetimeFormat));
	}
	private static RestTemplate getRestTemplate(){
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
        for (HttpMessageConverter httpMessageConverter : restTemplate.getMessageConverters()) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
                continue;
            }
            messageConverters.add(httpMessageConverter);
        }
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
	}
}
