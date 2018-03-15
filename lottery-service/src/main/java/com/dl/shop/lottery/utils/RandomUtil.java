package com.dl.shop.lottery.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 随机工具类
 */
public class RandomUtil {

	/**
	 * 随机生成球
	 * @param allNum 总数
	 * @param chooseNum 选择几个
	 * @param addZero 是否补0
	 * @return 球的文本组合,例如: "1,2,3,4,5,6"
	 */
	public static String randomBall(int allNum, int chooseNum, boolean addZero){
		return randomBall(allNum, chooseNum, addZero, false);
	}
	

	/**
	 * 随机生成球
	 * @author jaybai
	 * @param allNum
	 * @param chooseNum
	 * @param addZero
	 * @param isZeroStar 是否从0开始
	 */
	public static String randomBall(int allNum, int chooseNum, boolean addZero, boolean isZeroStar){
		int[] choose = new int[chooseNum];
		List<Integer> list = new ArrayList<Integer>();
		int ballMin;
		int ballMax;
		if (isZeroStar) {
			ballMin = 0;
			ballMax = allNum - 1;
		}else{
			ballMin = 1;
			ballMax = allNum;
		}

		for (int i = ballMin; i <= ballMax; i++) {
			list.add(i);
		}
		
		for (int i = 0; i < chooseNum; i++) {
			int j = getRandom(0, list.size()-1);
			choose[i] =  list.get(j);			
			list.remove(j);
		}
		Arrays.sort(choose);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < choose.length; i++) {
			if (i == 0) {
				if (addZero && choose[i] < 10) {
					sb.append("0").append(choose[i]);
				}else{
					sb.append(choose[i]);
				}
				
			}else{
				sb.append(",");
				if (addZero && choose[i] < 10) {
					sb.append("0").append(choose[i]);
				}else{
					sb.append(choose[i]);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 产生0～max的随机整数 包括0 和max
	 * 
	 * @param max
	 *            随机数的上限
	 * @return
	 */
	public static int getRandom(int max) {
		return new Random().nextInt(max + 1);
	}
	
	/**
	 * 产生 min～max的随机整数 包括 min 和 max
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min, int max) {
		int r = getRandom(max - min);
		return r + min;
	}
}
