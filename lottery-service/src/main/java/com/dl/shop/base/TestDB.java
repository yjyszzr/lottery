package com.dl.shop.base;

import com.alibaba.fastjson.JSON;
import com.dl.member.dto.MediaTokenDTO;
import com.dl.shop.base.dao.DyArtifiPrintDao;
import com.dl.shop.base.dao.DyArtifiPrintImple;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;

public class TestDB {

	public TestDB() {
//		DataBaseCfg cfg = new DataBaseCfg();
//		cfg.setDriver("com.mysql.jdbc.Driver");
//		cfg.setUrl("jdbc:mysql://39.106.18.39:3306/cxm_test");
//		cfg.setUserName("caixiaomi");
//		cfg.setUserPass("cxmtest");
//		
//		String userId = "100001";

		
//		DyArtifiPrintDao dyArtiDao = new DyArtifiPrintImple(cfg);
//		int cnt = dyArtiDao.isDyQueueExist(userId);
//		System.out.println("cnt:" + cnt);
//		
//		cnt = dyArtiDao.createDyQueue(userId);
//		System.out.println("create table cnt:" + cnt);
		
//		DDyArtifiPrintEntity dEntity = new DDyArtifiPrintEntity();
//		dEntity.orderSn = "abcdef";
//		int cnt = dyArtiDao.addDyArtifiPrintInfo(userId, dEntity);
//		System.out.println("add DyArtifiPrint cnt:" + cnt);
		

//		List<DDyArtifiPrintEntity> rList = dyArtiDao.listAll(userId);
//		System.out.println("list.size:" + rList.size());
//		int cnt = dyArtiDao.clearAll(userId);
//		System.out.println("clear cnt:" + cnt);
		
//		cnt = dyArtiDao.dropTable(userId);
//		System.out.println("drop table cnt:" + cnt);
		

//		MediaTokenDTO entity = MediaTokenDTO.getEntity(0);
//		String strInfo = JSON.toJSONString(entity);
//		System.out.println(strInfo);
		//
		//{"accKeyId":"LTAIQ2LG8kd1IU1k","accKeySecret":"Fu8InPLKtEgKSXJ6tJztj5BKnaj6DN","bucketName":"szcq-pic","url":"http://oss-cn-beijing.aliyuncs.com"}
		//
	}
	
	
	public static void main(String[] args) {
		new TestDB();
	}
}
