package com.dl.shop.lottery.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import com.dl.shop.lottery.core.ProjectConstant;

/**
 * 胜负彩计算逻辑
 */
public class SfcLogic {
	public static int getSFCNormal(List<HashMap<String, Vector<String>>> listItem) {
		int result = 1;
		
		int listLen = listItem.size();
		for(int i=0; i< listLen; i++){
			HashMap<String,  Vector<String>> item = listItem.get(i);
			Vector<String> itemResult = item.get(String.valueOf(i));
			int itemResultLen = itemResult.size();
			
			result = result * CommonLogic.combination(itemResultLen,ProjectConstant.SFC14_Item_MIN);
		}

		return result;
	}
	
	/**
	 * hshm 获取投注接口中需要的投注内容
	 * 
	 * @return 选择的足彩内容：3,1,0|1|1|1|1|1|1|1|1|1|1|1|1|3,1,0
	 */
	public static String getSFCContent(ArrayList<HashMap<String, Vector<String>>> listItem,String flag) {
		StringBuffer content = new StringBuffer();

		//组装有所对阵的结果
		int listLen = listItem.size();
		for(int i=0; i< listLen; i++){
			HashMap<String, Vector<String>> item = listItem.get(i);
			Vector<String> itemResult = item.get(String.valueOf(i));
			int itemResultLen = itemResult.size();
			
			//组装某对阵的结果
			StringBuffer itemCon = new StringBuffer();	
			for(int j = 0; j < (itemResultLen - 1); j++){
				itemCon.append(itemResult.elementAt(j));
//				if(!(itemResult.contains("(") &&  (j == 0 || j == (itemResultLen - 2)))){				    
//				    itemCon.append(Config.CONTENTSPLITEFLAG_DouHao);
//				}
			}
			
			if(itemResultLen >= 1){
			    itemCon.append(itemResult.elementAt(itemResultLen - 1));
			}
			
			if(itemResultLen > 1){
				content.append("(");
				content.append(sortResult(itemCon.toString()));
				content.append(")");
			}else {
				content.append(sortResult(itemCon.toString()));
			}
//			content.append(sortResult(itemCon.toString()));
//			if(i < (listLen - 1)){				
//				content.append(flag);
//			}
		}
		
		return content.toString();
	}
	
	/**
	 * 对选择结果进行升序排序
	 * @param iResult
	 * @return
	 * @create_time 2011-9-5 下午2:40:14
	 */
	public static String sortResult(String iResult){
	    StringBuffer sortResult = new StringBuffer();
	    
	    Pattern pattern = Pattern.compile("[,]+");//","分隔字符
	    String[] ballValues = pattern.split(iResult);
	    
	    Arrays.sort(ballValues);
	    int resultLen = ballValues.length;
	    
	    for(int i = 0; i < (resultLen - 1); i++){
	        sortResult.append(ballValues[i]);
	        sortResult.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
	    }
	    
	    if(resultLen >= 1){
	        sortResult.append(ballValues[resultLen - 1]);
	    }
	    
	    return sortResult.toString();
	}
	
	/**
	 * hshm 计算用户选择场数
	 * 
	 * @return 场数
	 */
	public static int getSFCSelects(List<HashMap<String, Vector<String>>> listItem) {
		int counts = 0;

		int listLen = listItem.size();
		for(int i=0; i< listLen; i++){
			HashMap<String, Vector<String>> item = listItem.get(i);
			Vector<String> itemResult = item.get(String.valueOf(i));
			int itemResultLen = itemResult.size();
			
			if(itemResultLen > 0){
				if(itemResultLen >= 2 && itemResult.elementAt(1).equals(")")){
					continue;//若该场选择结果内容为:(),则不计算注数
				}
				counts++;
			}
		}
		
		return counts;
	}
}
