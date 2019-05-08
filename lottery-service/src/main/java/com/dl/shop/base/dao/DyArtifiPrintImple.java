package com.dl.shop.base.dao;

import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
				" status INTEGER," +
				" lottery_classify_id INTEGER," +
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
		String sql = "select * from " + tableName + "a left join dl_order o on a.order_sn = o.order_sn  where o.play_type_detail like '%,%' and _id > " + start + ";";
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
			int status = rs.getInt("status");
			int lotteryClassifyId = rs.getInt("lottery_classify_id");
			String playDetailType = rs.getString("play_detail_type");
			String mixPlayBz = playDetailType.contains(",")?"1":"0";
			entity.id = id;
			entity.orderSn = ticketId;
			entity.status = status;
			entity.lotteryClassifyId = lotteryClassifyId;
			entity.mixPlayBz = mixPlayBz;
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
		String sql = "insert into " + tableName + " values(0,?,?,?)";
		String[] params = {entity.orderSn,0+"",entity.lotteryClassifyId+""};
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

	@Override
	public int updateOrderStatus(String mobile,String orderSn,int status) {
		// TODO Auto-generated method stub
		String tableName = getTNameByLgCode(mobile,TABLE_NAME);
		String sql = "update " + tableName + " set status=? where order_sn = ?;";
		String[] params = {status+"",orderSn};
		return this.exeUpdate(sql, params);
	}

	@Override
	public boolean isOperationAll(String mobile) {
		// TODO Auto-generated method stub
		boolean isAll = true;
		String tableName = getTNameByLgCode(mobile, TABLE_NAME);
		String sql = "select * from "  + tableName + " where status = 0;";
		ResultSet rs = this.exeQuery(sql, null);
		try {
			while(rs.next()) {
				isAll = false;
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.closeConn(rs, null);
		}
		return isAll;
	}
}
