package com.dl.shop.base.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.dl.shop.base.cfg.DataBaseCfg;

public class BaseDao {
	protected final String TAG = getClass().getSimpleName();
	
	private String DRIVER="com.mysql.jdbc.Driver";
	private String URL="jdbc:mysql://localhost:3306/jewel?useUnicode=true&characterEncoding=gbk";
	private String USER_NAME = "";
	private String USER_PWD = "";
	private Connection conn;

	public BaseDao(DataBaseCfg cfg) {
		DRIVER = cfg.getDriver();
		URL = cfg.getUrl() + "?useUnicode=true&characterEncoding=gbk";
		USER_NAME = cfg.getUserName();
		USER_PWD = cfg.getUserPass();
	}
	
	//打开数据库链接
	private boolean getConnect() {
		boolean flag = false;
		if (conn != null)
			return true;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL,USER_NAME,USER_PWD);
			flag = true;
			System.out.println("open database connection->" + conn);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			flag = false;
		}
		return flag;
	}
    
	//关闭数据库链接
	protected void closeConn(ResultSet rs,PreparedStatement ps) {
		try {
			if(rs != null){
				rs.close();
			}
			if(ps != null){
				ps.close();
			}
			conn.close();
			System.out.println("close database connection->" + conn);
			conn = null;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			conn = null;
		}
	}
	
	public ResultSet exeQuery(String sql, Object[] params) {
		getConnect();
		ResultSet rs = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			
		}
		return rs;
	}

	
	public int exeUpdate(String sql, Object[] params) {
		getConnect();
		int cnt = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i+1, params[i]);
				}
			}
			cnt = ps.executeUpdate();
		} catch (Exception ex) {
			System.out.println("sql:" + sql + ";" + ex.getMessage());
			return -1;
		} finally {
			closeConn(null,ps);
		}
		return cnt;
	}
	
	
	protected String getTNameByLgCode(String userId,String TABLE_NAME){
		return TABLE_NAME + "_" + userId;
	}
}
