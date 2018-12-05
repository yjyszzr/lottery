package com.dl.shop.base.tj;

import java.util.List;

import com.dl.order.param.OrderSnParam;
import com.dl.shop.base.tj.entity.TOrderEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;

public class Tj {

	public Tj() {
		
		test();
	}
	
	private void test() {
		DataBaseCfg cfg = new DataBaseCfg();
		//jdbc:mysql://localhost:3306/jewel
		cfg.setUrl("jdbc:mysql://172.17.0.100:3306/cxm_app");
		cfg.setUserName("cxm_user_admin");
		cfg.setUserPass("mwkQag0MNtF1");
		cfg.setDriver("com.mysql.jdbc.Driver");
		TOrderImple orderImple = new TOrderImple(cfg);
		List<TOrderEntity> list = orderImple.listAll();
		System.out.println("list.size:" + list.size());
		TOrderDetailImple detailImple = new TOrderDetailImple(cfg);
		for(TOrderEntity order : list) {
			String orderSn = order.orderSn;
			int count = detailImple.listByOrderSn(orderSn);
			if(count > 8) {
				System.out.println("order sn:" + orderSn + " 赛场:" + count + " 金额:" + order.moneyPaid.floatValue());
			}
		}
	}
	
//	public static void main(String[] args) {
//		new Tj();
//	}
}
