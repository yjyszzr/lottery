package com.dl.shop.base.tj;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dl.shop.base.dao.BaseDao;
import com.dl.shop.base.tj.entity.TOrderEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;

public class TOrderImple extends BaseDao{

	public TOrderImple(DataBaseCfg cfg) {
		super(cfg);
		// TODO Auto-generated constructor stub
	}
	
	public List<TOrderEntity> listAll(){
		List<TOrderEntity> list = new ArrayList<TOrderEntity>();
		String sql = "select order_id,order_sn,money_paid,add_time from dl_order where money_paid > 0 and add_time > 1541520000 and order_status != 8 and order_id > 502405;";
		ResultSet rs = this.exeQuery(sql,null);
		try {
			while(rs != null && rs.next()) {
				long orderId = rs.getLong("order_id");
				String orderSn = rs.getString("order_sn");
				BigDecimal money = rs.getBigDecimal("money_paid");
				long addTime = rs.getLong("add_time");
				TOrderEntity entity = new TOrderEntity();
				entity.orderId = orderId;
				entity.orderSn = orderSn;
				entity.moneyPaid = money;
				entity.addTime = addTime;
				list.add(entity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.closeConn(rs, null);
		}
		return list;
	}
}
