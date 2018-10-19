package com.dl.shop.base.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ArtifiLoginManager {
	private static ArtifiLoginManager instance;
	private List<String> mList;
	
	private ArtifiLoginManager() {
		mList = new ArrayList<String>();
	}
	
	public static ArtifiLoginManager getInstance() {
		if(instance == null) {
			instance = new ArtifiLoginManager();
		}
		return instance;
	}
	
	public void onLogin(String uid) {
		mList.add(uid);
	}
	
	public void onLogout(String uid) {
		mList.remove(uid);
	}

	public Vector<String> getCopyList(){
		return new Vector<String>(this.mList);
	}

	public void setList(List<String> mList) {
		this.mList = mList;
	}
}
