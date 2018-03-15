package com.dl.shop.lottery.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @Description: 竞技彩共用计算逻辑
 */
public class JCBaseLogic {
	
	/**
     * 计算每种过关方式的最小奖金
     * @param iMaxSpVector
     * @param min 
     * @return
     *
     * 2012-10-8下午06:00:12
     */
    public static double getBonusMinValue(Vector<Double> iMinSpVector, int min){
        double minBonusValue = 0;
        
        Double[] spArray = (Double[])iMinSpVector.toArray(new Double[0]);
        if(spArray.length == min){
        	minBonusValue = getBonusMaxValueBean(spArray);
        }else{        	
        	List<Double[]> list = combination(spArray, min);
        	for (int i = 0; i < list.size(); i++) {
        		double tempValueBean = getBonusMaxValueBean(list.get(i));
        		if(minBonusValue > tempValueBean || minBonusValue == 0){
        			minBonusValue = tempValueBean;
        		}
			}
//        	print(list);
        }
        
        return minBonusValue;
    }
	
    /**
     * 计算每种过关方式的最大奖金
     * @param iMaxSpVector
     * @param min 
     * @return
     *
     * 2012-10-8下午06:00:12
     */
    public static double getBonusMaxValue(Vector<Double> iMaxSpVector, int min){
        double maxBonusValue = 0;
        
        Double[] spArray = (Double[])iMaxSpVector.toArray(new Double[0]);
        if(spArray.length == min){
        	maxBonusValue = getBonusMaxValueBean(spArray);
        }else{        	
        	List<Double[]> list = combination(spArray, min);
        	for (int i = 0; i < list.size(); i++) {
        		maxBonusValue += getBonusMaxValueBean(list.get(i));
			}
//        	print(list);
        }
        
        return maxBonusValue;
    }
    
    /** 计算每种组合的内部元素相乘积 */
    private static double getBonusMaxValueBean(Double[] tempBonus){
    	double tempMaxBonusValue = 1;
    	
    	for (int i = 0; i < tempBonus.length; i++) {
    		tempMaxBonusValue *= tempBonus[i];
		}
    	
    	return tempMaxBonusValue;
    }
    
    /**  
     * @param a:组合数组  
     * @param m:生成组合长度   该长度 必须小于a数组的长度
     * @return :所有可能的组合数组列表  
     */
    public static List<Double[]> combination(Double[] a, int m) {
        List<Double[]> list = new ArrayList<Double[]>();
    	if (a.length < m) {
    		return list;
		}
        int n = a.length;
        boolean end = false; // 是否是最后一种组合的标记   
        // 生成辅助数组。首先初始化，将数组前n个元素置1，表示第一个组合为前n个数。   
        int[] tempNum = new int[n];
        for (int i = 0; i < n; i++) {
            if (i < m) {
                tempNum[i] = 1;

            } else {
                tempNum[i] = 0;
            }
        }
        
        list.add(createResult(a, tempNum, m));// 打印第一种默认组合   
        int k = 0;//标记位   
        while (!end) {
            boolean findFirst = false;
            boolean swap = false;
            // 然后从左到右扫描数组元素值的"10"组合，找到第一个"10"组合后将其变为"01"   
            for (int i = 0; i < n; i++) {
                int l = 0;
                if (!findFirst && tempNum[i] == 1) {
                    k = i;
                    findFirst = true;
                }
                if (tempNum[i] == 1 && tempNum[i + 1] == 0) {
                    tempNum[i] = 0;
                    tempNum[i + 1] = 1;
                    swap = true;
                    for (l = 0; l < i - k; l++) { // 同时将其左边的所有"1"全部移动到数组的最左端。   
                        tempNum[l] = tempNum[k + l];
                    }
                    for (l = i - k; l < i; l++) {
                        tempNum[l] = 0;
                    }
                    if (k == i && i + 1 == n - m) {//假如第一个"1"刚刚移动到第n-m+1个位置,则终止整个寻找   
                        end = true;
                    }
                }
                if (swap) {
                    break;
                }
            }
            list.add(createResult(a, tempNum, m));// 添加下一种默认组合   
        }
        
        return list;
    }

    // 根据辅助数组和原始数组生成结果数组   
    private static Double[] createResult(Double[] a, int[] temp, int m) {
        Double[] result = new Double[m];
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if (temp[i] == 1) {
                result[j] = a[i];
                j++;
            }
        }
        return result;
    }
    
    // 打印整组数组   
    public static void print(List<Double[]> list) {
        for (int i = 0; i < list.size(); i++) {
            Double[] temp = (Double[]) list.get(i);
            for (int j = 0; j < temp.length; j++) {
            	System.out.print(temp[j] + " ");
            }
            System.out.println();
        }
    }

	public static ArrayList<String[]> combination(String[] codesStrings, int m) {
		 ArrayList<String[]> list = new ArrayList<String[]>();
	    	if (codesStrings.length < m) {
	    		return list;
			}
	        int n = codesStrings.length;
	        boolean end = false; // 是否是最后一种组合的标记   
	        // 生成辅助数组。首先初始化，将数组前n个元素置1，表示第一个组合为前n个数。   
	        int[] tempNum = new int[n];
	        for (int i = 0; i < n; i++) {
	            if (i < m) {
	                tempNum[i] = 1;

	            } else {
	                tempNum[i] = 0;
	            }
	        }
	        
	        list.add(createResult(codesStrings, tempNum, m));// 打印第一种默认组合   
	        int k = 0;//标记位   
	        while (!end) {
	            boolean findFirst = false;
	            boolean swap = false;
	            // 然后从左到右扫描数组元素值的"10"组合，找到第一个"10"组合后将其变为"01"   
	            for (int i = 0; i < n; i++) {
	                int l = 0;
	                if (!findFirst && tempNum[i] == 1) {
	                    k = i;
	                    findFirst = true;
	                }
	                if (tempNum[i] == 1 && tempNum[i + 1] == 0) {
	                    tempNum[i] = 0;
	                    tempNum[i + 1] = 1;
	                    swap = true;
	                    for (l = 0; l < i - k; l++) { // 同时将其左边的所有"1"全部移动到数组的最左端。   
	                        tempNum[l] = tempNum[k + l];
	                    }
	                    for (l = i - k; l < i; l++) {
	                        tempNum[l] = 0;
	                    }
	                    if (k == i && i + 1 == n - m) {//假如第一个"1"刚刚移动到第n-m+1个位置,则终止整个寻找   
	                        end = true;
	                    }
	                }
	                if (swap) {
	                    break;
	                }
	            }
	            list.add(createResult(codesStrings, tempNum, m));// 添加下一种默认组合   
	        }
	        
	        return list;
	}
	
	// 根据辅助数组和原始数组生成结果数组   
    private static String[] createResult(String[] a, int[] temp, int m) {
        String[] result = new String[m];
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if (temp[i] == 1) {
                result[j] = a[i];
                j++;
            }
        }
        return result;
    }
    
    
}
