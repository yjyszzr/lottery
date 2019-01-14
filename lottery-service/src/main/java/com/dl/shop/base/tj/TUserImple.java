package com.dl.shop.base.tj;

import com.dl.shop.base.dao.BaseDao;
import com.dl.shop.base.tj.entity.TUserEntity;
import com.dl.shop.lottery.configurer.DataBaseCfg;

public class TUserImple extends BaseDao{

	public TUserImple(DataBaseCfg cfg) {
		super(cfg);
		// TODO Auto-generated constructor stub
	}
	
	
	public int updateUserInfo(TUserEntity userEntity) {
		int cnt = 0;
		String sql = "update dl_user set is_super_white = 1 where mobile=?;";
		String[] params = {userEntity.mobile};
		cnt = this.exeUpdate(sql, params);
		return cnt;
	}
}
