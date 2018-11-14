package com.dl.shop.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;

public class DyArtifiPrintImple extends BaseDao implements DyArtifiPrintDao{

	public DyArtifiPrintImple(DataBaseCfg cfg) {
		super(cfg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int isDyQueueExist(String userId) {
		// TODO Auto-generated method stub
		int cnt = 0;
		String tableName = getTNameByLgCode(userId, TABLE_NAME);
		String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name = " + "'" + tableName + "'";
		ResultSet rs = this.exeQuery(sql, null);
		if(rs != null) {
			try {
				if(rs.next()) {
					cnt++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				this.closeConn(rs, null);
			}
		}
		return cnt;
	}

	@Override
	public int createDyQueue(String userId) {
		// TODO Auto-generated method stub
		int cnt = 0;
		String tableName = getTNameByLgCode(userId,TABLE_NAME);
		String sql = "CREATE TABLE if not exists "+tableName+ "(_id bigint NOT NULL AUTO_INCREMENT," + 
				" order_sn VARCHAR(50),"+
				" PRIMARY KEY(_id));";
		try {
			 exeUpdate(sql, null);
			 cnt = 1;
		}catch(Exception ee) {
			ee.printStackTrace();
		}
		return cnt;
	}

	@Override
	public List<DDyArtifiPrintEntity> listAll(String userId,long start){
		// TODO Auto-generated method stub
		List<DDyArtifiPrintEntity> rList = new ArrayList<DDyArtifiPrintEntity>();
		String tableName = getTNameByLgCode(userId, TABLE_NAME);
		String sql = "select * from " + tableName + " where _id > " + start + ";";
		ResultSet rs = this.exeQuery(sql, null);
		if(rs != null) {
			try {
				while(rs.next()) {
					DDyArtifiPrintEntity entity = getEntityByRs(rs);
					rList.add(entity);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				this.closeConn(rs, null);
			}
		}
		return rList;
	}

	private DDyArtifiPrintEntity getEntityByRs(ResultSet rs) {
		DDyArtifiPrintEntity entity = new DDyArtifiPrintEntity();
		try {
			long id = rs.getLong("_id");
			String ticketId = rs.getString("order_sn");
			entity.id = id;
			entity.orderSn = ticketId;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	@Override
	public int clearAll(String userId) {
		// TODO Auto-generated method stub
		String tableName = getTNameByLgCode(userId, TABLE_NAME);
		String sql = "delete from " + tableName+";";
		return this.exeUpdate(sql,null);
	}

	@Override
	public int addDyArtifiPrintInfo(String userId, DDyArtifiPrintEntity entity) {
		// TODO Auto-generated method stub
		String tableName = getTNameByLgCode(userId,TABLE_NAME);
		String sql = "insert into " + tableName + " values(0,?)";
		String[] params = {entity.orderSn};
		return this.exeUpdate(sql, params);
	}

	@Override
	public int dropTable(String userId) {
		// TODO Auto-generated method stub
		String tableName = getTNameByLgCode(userId, TABLE_NAME);
		String sql = "drop table " + tableName + ";";
		return this.exeUpdate(sql,null);
	}

	@Override
	public int delData(String uid, String orderSn) {
		// TODO Auto-generated method stub
		String tableName = getTNameByLgCode(uid,TABLE_NAME);
		String sql = "delete from " + tableName + " where order_sn=?;";
		String[] params = {orderSn};
		return exeUpdate(sql, params);
	}

	@Override
	public DDyArtifiPrintEntity queryEntityByOrderSn(String userId, String orderSn) {
		// TODO Auto-generated method stub
		DDyArtifiPrintEntity mEntity = null;
		String tableName = getTNameByLgCode(userId,TABLE_NAME);
		String sql = "select * from " + tableName + " where order_sn=?;";
		String[] params = {orderSn};
		ResultSet rs = this.exeQuery(sql, params);
		if(rs != null) {
			try {
				if(rs.next()) {
					mEntity = getEntityByRs(rs);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				this.closeConn(rs,null);
			}
		}
		return mEntity;
	}

	@Override
	public int deleteOrderSn(String userId, String orderSn) {
		// TODO Auto-generated method stub
		String tableName = getTNameByLgCode(userId,TABLE_NAME);
		String sql = "delete from " + tableName + " where order_sn = ?;";
		String[] params = {orderSn};
		return this.exeUpdate(sql, params);
	}

}
