package com.dl.shop.lottery.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.logic.SfcLogic;

/**
 * 彩票工具类
 */
public class LotteryUtil {
	
	/**计算注数 */
    public static int getCount(List<HashMap<String, Vector<String>>> selectBallCopy) {
    	int combCounts = 0;
    	int selectNum = SfcLogic.getSFCSelects(selectBallCopy);
    	if (selectNum >= ProjectConstant.SFC14_MIN) {
            combCounts = SfcLogic.getSFCNormal(selectBallCopy);
        }
        return combCounts;
    }
    
    /**给Vector按3 1 0排序*/
    public static void sortVector(Vector<String> src, Vector<String> obj){
    	if(!src.isEmpty()){
    		int len = src.size();
    		for (int i = 0; i < len; i++) {
    			obj.add(new String(src.get(i)));
    		}
    		if(len == 1) return;
    		String tmp = null;
    		for (int i = 0; i < len-1; i++) {
				for (int j = 1; j < len; j++) {
					if(Integer.parseInt(obj.get(i)) < Integer.parseInt(obj.get(j))){
						tmp = obj.get(i);
						obj.set(i, obj.get(j));
						obj.set(j, tmp);
					}
				}
			}
    	}
    }
}
