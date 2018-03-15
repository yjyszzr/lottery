package com.dl.shop.lottery.logic;

import java.util.ArrayList;
import java.util.Vector;

import com.dl.shop.lottery.core.ProjectConstant;
import com.dl.shop.lottery.model.JczqSchema;

public class JzLogic extends JCBaseLogic {
	
	/**
	 * 获取用于计算注数的方案内容--混合过关
	 * @return  1,1,1,(10),10,1,1,1,1,1,1,3,1,0
	 */
	public static String getJczqHHContent(ArrayList<JczqSchema> listItem) {
        StringBuffer content = new StringBuffer();

        int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	StringBuffer itemCon = new StringBuffer();
        	
        	boolean [] result = jczqSchema.spf_selectedResult;
        	int size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
        	result = jczqSchema.rqspf_selectedResult;
        	size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
        	result = jczqSchema.bf_selectedResult;
        	size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
        	result = jczqSchema.bqspf_selectedResult;
        	size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
        	result = jczqSchema.zjq_selectedResult;
        	size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
            content.append(itemCon);
            
            if(i < (listLen - 1)){              
                content.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
            }
        }
        
        return content.toString();
    }
	
	/**
	 * 获取用于计算注数的方案内容
	 * @return  1,1,1,(10),10,1,1,1,1,1,1,3,1,0
	 */
	public static String getJczqBFContent(ArrayList<JczqSchema> listItem, String mPlay) {
        StringBuffer content = new StringBuffer();

        int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	boolean[] result = null ;
        	if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BF)){
        		result = jczqSchema.bf_selectedResult;
        	}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BQSPF)) {
        		result = jczqSchema.bqspf_selectedResult;
			}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_ZJQ)) {
				result = jczqSchema.zjq_selectedResult;
			}
        	StringBuffer itemCon = new StringBuffer();
        	
        	int size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
            content.append(itemCon);
            
            if(i < (listLen - 1)){              
                content.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
            }
        }
        
        return content.toString();
    }
	
	/**
     * 混合过关用, 获取计算(最大、最小)奖金需要的数据,格式：{1.5,2.5,3.1,6.1}
     */
    public static ArrayList<Vector<Double>> getHunHeBfSpBonusVector(ArrayList<JczqSchema> mListItem){
    	ArrayList<Vector<Double>> spBonus = new ArrayList<Vector<Double>>();
        Vector<Double> maxSPVector  = new Vector<Double>();
        Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	double tempMaxSp = 0;
        	double tempMinSp = 0;
        	
        	JczqSchema jczqSchema = mListItem.get(i);
//        	boolean[] tempSelectResult = null; 
//			String[] tempSp = null;
//			if(mPlay.equalsIgnoreCase(Constants.JZ_BF)){
//				tempSelectResult = jczqSchema.bf_selectedResult; 
//				tempSp = jczqSchema.bf_sp;
//			}else if(mPlay.equalsIgnoreCase(Constants.JZ_BQSPF)){
//				tempSelectResult = jczqSchema.bqspf_selectedResult; 
//				tempSp = jczqSchema.bqsfp_sp;
//			}else if(mPlay.equalsIgnoreCase(Constants.JZ_ZJQ)){
//				tempSelectResult = jczqSchema.zjq_selectedResult; 
//				tempSp = jczqSchema.zjq_sp;
//			}else if(mPlay.equalsIgnoreCase(Constants.JZ_2X1)){
//				tempSelectResult = jczqSchema.x21_selectedResult; 
//				tempSp = jczqSchema.x21_sp;
//			}
            
			/** 胜平负  */
	        Vector<Double> tempSpBonus = getMinMaxSP(jczqSchema.spf_selectedResult, jczqSchema.spf_sp, tempMaxSp, tempMinSp);
        	if (tempSpBonus.size() >= 2){
		        tempMaxSp = tempSpBonus.get(0);
	        	tempMinSp = tempSpBonus.get(1);
        	}
        	
        	/** 让球胜平负 */
        	tempSpBonus = getMinMaxSP(jczqSchema.rqspf_selectedResult, jczqSchema.rqspf_sp, tempMaxSp, tempMinSp);
        	if (tempSpBonus.size() >= 2){
		        tempMaxSp = tempSpBonus.get(0);
	        	tempMinSp = tempSpBonus.get(1);
        	}

        	/** 半全场胜平负 */
        	tempSpBonus = getMinMaxSP(jczqSchema.bqspf_selectedResult, jczqSchema.bqsfp_sp, tempMaxSp, tempMinSp);
        	if (tempSpBonus.size() >= 2){
		        tempMaxSp = tempSpBonus.get(0);
	        	tempMinSp = tempSpBonus.get(1);
        	}

        	/** 比分 */
        	tempSpBonus = getMinMaxSP(jczqSchema.bf_selectedResult, jczqSchema.bf_sp, tempMaxSp, tempMinSp);
        	if (tempSpBonus.size() >= 2){
		        tempMaxSp = tempSpBonus.get(0);
	        	tempMinSp = tempSpBonus.get(1);
        	}
        	
        	/** 总进球 */
        	tempSpBonus = getMinMaxSP(jczqSchema.zjq_selectedResult, jczqSchema.zjq_sp, tempMaxSp, tempMinSp);
        	if (tempSpBonus.size() >= 2){
		        tempMaxSp = tempSpBonus.get(0);
	        	tempMinSp = tempSpBonus.get(1);
        	}

	        maxSPVector.add(tempMaxSp);
            minSPVector.add(tempMinSp);
        }
        
        spBonus.add(maxSPVector);
        spBonus.add(minSPVector);
        
        return spBonus;
    }
    
	private static Vector<Double> getMinMaxSP(boolean[] tempSelectResult, String[] tempSp, double tempMaxSp, double tempMinSp) {
		Vector<Double> spVector  = new Vector<Double>();
		
		int resultLen = tempSp.length;
		for (int j = 0; j < resultLen; j++) {
			if (tempSelectResult[j] == true) {
				if (tempMaxSp < Double.parseDouble(tempSp[j])) {
					tempMaxSp = Double.parseDouble(tempSp[j]);
				}

				if (tempMinSp > Double.parseDouble(tempSp[j]) || tempMinSp == 0) {
					tempMinSp = Double.parseDouble(tempSp[j]);
				}
			}
		}
		
		spVector.add(tempMaxSp);
		spVector.add(tempMinSp);
		
		return spVector;
	}
    
	/**
     * 获取计算(最大、最小)奖金需要的数据,格式：{1.5,2.5,3.1,6.1}
     */
    public static ArrayList<Vector<Double>> getBfSpBonusVector(ArrayList<JczqSchema> mListItem, String mPlay){
    	ArrayList<Vector<Double>> spBonus = new ArrayList<Vector<Double>>();
        Vector<Double> maxSPVector  = new Vector<Double>();
        Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	double tempMaxSp = 0;
        	double tempMinSp = 0;
        	
        	JczqSchema jczqSchema = mListItem.get(i);
        	boolean[] tempSelectResult = null; 
			String[] tempSp = null;
			if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BF)){
				tempSelectResult = jczqSchema.bf_selectedResult; 
				tempSp = jczqSchema.bf_sp;
			}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BQSPF)){
				tempSelectResult = jczqSchema.bqspf_selectedResult; 
				tempSp = jczqSchema.bqsfp_sp;
			}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_ZJQ)){
				tempSelectResult = jczqSchema.zjq_selectedResult; 
				tempSp = jczqSchema.zjq_sp;
			}
			
            int resultLen = tempSp.length;
            for (int j = 0; j < resultLen; j++) {
            	if(tempSelectResult[j] == true){
                		if(tempMaxSp < Double.parseDouble(tempSp[j])){
                			tempMaxSp = Double.parseDouble(tempSp[j]);
                		}
                		
                		if(tempMinSp > Double.parseDouble(tempSp[j]) || tempMinSp == 0){
                			tempMinSp = Double.parseDouble(tempSp[j]);
                		}
            	}
			}
            maxSPVector.add(tempMaxSp);
            minSPVector.add(tempMinSp);
        }
        
        spBonus.add(maxSPVector);
        spBonus.add(minSPVector);
        
        return spBonus;
    }
    
    /**
     * 按照格式，生成投注的内容(比分、半全场、总进球)：
     * @param listItem
     * @return F20130820001#周二001#1,2,3#0/F20130820002#周二002#1,2,3#0/F20130820004#周二004#1,2,3#0
     *
     * 2012-8-14上午10:05:54
     */
    public static String getBetCon(ArrayList<JczqSchema> listItem, String mPlay, String[] selectItemTitle){
//    	ArrayList<String> betCon = new ArrayList<String>();
    	
    	StringBuffer codeCon = new StringBuffer();
//    	String qiHaoId = "";
//    	StringBuffer rangQiuCon = new StringBuffer();
//    	StringBuffer spCon = new StringBuffer();
//    	StringBuffer playIdCon = new StringBuffer();
    	
    	int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	boolean[] tempResult = null;
        	if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BF)){
        		tempResult = jczqSchema.bf_selectedResult;
        	}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BQSPF)){
        		tempResult = jczqSchema.bqspf_selectedResult;
        	}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_ZJQ)){
        		tempResult = jczqSchema.zjq_selectedResult;
        	}
        	
        	codeCon.append(jczqSchema.play_id);//对阵编号
            
        	codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        	codeCon.append(jczqSchema.chang_ci);//年月日(周四)
            
        	codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        	
        	//投注内容 -- 格式3/1/0,3/1,3
        	StringBuffer tempCodeCon = new StringBuffer();
        	for (int j = 0; j < tempResult.length; j++) {
        		if(tempResult[j] == true){
        			tempCodeCon.append(selectItemTitle[j]);
        			tempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
        		}
			}
        	
			if(tempCodeCon.lastIndexOf(ProjectConstant.CONTENTSPLITEFLAG_DouHao) == (tempCodeCon.length() - 1)){
				codeCon.append(tempCodeCon.substring(0, tempCodeCon.length() - 1));
			}else{
				codeCon.append(tempCodeCon);
			}
			
			codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
			codeCon.append("0");//是否胆拖
			
            if(i < (listLen - 1)){              
            	codeCon.append(ProjectConstant.ZqBetConItem);
            }
            
//            //期号id--最早那场对阵的qihao_id
//            String qihao = jczqSchema.qihao_id;
//            if(i == 0){
//            	qiHaoId = qihao;
//            }else{            	
//            	if(Integer.parseInt(qihao) < Integer.parseInt(qiHaoId)){
//            		qiHaoId = qihao;
//            	}
//            }
//            
//            //让球,格式：0,0,0
//            rangQiuCon.append(jczqSchema.gg_rq);
//            if(i < (listLen - 1)){              
//            	rangQiuCon.append(Constants.CONTENTSPLITEFLAG_DouHao);
//            }
//            
//            //sp值, 格式：0/0/0,0/1,0
//            StringBuffer tempSpCon = new StringBuffer();
//            String tempSp[] = null;
//            if(mPlay.equalsIgnoreCase(Constants.JZ_BF)){
//            	tempSp = jczqSchema.bf_sp;
//        	}else if(mPlay.equalsIgnoreCase(Constants.JZ_BQSPF)){
//        		tempSp = jczqSchema.bqsfp_sp;
//        	}else if(mPlay.equalsIgnoreCase(Constants.JZ_ZJQ)){
//        		tempSp = jczqSchema.zjq_sp;
//        	}
//            for (int j = 0; j < tempSp.length; j++) {
//				if(tempResult[j] == true){
//					tempSpCon.append(tempSp[j]);
//					tempSpCon.append(Constants.ZqBetConItem);
//				}
//			}
////            tempSpCon.append(result[0] == true ? (jczqSchema.host_sp + Constants.ZqBetConItem) : "");
////            tempSpCon.append(result[1] == true ? (jczqSchema.ping_sp + Constants.ZqBetConItem) : "");
////            tempSpCon.append(result[2] == true ? jczqSchema.guest_sp : "");
//            if(tempSpCon.lastIndexOf(Constants.ZqBetConItem) == (tempSpCon.length() - 1)){
//            	spCon.append(tempSpCon.substring(0, tempSpCon.length() - 1));
//            }else{
//            	spCon.append(tempSpCon);
//            }
//            
//            if(i < (listLen - 1)){              
//            	spCon.append(Constants.CONTENTSPLITEFLAG_DouHao);
//            }
//            
//            //场次ids的字符串
//            playIdCon.append(jczqSchema.play_id);
//            if(i < (listLen - 1)){              
//            	playIdCon.append(Constants.CONTENTSPLITEFLAG_DouHao);
//            }
    	}
        
//        betCon.add(qiHaoId);
//        betCon.add(codeCon.toString());
//        betCon.add(playIdCon.toString());
//        betCon.add(spCon.toString());
//        betCon.add(rangQiuCon.toString());
    	
    	return codeCon.toString();
    }
    
    /**
     * 按照格式，生成投注的内容(混合过关)：
     * F20130822001#周四001#7201_胜,7204_胜平,7204_胜负#0/F20130822002#周四002#7202_1:0,7203_1,7202_胜其它#0
     */
    public static String getBetCon(ArrayList<JczqSchema> listItem, String[] selectItemSpfTitle, String[] selectItemBfTitle, String[] selectItemBqSpfTitle, String[] selectItemZjqTitle){
    	
    	StringBuffer codeCon = new StringBuffer();
    	
    	int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	boolean[] tempSpfResult = jczqSchema.spf_selectedResult;
        	boolean[] tempRqSpfResult = jczqSchema.rqspf_selectedResult;
        	boolean[] tempBfResult = jczqSchema.bf_selectedResult;
        	boolean[] tempBqSpfResult = jczqSchema.bqspf_selectedResult;
        	boolean[] tempZjqResult = jczqSchema.zjq_selectedResult;
        	
        	codeCon.append(jczqSchema.play_id);//对阵编号
            
        	codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        	codeCon.append(jczqSchema.chang_ci);//年月日(周四)
            
        	codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        	
        	StringBuffer tempCodeCon = new StringBuffer();//code
        	
        	StringBuffer bfTempCodeCon = new StringBuffer();//code
        	StringBuffer bqspfTempCodeCon = new StringBuffer();//code
        	StringBuffer zjqTempCodeCon = new StringBuffer();//code
        	StringBuffer rqspfTempCodeCon = new StringBuffer();//code
        	StringBuffer spfTempCodeCon = new StringBuffer();//code
        	
        	//让球胜平负
        	for (int j = 0; j < tempRqSpfResult.length; j++) {
        		if(tempRqSpfResult[j] == true){
        			rqspfTempCodeCon.append(ProjectConstant.JCZQRQSPF);
        			rqspfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XiaHuaXian);
        			rqspfTempCodeCon.append(selectItemSpfTitle[j]);
        			rqspfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
        		}
			}

        	tempCodeCon.append(rqspfTempCodeCon);
        	
			//总进球
        	for (int j = 0; j < tempZjqResult.length; j++) {
        		if(tempZjqResult[j] == true){
        			zjqTempCodeCon.append(ProjectConstant.JCZQ_ZJQ);
        			zjqTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XiaHuaXian);
        			zjqTempCodeCon.append(selectItemZjqTitle[j]);
        			zjqTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
        		}
			}
        	
        	tempCodeCon.append(zjqTempCodeCon);
        	
			//比分
        	for (int j = 0; j < tempBfResult.length; j++) {
        		if(tempBfResult[j] == true){
        			bfTempCodeCon.append(ProjectConstant.JCZQ_BF);
        			bfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XiaHuaXian);
        			bfTempCodeCon.append(selectItemBfTitle[j]);
        			bfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
        		}
			}
        	
        	tempCodeCon.append(bfTempCodeCon);
        	
        	for (int j = 0; j < tempBqSpfResult.length; j++) {
        		if(tempBqSpfResult[j] == true){
        			bqspfTempCodeCon.append(ProjectConstant.JCZQ_BQSPF);
        			bqspfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XiaHuaXian);
        			bqspfTempCodeCon.append(selectItemBqSpfTitle[j]);
        			bqspfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
        		}
			}
        	
        	tempCodeCon.append(bqspfTempCodeCon);
        	
        	for (int j = 0; j < tempSpfResult.length; j++) {
        		if(tempSpfResult[j] == true){
        			spfTempCodeCon.append(ProjectConstant.JCZQSPF);
        			spfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XiaHuaXian);
        			spfTempCodeCon.append(selectItemSpfTitle[j]);
        			spfTempCodeCon.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
        		}
			}
        	
        	tempCodeCon.append(spfTempCodeCon);
        	
        	codeCon.append(getConOfRemoveComma(tempCodeCon));
        	
			codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
			codeCon.append("0");//是否胆拖
			
            if(i < (listLen - 1)){              
            	codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XieXian);
            }
            
    	}
        
    	return codeCon.toString();
    }
    
    private static String getConOfRemoveComma(StringBuffer mTarget){
    	if(mTarget.length() > 0 && mTarget.lastIndexOf(ProjectConstant.CONTENTSPLITEFLAG_DouHao) == (mTarget.length() - 1)){
    		return mTarget.substring(0, mTarget.length() - 1);
    	}
    	
    	return mTarget.toString();
    }
	
	/**
	 * 获取用于计算注数的方案内容
	 * 
	 * @param listItem 选择的足球对阵
	 * @return @return 选择的足彩内容：1,1,1,(10),10,1,1,1,1,1,1,3,1,0
	 *
	 * 2012-8-1上午10:25:09
	 */
	public static String getJczqSPFContent(ArrayList<JczqSchema> listItem) {
        StringBuffer content = new StringBuffer();

        int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	boolean[] result = jczqSchema.selectedResult;
        	StringBuffer itemCon = new StringBuffer();
        	
        	itemCon.append(result[0] == true ? ProjectConstant.host_win : "");
        	itemCon.append(result[1] == true ? ProjectConstant.host_ping : "");
        	itemCon.append(result[2] == true ? ProjectConstant.host_loss : "");
        	
            content.append(itemCon);
            
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
     * @create_time 2012-08-01 上午10:53:33
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
     * 按照格式，生成投注的内容：
     * @param listItem
     * @return
     *
     * 2012-8-14上午10:05:54
     */
    public static String getBetCon(ArrayList<JczqSchema> listItem){
//    	ArrayList<String> betCon = new ArrayList<String>();
    	
    	StringBuffer codeCon = new StringBuffer();
//    	String qiHaoId = "";
//    	StringBuffer rangQiuCon = new StringBuffer();
//    	StringBuffer spCon = new StringBuffer();
//    	StringBuffer playIdCon = new StringBuffer();
    	
    	int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	codeCon.append(getSingleSelectCon(jczqSchema));
			
            if(i < (listLen - 1)){              
            	codeCon.append(ProjectConstant.CONTENTSPLITEFLAG_XieXian);
            }
            
//            //期号id--最早那场对阵的qihao_id
//            String qihao = jczqSchema.qihao_id;
//            if(i == 0){
//            	qiHaoId = qihao;
//            }else{            	
//            	if(Integer.parseInt(qihao) < Integer.parseInt(qiHaoId)){
//            		qiHaoId = qihao;
//            	}
//            }
            
//            //让球,格式：0,0,0
//            rangQiuCon.append(jczqSchema.gg_rq);
//            if(i < (listLen - 1)){              
//            	rangQiuCon.append(Constants.CONTENTSPLITEFLAG_DouHao);
//            }
            
//            //sp值, 格式：0/0/0,0/1,0
//            StringBuffer tempSpCon = new StringBuffer();
//            tempSpCon.append(result[0] == true ? (jczqSchema.host_sp + Constants.ZqBetConItem) : "");
//            tempSpCon.append(result[1] == true ? (jczqSchema.ping_sp + Constants.ZqBetConItem) : "");
//            tempSpCon.append(result[2] == true ? jczqSchema.guest_sp : "");
//            if(tempSpCon.lastIndexOf(Constants.ZqBetConItem) == (tempSpCon.length() - 1)){
//            	spCon.append(tempSpCon.substring(0, tempSpCon.length() - 1));
//            }else{
//            	spCon.append(tempSpCon);
//            }
//            
//            if(i < (listLen - 1)){              
//            	spCon.append(Constants.CONTENTSPLITEFLAG_DouHao);
//            }
            
//            //场次ids的字符串
//            playIdCon.append(jczqSchema.play_id);
//            if(i < (listLen - 1)){              
//            	playIdCon.append(Config.CONTENTSPLITEFLAG_XieXian);
//            }
    	}
        
//        betCon.add(qiHaoId);
//        betCon.add(codeCon.toString());
//        betCon.add(playIdCon.toString());
//        betCon.add(spCon.toString());
//        betCon.add(rangQiuCon.toString());
    	
    	return codeCon.toString();
    }
    
    /**
     * 组装单场赛事的选择结果投注内容
     * @return con  F20130822001#周四001#胜,负#0/F20130822002#周四002#负,平#0
     */
    private static StringBuffer getSingleSelectCon(JczqSchema jczqSchema){
        StringBuffer con = new StringBuffer();
        
        con.append(jczqSchema.play_id);//对阵编号
        
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        con.append(jczqSchema.chang_ci);//年月日(周四)
        
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        
        StringBuffer tempCodeCon = new StringBuffer();
    	tempCodeCon.append(jczqSchema.selectedResult[0] == true ? (ProjectConstant.sheng + ProjectConstant.CONTENTSPLITEFLAG_DouHao) : "");
    	tempCodeCon.append(jczqSchema.selectedResult[1] == true ? (ProjectConstant.ping+ ProjectConstant.CONTENTSPLITEFLAG_DouHao) : "");
    	tempCodeCon.append(jczqSchema.selectedResult[2] == true ? ProjectConstant.fu : "");
    	
    	if(tempCodeCon.lastIndexOf(ProjectConstant.CONTENTSPLITEFLAG_DouHao) == (tempCodeCon.length() - 1)){
    		con.append(tempCodeCon.substring(0, tempCodeCon.length() - 1));
		}else{
			con.append(tempCodeCon);
		}
        con.append(ProjectConstant.CONTENTSPLITEFLAG_JinHao);
        con.append("0");//是否胆拖
//        con.append(Config.CONTENTSPLITEFLAG_XieXian);
        
        return con;
    }
    
    /**
     * 根据场次从小到大排序(game_date、chang_ci)
     */
	public static void sortByChangci(ArrayList<JczqSchema> listItem) {

        int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	for(int j = i + 1; j < listLen; j++){
        		JczqSchema tempJczqSchema1 = listItem.get(i);
        		JczqSchema tempJczqSchema2 = listItem.get(j);
//        		if(Integer.parseInt(tempJczqSchema1.play_id) > Integer.parseInt(tempJczqSchema2.play_id)){
//        			listItem.set(i, tempJczqSchema2);
//        			listItem.set(j, tempJczqSchema1);
//        		}
//        		if(Integer.parseInt(tempJczqSchema1.game_date) > Integer.parseInt(tempJczqSchema2.game_date)){
//    				listItem.set(i, tempJczqSchema2);
//        			listItem.set(j, tempJczqSchema1);
//        		}else if(Integer.parseInt(tempJczqSchema1.game_date) == Integer.parseInt(tempJczqSchema2.game_date)){
        			if(Integer.parseInt(tempJczqSchema1.game_no) > Integer.parseInt(tempJczqSchema2.game_no)){
        				listItem.set(i, tempJczqSchema2);
            			listItem.set(j, tempJczqSchema1);	
//        			}
				}
        	}
        }
        
    }
	
	/**
     * 获取计算最大奖金需要的数据,格式：{1.5,2.5,3.1,6.1}
     * @param mListItem
     * @return
     *
     * 2012-10-8下午05:03:12
     */
    public static Vector<Double> getSpBonusVector(ArrayList<JczqSchema> mListItem){
        Vector<Double> maxSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	Double tempMaxSp = Double.valueOf(0);
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(true == jczqSchema.selectedResult[0]){
            	if(tempMaxSp < Double.parseDouble(jczqSchema.host_sp)){
            		tempMaxSp = Double.parseDouble(jczqSchema.host_sp);
            	}
            }
            if(true == jczqSchema.selectedResult[1]){
            	if(tempMaxSp < Double.parseDouble(jczqSchema.ping_sp)){
            		tempMaxSp = Double.parseDouble(jczqSchema.ping_sp);
            	}
            }
            if(true == jczqSchema.selectedResult[2]){
            	if(tempMaxSp < Double.parseDouble(jczqSchema.guest_sp)){
            		tempMaxSp = Double.parseDouble(jczqSchema.guest_sp);
            	}
            }
            
            maxSPVector.add(tempMaxSp);
        }
        
        return maxSPVector;
    }
    
	/**
     * 获取计算最小奖金需要的数据,格式：{1.5,2.5,3.1,6.1}
     * @param mListItem
     * @return
     *
     * 2012-10-8下午05:03:12
     */
    public static Vector<Double> getMinSpBonusVector(ArrayList<JczqSchema> mListItem){
        Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	Double tempMinSp = Double.valueOf(0);
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(true == jczqSchema.selectedResult[0]){
            	if(tempMinSp > Double.parseDouble(jczqSchema.host_sp) || tempMinSp == 0){
            		tempMinSp = Double.parseDouble(jczqSchema.host_sp);
            	}
            }
            if(true == jczqSchema.selectedResult[1]){
            	if(tempMinSp > Double.parseDouble(jczqSchema.ping_sp) || tempMinSp == 0){
            		tempMinSp = Double.parseDouble(jczqSchema.ping_sp);
            	}
            }
            if(true == jczqSchema.selectedResult[2]){
            	if(tempMinSp > Double.parseDouble(jczqSchema.guest_sp) || tempMinSp == 0){
            		tempMinSp = Double.parseDouble(jczqSchema.guest_sp);
            	}
            }
            
            minSPVector.add(tempMinSp);
        }
        
        return minSPVector;
    }
    
    /**
     * 计算注数 --单场决胜
     * @param 
     * @return 注数
     * @create_time 2012-08-01 上午10:53:33
     */
    public static int getSPFDC(ArrayList<JczqSchema> mListItem) {
        int zhushu = 0;
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(true == jczqSchema.selectedResult[0]){
            	zhushu++;
            }
            
            if(true == jczqSchema.selectedResult[1]){
            	zhushu++;
            }
            if(true == jczqSchema.selectedResult[2]){
            	zhushu++;
            }
        }

        return zhushu;
    }
    
    /**
     * 计算最大奖金 --单场决胜
     * @param 
     * @return 注数
     * @create_time 2012-08-01 上午10:53:33
     */
    public static double getMaxSPFBonus(Vector<Double> maxBonusVector) {
        double maxBonus = 0;
        
        int selectLen = maxBonusVector.size();
        for (int i = 0; i < selectLen; i++) {
        	maxBonus += maxBonusVector.elementAt(i) * 0.9;
        }

        return maxBonus;
    }
    
    /**
     * 计算最小奖金 --单场决胜
     * @param 
     * @return 注数
     * @create_time 2012-08-01 上午10:53:33
     */
    public static double getMinSPFBonus(Vector<Double> minBonusVector ) {
    	double minBonus = minBonusVector.elementAt(0);
        
        int selectLen = minBonusVector.size();
        for (int i = 1; i < selectLen; i++) {
        	if(minBonus > minBonusVector.elementAt(i)){
        		minBonus = minBonusVector.elementAt(i);
        	}
        }

        return minBonus * 0.9;
    }
    
    /**
     * 是否有全包,返回全包的最小SP数组
     * @param minBonusVector
     * @return
     *
     * 2013-5-21下午05:31:24
     */
    public static Vector<Double> checkAll(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(jczqSchema.selectedResult != null && jczqSchema.selectedResult.length >= 3){
            	if(true == jczqSchema.selectedResult[0] 
                		&& true == jczqSchema.selectedResult[1]
                		&& true == jczqSchema.selectedResult[2]){
                	Double tempMinSp = Double.valueOf(0);
                	
                	if(Double.parseDouble(jczqSchema.host_sp) >= Double.parseDouble(jczqSchema.ping_sp)){
                		tempMinSp = Double.parseDouble(jczqSchema.ping_sp);
                	}else{
                		tempMinSp = Double.parseDouble(jczqSchema.host_sp);
                	}
                	
                	if(tempMinSp > Double.parseDouble(jczqSchema.guest_sp)){
                		tempMinSp = Double.parseDouble(jczqSchema.guest_sp);
                	}
                	
                	minSPVector.add(tempMinSp);
                }
            }
        }
        
        return minSPVector;
    }
    
    /**
     * 返回非全包的最小SP数组
     * @param minBonusVector
     * @return
     *
     * 2013-5-21下午05:31:24
     */
    public static Vector<Double> checkAll_NON(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(jczqSchema.selectedResult != null && jczqSchema.selectedResult.length >= 3){
            	if(false == jczqSchema.selectedResult[0] 
                		|| false == jczqSchema.selectedResult[1]
                		|| false == jczqSchema.selectedResult[2]){
                    	Double tempMinSp = Double.valueOf(0);
                    	
                        if(true == jczqSchema.selectedResult[0]){
                        	if(tempMinSp > Double.parseDouble(jczqSchema.host_sp) || tempMinSp == 0){
                        		tempMinSp = Double.parseDouble(jczqSchema.host_sp);
                        	}
                        }
                        if(true == jczqSchema.selectedResult[1]){
                        	if(tempMinSp > Double.parseDouble(jczqSchema.ping_sp) || tempMinSp == 0){
                        		tempMinSp = Double.parseDouble(jczqSchema.ping_sp);
                        	}
                        }
                        if(true == jczqSchema.selectedResult[2]){
                        	if(tempMinSp > Double.parseDouble(jczqSchema.guest_sp) || tempMinSp == 0){
                        		tempMinSp = Double.parseDouble(jczqSchema.guest_sp);
                        	}
                        }
                        
                        minSPVector.add(tempMinSp);
                }
            }
        }
        
        return minSPVector;
    }
    
    
    /** 获取计算(最大、最小)奖金需要的数据,格式：{1.5,2.5,3.1,6.1} */
    public static void getHHSpBonusVector(){
    	ArrayList<Object> all_sp_cmb = new ArrayList<Object>();
    	
    	// 1：0	胜	胜胜、平胜	1
    	ArrayList<Object>  sp_1_0 = new ArrayList<Object>(); 
    	sp_1_0.add("10");
    	sp_1_0.add("3");
    	ArrayList<String> bqspf_1_0 = new ArrayList<String>(); 
    	bqspf_1_0.add("33");
    	bqspf_1_0.add("13");
    	sp_1_0.add(bqspf_1_0);
    	sp_1_0.add("1");
    	
    	all_sp_cmb.add(sp_1_0);
    	
    	// 2：0	胜	胜胜、平胜	2
    	ArrayList<Object>  sp_2_0 = new ArrayList<Object>(); 
    	sp_2_0.add("20");
    	sp_2_0.add("3");
    	ArrayList<String> bqspf_2_0 = new ArrayList<String>(); 
    	bqspf_2_0.add("33");
    	bqspf_2_0.add("13");
    	sp_2_0.add(bqspf_1_0);
    	sp_2_0.add("2");
    	
    	all_sp_cmb.add(sp_2_0);
    	
    	// 2：1	胜	胜胜、平胜、负胜	3
    	ArrayList<Object>  sp_2_1 = new ArrayList<Object>(); 
    	sp_2_1.add("21");
    	sp_2_1.add("3");
    	ArrayList<String> bqspf_2_1 = new ArrayList<String>(); 
    	bqspf_2_1.add("33");
    	bqspf_2_1.add("13");
    	bqspf_2_1.add("03");
    	sp_2_1.add(bqspf_2_1);
    	sp_2_1.add("3");
    	
    	all_sp_cmb.add(sp_2_1);
    	
    	//3：0	胜	胜胜、平胜	3
    	ArrayList<Object>  sp_3_0 = new ArrayList<Object>(); 
    	sp_3_0.add("30");
    	sp_3_0.add("3");
    	ArrayList<String> bqspf_3_0 = new ArrayList<String>(); 
    	bqspf_3_0.add("33");
    	bqspf_3_0.add("13");
    	sp_3_0.add(bqspf_3_0);
    	sp_3_0.add("3");
    	
    	all_sp_cmb.add(sp_3_0);
    	
    	//3：1	胜	胜胜、平胜、负胜	4
    	ArrayList<Object>  sp_3_1 = new ArrayList<Object>(); 
    	sp_3_1.add("31");
    	sp_3_1.add("3");
    	ArrayList<String> bqspf_3_1 = new ArrayList<String>(); 
    	bqspf_3_1.add("33");
    	bqspf_3_1.add("13");
    	bqspf_3_1.add("03");
    	sp_3_1.add(bqspf_3_1);
    	sp_3_1.add("4");
    	
    	all_sp_cmb.add(sp_3_1);
    	
    	//3：2	胜	胜胜、平胜、负胜	5
    	ArrayList<Object>  sp_3_2 = new ArrayList<Object>(); 
    	sp_3_2.add("31");
    	sp_3_2.add("3");
    	ArrayList<String> bqspf_3_2 = new ArrayList<String>(); 
    	bqspf_3_2.add("33");
    	bqspf_3_2.add("13");
    	bqspf_3_2.add("03");
    	sp_3_2.add(bqspf_3_2);
    	sp_3_2.add("5");
    	
    	all_sp_cmb.add(sp_3_2);
    	
    	//4：0	胜	胜胜、平胜	4
    	ArrayList<Object>  sp_4_0 = new ArrayList<Object>(); 
    	sp_4_0.add("40");
    	sp_4_0.add("3");
    	ArrayList<String> bqspf_4_0 = new ArrayList<String>(); 
    	bqspf_4_0.add("33");
    	bqspf_4_0.add("13");
    	sp_4_0.add(bqspf_4_0);
    	sp_4_0.add("4");
    	
    	all_sp_cmb.add(sp_4_0);
    	
    	//4：1	胜	胜胜、平胜、负胜	5
    	ArrayList<Object>  sp_4_1 = new ArrayList<Object>(); 
    	sp_4_1.add("41");
    	sp_4_1.add("3");
    	ArrayList<String> bqspf_4_1 = new ArrayList<String>(); 
    	bqspf_4_1.add("33");
    	bqspf_4_1.add("13");
    	bqspf_4_1.add("03");
    	sp_4_1.add(bqspf_4_0);
    	sp_4_1.add("5");
    	
    	all_sp_cmb.add(sp_4_1);
    	
    	//4：2	胜	胜胜、平胜、负胜	6
    	ArrayList<Object>  sp_4_2 = new ArrayList<Object>(); 
    	sp_4_2.add("42");
    	sp_4_2.add("3");
    	ArrayList<String> bqspf_4_2 = new ArrayList<String>(); 
    	bqspf_4_2.add("33");
    	bqspf_4_2.add("13");
    	bqspf_4_2.add("03");
    	sp_4_2.add(bqspf_4_2);
    	sp_4_2.add("6");
    	
    	all_sp_cmb.add(sp_4_2);
    	
    	//5：0	胜	胜胜、平胜	5
    	ArrayList<Object>  sp_5_0 = new ArrayList<Object>(); 
    	sp_5_0.add("50");
    	sp_5_0.add("3");
    	ArrayList<String> bqspf_5_0 = new ArrayList<String>(); 
    	bqspf_5_0.add("33");
    	bqspf_5_0.add("13");
    	sp_5_0.add(bqspf_5_0);
    	sp_5_0.add("5");
    	
    	all_sp_cmb.add(sp_5_0);
    	
    	//5：1	胜	胜胜、平胜、负胜	6
    	ArrayList<Object>  sp_5_1 = new ArrayList<Object>(); 
    	sp_5_1.add("51");
    	sp_5_1.add("3");
    	ArrayList<String> bqspf_5_1 = new ArrayList<String>(); 
    	bqspf_5_1.add("33");
    	bqspf_5_1.add("13");
    	bqspf_5_1.add("03");
    	sp_5_1.add(bqspf_5_1);
    	sp_5_1.add("6");
    	
    	all_sp_cmb.add(sp_5_1);
    	
    	//5：2	胜	胜胜、平胜、负胜	7+
    	ArrayList<Object>  sp_5_2 = new ArrayList<Object>(); 
    	sp_5_2.add("52");
    	sp_5_2.add("3");
    	ArrayList<String> bqspf_5_2 = new ArrayList<String>(); 
    	bqspf_5_2.add("33");
    	bqspf_5_2.add("13");
    	bqspf_5_2.add("03");
    	sp_5_2.add(bqspf_5_2);
    	sp_5_2.add("7");
    	
    	all_sp_cmb.add(sp_5_2);
    	
    	//胜其他	胜	胜胜、平胜、负胜	7+
    	ArrayList<Object>  sp_9_0 = new ArrayList<Object>(); 
    	sp_9_0.add("90");
    	sp_9_0.add("3");
    	ArrayList<String> bqspf_9_0 = new ArrayList<String>(); 
    	bqspf_9_0.add("33");
    	bqspf_9_0.add("13");
    	bqspf_9_0.add("03");
    	sp_9_0.add(bqspf_9_0);
    	sp_9_0.add("7");
    	
    	all_sp_cmb.add(sp_9_0);
    	
    	//0：0	平	平平	0
    	ArrayList<Object>  sp_0_0 = new ArrayList<Object>(); 
    	sp_0_0.add("00");
    	sp_0_0.add("1");
    	ArrayList<String> bqspf_0_0 = new ArrayList<String>(); 
    	bqspf_0_0.add("11");
    	sp_0_0.add(bqspf_0_0);
    	sp_0_0.add("0");
    	
    	all_sp_cmb.add(sp_0_0);
    	
    	//1：1	平	胜平、平平、负平	2
    	ArrayList<Object>  sp_1_1 = new ArrayList<Object>(); 
    	sp_1_1.add("11");
    	sp_1_1.add("1");
    	ArrayList<String> bqspf_1_1 = new ArrayList<String>(); 
    	bqspf_1_1.add("31");
    	bqspf_1_1.add("11");
    	bqspf_1_1.add("01");
    	sp_0_0.add(bqspf_1_1);
    	sp_0_0.add("2");
    	
    	all_sp_cmb.add(sp_1_1);
    	
    	//2：2	平	胜平、平平、负平	4
    	ArrayList<Object>  sp_2_2 = new ArrayList<Object>(); 
    	sp_2_2.add("22");
    	sp_2_2.add("1");
    	ArrayList<String> bqspf_2_2 = new ArrayList<String>(); 
    	bqspf_2_2.add("31");
    	bqspf_2_2.add("11");
    	bqspf_2_2.add("01");
    	sp_2_2.add(bqspf_2_2);
    	sp_2_2.add("4");
    	
    	all_sp_cmb.add(sp_2_2);
    	
    	//3：3	平	胜平、平平、负平	6
    	ArrayList<Object>  sp_3_3 = new ArrayList<Object>(); 
    	sp_3_3.add("33");
    	sp_3_3.add("1");
    	ArrayList<String> bqspf_3_3 = new ArrayList<String>(); 
    	bqspf_3_3.add("31");
    	bqspf_3_3.add("11");
    	bqspf_3_3.add("01");
    	sp_3_3.add(bqspf_3_3);
    	sp_3_3.add("6");
    	
    	all_sp_cmb.add(sp_3_3);
    	
    	//平其他	平	胜平、平平、负平	7+
    	ArrayList<Object>  sp_9_9 = new ArrayList<Object>(); 
    	sp_9_9.add("99");
    	sp_9_9.add("1");
    	ArrayList<String> bqspf_9_9 = new ArrayList<String>(); 
    	bqspf_9_9.add("31");
    	bqspf_9_9.add("11");
    	bqspf_9_9.add("01");
    	sp_9_9.add(bqspf_9_9);
    	sp_9_9.add("7");
    	
    	all_sp_cmb.add(sp_9_9);
    	
    	//0：1	负	负负、平负	1
    	ArrayList<Object>  sp_0_1 = new ArrayList<Object>(); 
    	sp_0_1.add("01");
    	sp_0_1.add("0");
    	ArrayList<String> bqspf_0_1 = new ArrayList<String>(); 
    	bqspf_0_1.add("00");
    	bqspf_0_1.add("10");
    	sp_0_1.add(bqspf_0_1);
    	sp_0_1.add("1");
    	
    	all_sp_cmb.add(sp_0_1);
    	
    	//0：2	负	负负、平负	2
    	ArrayList<Object>  sp_0_2 = new ArrayList<Object>(); 
    	sp_0_2.add("02");
    	sp_0_2.add("0");
    	ArrayList<String> bqspf_0_2 = new ArrayList<String>(); 
    	bqspf_0_2.add("00");
    	bqspf_0_2.add("10");
    	sp_0_1.add(bqspf_0_2);
    	sp_0_1.add("2");
    	
    	all_sp_cmb.add(sp_0_2);
    	
    	//1：2	负	负负、平负、胜负	3
    	ArrayList<Object>  sp_1_2 = new ArrayList<Object>(); 
    	sp_1_2.add("12");
    	sp_1_2.add("0");
    	ArrayList<String> bqspf_1_2 = new ArrayList<String>(); 
    	bqspf_1_2.add("00");
    	bqspf_1_2.add("10");
    	bqspf_1_2.add("30");
    	sp_1_2.add(bqspf_1_2);
    	sp_1_2.add("3");
    	
    	all_sp_cmb.add(sp_1_2);
    	
    	//0：3	负	负负、平负	3
    	ArrayList<Object>  sp_0_3 = new ArrayList<Object>(); 
    	sp_0_3.add("03");
    	sp_0_3.add("0");
    	ArrayList<String> bqspf_0_3 = new ArrayList<String>(); 
    	bqspf_0_3.add("00");
    	bqspf_0_3.add("10");
    	sp_0_3.add(bqspf_0_3);
    	sp_0_3.add("3");
    	
    	all_sp_cmb.add(sp_0_3);
    	
    	//1：3	负	负负、平负、胜负	4
    	ArrayList<Object>  sp_1_3 = new ArrayList<Object>(); 
    	sp_1_3.add("13");
    	sp_1_3.add("0");
    	ArrayList<String> bqspf_1_3 = new ArrayList<String>(); 
    	bqspf_1_3.add("00");
    	bqspf_1_3.add("10");
    	bqspf_1_3.add("30");
    	sp_1_3.add(bqspf_1_3);
    	sp_1_3.add("4");
    	
    	all_sp_cmb.add(sp_1_3);
    	
    	//2：3	负	负负、平负、胜负	5
    	ArrayList<Object>  sp_2_3 = new ArrayList<Object>(); 
    	sp_2_3.add("23");
    	sp_2_3.add("0");
    	ArrayList<String> bqspf_2_3 = new ArrayList<String>(); 
    	bqspf_2_3.add("00");
    	bqspf_2_3.add("10");
    	sp_2_3.add(bqspf_2_3);
    	sp_2_3.add("5");
    	
    	all_sp_cmb.add(sp_2_3);
    	
    	//0：4	负	负负、平负	4
    	ArrayList<Object>  sp_0_4 = new ArrayList<Object>(); 
    	sp_0_4.add("04");
    	sp_0_4.add("0");
    	ArrayList<String> bqspf_0_4 = new ArrayList<String>(); 
    	bqspf_0_4.add("00");
    	bqspf_0_4.add("10");
    	sp_0_4.add(bqspf_0_4);
    	sp_0_4.add("4");
    	
    	all_sp_cmb.add(sp_0_4);
    	
    	//1：4	负	负负、平负、胜负	5
    	ArrayList<Object>  sp_1_4 = new ArrayList<Object>(); 
    	sp_1_4.add("14");
    	sp_1_4.add("0");
    	ArrayList<String> bqspf_1_4 = new ArrayList<String>(); 
    	bqspf_1_4.add("00");
    	bqspf_1_4.add("10");
    	bqspf_1_4.add("30");
    	sp_1_4.add(bqspf_1_4);
    	sp_1_4.add("4");
    	
    	all_sp_cmb.add(sp_1_4);
    	
    	//2：4	负	负负、平负、胜负	6
    	ArrayList<Object>  sp_2_4 = new ArrayList<Object>(); 
    	sp_2_4.add("24");
    	sp_2_4.add("0");
    	ArrayList<String> bqspf_2_4 = new ArrayList<String>(); 
    	bqspf_2_4.add("00");
    	bqspf_2_4.add("10");
    	bqspf_2_4.add("30");
    	sp_2_4.add(bqspf_2_4);
    	sp_2_4.add("6");
    	
    	all_sp_cmb.add(sp_2_4);
    	
    	//0：5	负	负负、平负	5
    	ArrayList<Object>  sp_0_5 = new ArrayList<Object>(); 
    	sp_0_5.add("24");
    	sp_0_5.add("0");
    	ArrayList<String> bqspf_0_5 = new ArrayList<String>(); 
    	bqspf_0_5.add("00");
    	bqspf_0_5.add("10");
    	sp_0_5.add(bqspf_0_5);
    	sp_0_5.add("5");
    	
    	all_sp_cmb.add(sp_0_5);
    	
    	//1：5	负	负负、平负、胜负	6
    	ArrayList<Object>  sp_1_5 = new ArrayList<Object>(); 
    	sp_1_5.add("24");
    	sp_1_5.add("0");
    	ArrayList<String> bqspf_1_5 = new ArrayList<String>(); 
    	bqspf_1_5.add("00");
    	bqspf_1_5.add("10");
    	sp_1_5.add(bqspf_1_5);
    	sp_1_5.add("6");
    	
    	all_sp_cmb.add(sp_1_5);
    	
    	//2：5	负	负负、平负、胜负	7+
    	ArrayList<Object>  sp_2_5 = new ArrayList<Object>(); 
    	sp_2_5.add("25");
    	sp_2_5.add("0");
    	ArrayList<String> bqspf_2_5 = new ArrayList<String>(); 
    	bqspf_2_5.add("00");
    	bqspf_2_5.add("10");
    	bqspf_2_5.add("30");
    	sp_2_5.add(bqspf_2_5);
    	sp_2_5.add("7");
    	
    	all_sp_cmb.add(sp_2_5);
    	
    	//负其他	负	负负、平负、胜负	7+
    	ArrayList<Object>  sp_0_9 = new ArrayList<Object>(); 
    	sp_0_9.add("09");
    	sp_0_9.add("0");
    	ArrayList<String> bqspf_0_9 = new ArrayList<String>(); 
    	bqspf_0_9.add("00");
    	bqspf_0_9.add("10");
    	bqspf_0_9.add("30");
    	sp_0_9.add(bqspf_0_9);
    	sp_0_9.add("7");
    	
    	all_sp_cmb.add(sp_0_9);
    }
    
    private static boolean checkAllItemIsSelect(boolean[] tempSelectList){
    	boolean allIsSelected = true;
    	
    	for (int i = 0; i < tempSelectList.length; i++) {
			if(!(tempSelectList[i])){
				return false;
			}
		}
    	
    	return allIsSelected;
    }
    
    private static boolean checkAllNonItemIsSelect(boolean[] tempSelectList){
    	boolean allIsSelected = false;
    	
    	for (int i = 0; i < tempSelectList.length; i++) {
			if(!(tempSelectList[i])){
				return true;
			}
		}
    	
    	return allIsSelected;
    }
    
    
    /**
     * 是否有全包,返回全包的最小SP数组
     * @param minBonusVector
     * @return
     *
     * 2013-5-21下午05:31:24
     */
    public static Vector<Double> checkBfAll(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(checkAllItemIsSelect(jczqSchema.bf_selectedResult)){
            	Double tempMinSp = Double.parseDouble(jczqSchema.bf_sp[0]);
            	
            	for (int j = 1; j < jczqSchema.bf_selectedResult.length; j++) {
            		if(Double.parseDouble(jczqSchema.bf_sp[j]) <  tempMinSp ){
            			tempMinSp = Double.parseDouble(jczqSchema.bf_sp[j]);
            		}
				}
            	
            	minSPVector.add(tempMinSp);
            }
        }
        
        return minSPVector;
    }
    
    /**
     * 返回非全包的最小SP数组
     */
    public static Vector<Double> checkBfAll_NON(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(checkAllNonItemIsSelect(jczqSchema.bf_selectedResult)){
                	Double tempMinSp = Double.valueOf(0);
                	tempMinSp = Double.parseDouble(jczqSchema.bf_sp[0]);
                	for (int j = 0; j < jczqSchema.bf_sp.length; j++) {
                		if(Double.parseDouble(jczqSchema.bf_sp[j]) <  tempMinSp  && jczqSchema.bf_selectedResult[j] ){
                			tempMinSp = Double.parseDouble(jczqSchema.bf_sp[j]);
                		}
					}
                    minSPVector.add(tempMinSp);
            }
        }
        
        return minSPVector;
    }
    
    public static Vector<Double> checkZjqAll(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(checkAllItemIsSelect(jczqSchema.zjq_selectedResult)){
            	Double tempMinSp = Double.parseDouble(jczqSchema.zjq_sp[0]);
            	
            	for (int j = 1; j < jczqSchema.zjq_selectedResult.length; j++) {
            		if(Double.parseDouble(jczqSchema.zjq_sp[j]) <  tempMinSp ){
            			tempMinSp = Double.parseDouble(jczqSchema.zjq_sp[j]);
            		}
				}
            	
            	minSPVector.add(tempMinSp);
            }
        }
        
        return minSPVector;
    }
    
    /** 返回非全包的最小SP数组     */
    public static Vector<Double> checkZjqAll_NON(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(checkAllNonItemIsSelect(jczqSchema.zjq_selectedResult)){
                	Double tempMinSp = Double.valueOf(0);
                	tempMinSp = Double.parseDouble(jczqSchema.zjq_sp[0]);
                	for (int j = 0; j < jczqSchema.zjq_sp.length; j++) {
                		if(Double.parseDouble(jczqSchema.zjq_sp[j]) <  tempMinSp && jczqSchema.zjq_selectedResult[j] ){
                			tempMinSp = Double.parseDouble(jczqSchema.zjq_sp[j]);
                		}
					}
                    minSPVector.add(tempMinSp);
            }
        }
        
        return minSPVector;
    }
    
    public static Vector<Double> checkBqspfAll(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(checkAllItemIsSelect(jczqSchema.bqspf_selectedResult)){
            	Double tempMinSp = Double.parseDouble(jczqSchema.bqsfp_sp[0]);
            	
            	for (int j = 1; j < jczqSchema.bqspf_selectedResult.length; j++) {
            		if(Double.parseDouble(jczqSchema.bqsfp_sp[j]) <  tempMinSp ){
            			tempMinSp = Double.parseDouble(jczqSchema.bqsfp_sp[j]);
            		}
				}
            	
            	minSPVector.add(tempMinSp);
            }
        }
        
        return minSPVector;
    }
    
    /** 返回非全包的最小SP数组     */
    public static Vector<Double> checkBqspfAll_NON(ArrayList<JczqSchema> mListItem) {
    	Vector<Double> minSPVector  = new Vector<Double>();
        
        int selectLen = mListItem.size();
        for (int i = 0; i < selectLen; i++) {
        	
            JczqSchema jczqSchema = mListItem.get(i);
            if(checkAllNonItemIsSelect(jczqSchema.bqspf_selectedResult)){
                	Double tempMinSp = Double.valueOf(0);
                	tempMinSp = Double.parseDouble(jczqSchema.bqsfp_sp[0]);
                	for (int j = 0; j < jczqSchema.bqsfp_sp.length; j++) {
                		if(Double.parseDouble(jczqSchema.bqsfp_sp[j]) <  tempMinSp  && jczqSchema.bqspf_selectedResult[j]  ){
                			tempMinSp = Double.parseDouble(jczqSchema.bqsfp_sp[j]);
                		}
					}
                    minSPVector.add(tempMinSp);
            }
        }
        
        return minSPVector;
    }
    
	/**
	 * 获取用于计算注数的方案内容
	 * @return  1,1,1,(10),10,1,1,1,1,1,1,3,1,0
	 */
	public static String getJczqBfBqspfZjqX21Content(ArrayList<JczqSchema> listItem, String mPlay) {
        StringBuffer content = new StringBuffer();

        int listLen = listItem.size();
        for(int i = 0; i < listLen; i++){
        	JczqSchema jczqSchema = listItem.get(i);
        	boolean[] result = null ;
        	if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BF)){
        		result = jczqSchema.bf_selectedResult;
        	}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_BQSPF)) {
        		result = jczqSchema.bqspf_selectedResult;
			}else if(mPlay.equalsIgnoreCase(ProjectConstant.JZ_ZJQ)) {
				result = jczqSchema.zjq_selectedResult;
			}
        	StringBuffer itemCon = new StringBuffer();
        	
        	int size = result.length;
        	for (int j = 0; j < size; j++) {
				itemCon.append(result[j] == true ? ProjectConstant.host_win : "");
			}
        	
            content.append(itemCon);
            
            if(i < (listLen - 1)){              
                content.append(ProjectConstant.CONTENTSPLITEFLAG_DouHao);
            }
        }
        
        return content.toString();
    }
	
	/**
	 * 校验投注内容，预防出现未选中比赛结果的对阵，导致出不了票
	 * 
	 * @param betCon 投注接口中用到的投注内容
	 * @return
	 */
	public static boolean checkBetCon(String betCon){
		if(betCon.contains("##")){
			return false;
		}
		
		return true;
	}
    
}
