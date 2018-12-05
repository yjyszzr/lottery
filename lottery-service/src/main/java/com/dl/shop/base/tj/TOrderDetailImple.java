package com.dl.shop.base.tj;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dl.shop.base.dao.BaseDao;
import com.dl.shop.lottery.configurer.DataBaseCfg;

public class TOrderDetailImple extends BaseDao{

	public TOrderDetailImple(DataBaseCfg cfg) {
		super(cfg);
		// TODO Auto-generated constructor stub
	}

	
	public int listByOrderSn(String orderSn) {
		int cnt = 0;
		String sql = "select count(*) from dl_order_detail where order_sn = " + orderSn;
		ResultSet rs = this.exeQuery(sql, null);
		try {
			if(rs.next()) {
				int count = rs.getInt(1);
				cnt = count;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.closeConn(rs, null);
		}
		return cnt;
	}
}
