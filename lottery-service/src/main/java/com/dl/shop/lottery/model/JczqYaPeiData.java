package com.dl.shop.lottery.model;

import java.util.List;

/**
 * 亚赔数据解析
 *
 */
public class JczqYaPeiData {
	public List<CompanyData> yapeiDatas;
	
	public static class CompanyData {
		public String companyName;
		public String hostOdds_1;
		public String hostOdds_2;
		public String guestOdds_1;
		public String guestOdds_2;
		public String rangQiu_1;
		public String rangQiu_2;
	}
	
}
