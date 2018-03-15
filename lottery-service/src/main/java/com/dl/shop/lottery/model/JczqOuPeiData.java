package com.dl.shop.lottery.model;

import java.util.List;

public class JczqOuPeiData {
	public List<CompanyOddsData> ouPeiDatas;
	
	public static class CompanyOddsData {
		public String companyName;
		public List<OddsData> oddsData;
	}
	public static class OddsData {
		public String oddsName;
		public String hostWin;
		public String draw;
		public String hostLose;
	}
	
}
