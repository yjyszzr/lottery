package com.dl.shop.lottery.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.dl.shop.lottery.core.ProjectConstant;

public class JCZQLogic {

    /**
     * hshm 获取用于计算注数的方案内容
     * 
     * @return 选择的足彩内容：1,1,1,(10),10,1,1,1,1,1,1,3,1,0
     */
    public static String getSPFContent(ArrayList<Vector<Object>> listItem) {
        StringBuffer content = new StringBuffer();

        int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
            Vector<Object> itemResult = listItem.get(i);
            @SuppressWarnings("unchecked")
			HashMap<String, String> selectMap = (HashMap<String, String>) itemResult.elementAt(2);

            StringBuffer itemCon = new StringBuffer();  
            for (Iterator<String> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
                itemCon.append((String) iterator.next());
            }
            
            //胆码，则添加()
            if(itemResult.contains("(") && itemResult.contains(")")){
                content.append("(");
                content.append(itemCon);
                content.append(")");
            }else{
                content.append(itemCon);
            }
            
            if(i < (listLen - 1)){              
                content.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
            }
        }
        
        return content.toString();
    }
    
    /**
     * 计算注数 
     * @param content = 1,1,1,(10),10,1,1,1,1,1,1,3,1,0
     * @param requestNum 选择的过关最小场数，比如6串1，则requestNum = 6
     * @return
     * @create_time 2011-10-25 上午10:53:33
     */
    public static int getSPFDanTuo(String content, int requestNum) {
        int danCount = 0, tuoCount = 0, zhushu = 0;

        //胆的注数
        int danTotalCount = 1;
        //拖的注数排列
        String tuoString = ""; 
        String[] contents = content.split(ProjectConstant.CONTENTSPLITEFLAG_DouHao);

        for (int i = 0; i < contents.length; i++) {
            String contentStr = contents[i].trim(); //得到每场的结果集

            if (contentStr.indexOf("(") != -1 && contentStr.indexOf(")") != -1) {
                danCount++; //胆码数量++
                danTotalCount = danTotalCount * (contentStr.toCharArray().length - 2);
            } else {
                tuoCount++; //托码数量++
                tuoString = tuoString + contentStr.toCharArray().length + ",";
            } 

        }
        tuoString = tuoString.substring(0, tuoString.lastIndexOf(","));
        zhushu = CommonLogic.combineTotalCount(tuoString.split(","), danTotalCount, tuoCount, requestNum - danCount);

        return zhushu;
    }
    
    /**
     * 组装投注内容
     * @param iSelectBall 数据格式:日期(yymmdd)，对阵编号，{胜平负-SP值}，胆码标识，如：111105,22,{3,3.01),(,)
     * @return 10212001(1-1.2,2-1.8)#110212002(1-1.3)|110212003(1-1.3)
     * @create_time 2011-10-25 下午4:44:46
     */
    public static ArrayList<String> getSPFBetContent(ArrayList<Vector<Object>> iSelectBall) {
    	ArrayList<String> betCon = new ArrayList<String>();
    	
        StringBuffer content = new StringBuffer();
        StringBuffer spContent = new StringBuffer();//SP
        
        StringBuffer tempSpContent = new StringBuffer();//SP
        StringBuffer danCon = new StringBuffer();//胆码内容
        StringBuffer tuoCon = new StringBuffer();//拖码内容
        
        int size = iSelectBall.size();
        for(int i = 0; i < size; i++){
            Vector<Object> itemResult = iSelectBall.get(i);
            int itemResultLen = itemResult.size();
            if(itemResultLen > 0){//拖码
                if(!(itemResult.contains("(") && itemResult.contains(")"))){//拖码
                    tuoCon.append(getSingleSelectCon(itemResult));
                }else{//胆码
                    danCon.append(getSingleSelectCon(itemResult));
                }
            }
            
            tempSpContent.append(getSingleSelectSp(itemResult));
        }
        
        //若有胆码，则按照胆码格式组装：1(3)#22(3)|33(1)|44(1)
        if(danCon.toString().endsWith(ProjectConstant.CONTENTSPLITEFLAG_ShuXian)){
            content.append(danCon.substring(0, (danCon.length() - 1)));
            content.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        }
        
        //有无胆码，拖码都一样
        if(tuoCon.toString().endsWith(ProjectConstant.CONTENTSPLITEFLAG_XieXian)){
            content.append(tuoCon.substring(0, (tuoCon.length() - 1)));
        }
        
        if(tempSpContent.toString().endsWith(ProjectConstant.CONTENTSPLITEFLAG_XieXian)){
        	spContent.append(tempSpContent.substring(0, (tempSpContent.length() - 1)));
        }
        
        betCon.add(content.toString());
        betCon.add(spContent.toString());
        
        return betCon;
    }
    
    /**
     * 组装单场赛事的选择结果投注内容
     * @param iItemResult
     * @return con  F20130822001#周四001#胜胜,胜平,胜负#0/F20130822002#周四002#胜负,平胜#0
     * @create_time 2011-12-1 上午10:40:23
     */
    private static StringBuffer getSingleSelectCon(Vector<Object> iItemResult){
        StringBuffer con = new StringBuffer();
        
        con.append(iItemResult.elementAt(1));//对阵编号
        
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        con.append(iItemResult.elementAt(0));//年月日(周四)
//        con.append("(");
        
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        @SuppressWarnings("unchecked")
		HashMap<String, String> selectMap = (HashMap<String, String>) iItemResult.elementAt(2);
        for (Iterator<String> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {//投注内容：胜,平,负
            String key = (String) iterator.next();
            if(key.equalsIgnoreCase("3")){
            	con.append("胜");
            }else if(key.equalsIgnoreCase("1")){
            	con.append("平");
            }else {
            	con.append("负");
			}
//            con.append(key);
//            con.append("-");
//            con.append(selectMap.get(key));
            
            if(iterator.hasNext()){
                con.append(",");
            }
        }
//        con.append(")");
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        con.append("0");//是否胆拖
        con.append(ProjectConstant.CONTENTSPLITEFLAG_XieXian);
        
        return con;
    }
    
    /**
     * 组装单场赛事的选择Sp值
     * @param iItemResult
     * @return con  F20130822001#胜-1.3,负-2.1
     * @create_time 2011-12-1 上午10:40:23
     */
    private static StringBuffer getSingleSelectSp(Vector<Object> iItemResult){
        StringBuffer con = new StringBuffer();
        
        con.append(iItemResult.elementAt(1));//对阵编号
        
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        
        @SuppressWarnings("unchecked")
		HashMap<String, String> selectMap = (HashMap<String, String>) iItemResult.elementAt(2);
        for (Iterator<String> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {//投注内容：胜,平,负
            String key = (String) iterator.next();
            if(key.equalsIgnoreCase("3")){
            	con.append("胜");
            }else if(key.equalsIgnoreCase("1")){
            	con.append("平");
            }else {
            	con.append("负");
			}
            con.append(ProjectConstant.CONTENTSPLITEFLAG_HenXian);
            con.append(selectMap.get(key));
            
            if(iterator.hasNext()){
                con.append(",");
            }
        }
        con.append(ProjectConstant.CONTENTSPLITEFLAG_XieXian);
        
        return con;
    }
    
    /**
     * 获取计算奖金需要的数据,格式：Vector<Vector<Double>> = {{非胆码SP值},{胆码SP值}}
     * @param iSelectBall 数据格式:日期(yymmdd)，对阵编号，{胜平负-SP值}，胆码标识，如：111105,22,{3,3.01),(,)
     * @return
     * @create_time 2011-12-5 下午2:28:23
     */
    public static Vector<Vector<Double>> getSpBonusVector(ArrayList<Vector<Object>> iSelectBall){
        Vector<Vector<Double>> maxSPVector  = new Vector<Vector<Double>>();
        
        Vector<Double> maxNormalSPVector = new Vector<Double>();//非胆码SP数组
        Vector<Double> maxDanSPVector = new Vector<Double>();//胆码SP数组
        
        int selectLen = iSelectBall.size();
        for (int i = 0; i < selectLen; i++) {
            Vector<Object> itemResult = iSelectBall.get(i);
            @SuppressWarnings("unchecked")
			HashMap<String, String> selectMap = (HashMap<String, String>) itemResult.elementAt(2);
            double maxSpValue = 0;
            for (Iterator<String> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                Double tempMaxSpValue = Double.parseDouble(selectMap.get(key));
                if(tempMaxSpValue > maxSpValue){
                    maxSpValue = tempMaxSpValue;
                }
            }
            if(!(itemResult.contains("(") && itemResult.contains(")"))){//拖码
                maxNormalSPVector.add(maxSpValue);
            }else{//胆码
                maxDanSPVector.add(maxSpValue);
            }
        }
        
        maxSPVector.add(maxNormalSPVector);
        maxSPVector.add(maxDanSPVector);
        
        return maxSPVector;
    }
    
    /**
     * 计算每种过关方式的最大奖金
     * @param iMaxSpVector Vector<Vector<Double>> = {{非胆码SP值},{胆码SP值}}
     * @param min 过关方式
     * @return
     * @create_time 2011-12-5 下午3:23:09
     */
    public static double getBonusMaxValue(Vector<Vector<Double>> iMaxSpVector, int min){
        double maxBonusValue = 0;
        
        Vector<Double> maxNormalSPVector = iMaxSpVector.elementAt(0);//非胆码SP数组
        Vector<Double> maxDanSPVector = iMaxSpVector.elementAt(1);//胆码SP数组
        
        //计算胆码组合的SP值
        double danSPValue = 1;
        int danLen = maxDanSPVector.size();
        for(int i = 0; i < danLen; i++){
            danSPValue = danSPValue *  maxDanSPVector.elementAt(i);
        }
        
        //非胆码个数(min - danLen)循环相乘  (范围: 2 ~ maxNormalSPVector.size)
        if((min - danLen) > 1){
            
            //两个元素相乘组合
            List<Object[]> result2 = vectorMultiply(maxNormalSPVector, null);
            List<List<Object[]>> resultList = new ArrayList<List<Object[]>>();
            resultList.add(result2);
            
            //三个以上元素相乘组合
            if((min - danLen) > 2){
                int count = min - 2 - danLen;
                List<Object[]> srcList = null, resultTmp = null;
                for (int i = 0; i < count; i++) {
                    srcList = resultList.get(i);
                    resultTmp = vectorMultiply(maxNormalSPVector, srcList);
                    resultList.add(resultTmp);
                }
            }
            
            List<Double> allList = new ArrayList<Double>();
            List<Object[]> tmpList = resultList.get(resultList.size()-1);          
            for (Iterator<Object[]> iterator = tmpList.iterator(); iterator.hasNext();) {
                Object[] obj = (Object[]) iterator.next();
                allList.add((Double)obj[1]);
                
                maxBonusValue += danSPValue * (Double)obj[1];//将各种组合的计算所得的SP累加
            }
        }else{
            int normalLen = maxNormalSPVector.size();
            for (int i = 0; i < normalLen; i++) {
                
                maxBonusValue += danSPValue * maxNormalSPVector.elementAt(i);//将各种组合的计算所得的SP累加
            }
        }
        
        return maxBonusValue;
    }
    
    public static String getDoubleStr(Vector<Double> doubles, String key){
        String str = "";
        String[] strs = key.split("_");
        for (int i = 0; i < strs.length; i++) {
            str += "" + doubles.elementAt(Integer.parseInt(strs[i])) + "*";
        }
        return str.substring(0, str.length()-1);
    }
    
    
    
    /**
     * vector: 需要相乘的集合, 相乘个数为2
     * srcList: 第1次为空,第2次开始,以前一次的结果集为srcList
     */
    public static List<Object[]> vectorMultiply(Vector<Double> vector, List<Object[]> srcList){
        List<Object[]> result = new ArrayList<Object[]>();
        int srcLen = 0, vectorLen = vector.size();
        Double multiplyValue = null;
        Set<String> keyAllSet = new HashSet<String>();;
        Object[] obj = null, obj2 = null;
        String key = null, keyAll = null;
        
        if(srcList != null && srcList.size() > 0){          //srcList不为空时
            srcLen = srcList.size();
            for (int i = 0; i < srcLen; i++) {
                obj = srcList.get(i);
                key = (String)obj[0];
                multiplyValue = (Double)obj[1];
                
                for (int j = 0; j < vectorLen; j++) {
                    String strJ = String.valueOf(j);
                    keyAll = getSortKey(key + "_" + strJ);
                    //判断: j是否存在key中, 而且keyAll是否存在keyAllSet中.
                    //例如: key=0_1, j=0 结果为否; 如keyAll=0_1_2, 且在keyAllSet中已存在,结果也为否.
                    if(key.indexOf(strJ) >= 0 || keyAllSet.contains(keyAll)){   
                        continue;
                    }else{
                        obj2 = new Object[2];
                        obj2[0] = keyAll;
                        obj2[1] = multiplyValue * vector.elementAt(j);
                        result.add(obj2);
                        keyAllSet.add(keyAll);
                    }
                }
            }
        }else{                                              //srcList为空: 第一次相乘的结果为: 0*1,0*2...1*2,1*3...
            for (int i = 0; i < vectorLen-1; i++) {
                for (int j = i+1; j < vectorLen; j++) {
                    obj = new Object[2];
                    obj[0] = i + "_" + j;
                    obj[1] = vector.elementAt(i) * vector.elementAt(j);
                    result.add(obj);
                }
            }
        }
        return result;
    }
    
    /**
     * 得到排序后的key
     */
    public static String getSortKey(String key){
        List<Integer> list = new ArrayList<Integer>();
        String[] strs = key.split("_");
        for (int i = 0; i < strs.length; i++) {
            list.add(Integer.parseInt(strs[i]));
        }
        Collections.sort(list);
        String key2 = "";
        for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
            key2 += (Integer) iterator.next() + "_";
        }
        return key2.substring(0, key2.length()-1);
    }
    
}
