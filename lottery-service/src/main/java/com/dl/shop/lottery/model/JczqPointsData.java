package com.dl.shop.lottery.model;

public class JczqPointsData {
	public PointsData totalPointsData;
	public PointsData hostPointsData;
	public PointsData guestPointsData;
	
	public static class PointsData {
		public String matchCount;
		public String winCount;
		public String loseCount;
		public String drawCount;
		public String goalCount;
		public String fumbleCount;
		public String jingCount;
		public String points;
		public String rank;
	}
}
