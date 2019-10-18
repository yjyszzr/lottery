package com.dl.shop.lottery.model;

import java.util.Date;

import lombok.Data;
/**
 * 用来临时记录投注彩票计算过程数据
 * @author 007
 *
 */
@Data
public class LotteryThirdApiLog {

	//接口名称
	 private String apiName;
	 //接口类型
	 private Integer apiType;
	 //接口描述
	 private String apiDesc;
	 //操作时间
	 private Date optionTime;
	 //接口参数
	 private String apiParam;
	 //接口返回
	 private String apiResult;
	 
	 public  LotteryThirdApiLog(){}
	 
	 public  LotteryThirdApiLog(String apiName, Integer apiType, String apiParam, String apiResult){
		 this.apiName = apiName;
		 this.apiType = apiType;
		 this.apiParam = apiParam;
		 this.apiResult = apiResult;
//		 this.optionTime = Date.from(Instant.now());
	 }
}
