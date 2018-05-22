package com.dl.shop.lottery.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dl.shop.lottery.model.JczqSchema;

public class ProjectConstant {
	public static final String BASE_PACKAGE = "com.dl.shop.lottery";//项目基础包名称，根据自己公司的项目修改

    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";//Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";//Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";//Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".mapper.Mapper";//Mapper插件基础接口的完全限定名
    public static final String MAPPER_BASE = "com.dl.base.mapper.Mapper";//Mapper插件基础接口的完全限定名
    
  //状态 0-售卖中 1-售卖结束
    public final static Integer MATCH_PLAY_STATUS_SELLING = 0; //售卖中
    public final static Integer MATCH_PLAY_STATUS_SELLED = 1;  //售卖结束
    
    //是否热门 0-非热门 1-热门
    public final static Integer MATCH_PLAY_NOT_HOT = 0; //非热门
    public final static Integer MATCH_PLAY_IS_HOT = 1;  //热门
    
    //是否删除 0-未删除 1-删除
    public final static Integer IS_NOT_DEL = 0;  //未删除
    public final static Integer IS_DEL = 1;      //删除
    
    //是否显示 0-不显示 1-显示
    public final static Integer IS_NOT_SHOW = 0;  //不显示
    public final static Integer IS_SHOW= 1;       //显示
    
    //审核状态： 0-未审核 1-审核通过 2-审核失败
    public final static Integer AUDIT_STAY = 0;   //待审核
    public final static Integer AUDIT_SUCCESS= 1; //审核通过
    public final static Integer AUDIT_FAIL= 2;    //审核失败
    
    public final static Integer CALLBACK_STAKE_SUCCESS= 16;   //出票回调成功状态
    public final static Integer CALLBACK_STAKE_FAIL= 17;   //出票回调失败状态
    public final static Integer CALLBACK_STAKE_ING= 8;   //出票回调出票中状态
    
    public static final String FORMAT_WINNING_MSG = "恭喜【{0}】投注竞足中奖";
    
    public final static String CONTENTSPLITEFLAG_ShuXian = "|";
    public final static String CONTENTSPLITEFLAG_DouHao = ",";
    public final static String RANDOMSPLITEFLAG_FenHao = ";";
    public final static String CONTENTSPLITEFLAG_JinHao = "#";
    public final static String CONTENTSPLITEFLAG_HenXian = "-";
    public final static String CONTENTSPLITEFLAG_XieXian = "/";
    public final static String CONTENTSPLITEFLAG_XiaHuaXian = "_";
    
    // 大乐透点数要求
    public static int DLT_Normal_MINFIRST = 5;
    public static int DLT_Normal_MINSECOND = 2;
    public static int DLT_DT_MAXFIRST = 4;
    public static int DLT_DT_MINFIRST = 1;
    public static int DLT_DT_MAXSECOND = 1;
    public static int DLT_DT_MINSECOND = 1;
    /** 前区码必须大于5个  */
    public static int DLT_DT_MINFIRSTTOTAL = 5;
    /** 前区码必须小于18个  */
    public static int DLT_Normal_MAXFIRST = 18;
    
 // 福彩3D点数
    public static int FC_MIN = 1;
    // 福彩组3
    public static int FC_Z3MIN = 2;
    // 福彩组6
    public static int FC_Z6MIN = 3;

    // 11选5任1
    public static int X115_R3_MIN = 3;
    // 11选5任3
    public static int X115_R5_MIN = 5;
    // 11选5任5
    public static int X115_R8_MIN = 8;
    // 11选5任5
    public static int X115_B3_MIN = 1;
    
    // 玩法
    public static String FC_Play_Single = "22";// 直选单式(1,2,3)

    public static String SSQ_PLAY_BZ = "502";// 双色球标准
    public static String SSQ_PLAY_DT = "503";// 双色球胆拖
    public static String SSQ_PLAY_JX = "501";// 双色球机选

    public static String FC3D_PLAY_BZ = "602";// 福彩直选复式
    public static String FC3D_PLAY_Z3 = "605";// 福彩组3
    public static String FC3D_PLAY_Z6 = "604";// 福彩组6
    public static String FC3D_PLAY_Z6_Single = "603";// 福彩组6单式
    public static String FC3D_PLAY_JX = "601";// 福彩直选单式
    
    public static String PL5_PLAY_BZ = "6402";// 排列5直选复式
    public static String PL5_PLAY_Single = "6401";// 排列5直选单式

    public static String DLT_PLAY_BZ = "3902";// 大乐透标准
    public static String DLT_PLAY_DT = "114";// 大乐透胆拖
    public static String DLT_PLAY_JX = "3901";// 大乐透机选
    public static String DLT_PLAY_ZJ_MUL = "3904";// 大乐透追加复式
    public static String DLT_PLAY_ZJ = "3903";// 大乐透追加单式

    public static String PL3_PLAY_BZ = "6302";// 排列3直选复式
    public static String PL3_PLAY_Z3 = "6305";// 排列3组3
    public static String PL3_PLAY_Z6 = "6304";// 排列3组6
    public static String PL3_PLAY_Z6_Single = "6303";// 排列3组6单式
    public static String PL3_PLAY_JX = "6301";// 排列3直选单式

    public static String X115_PLAY_BZ_R3 = "204";// 11选5任3标准
    public static String X115_PLAY_BZ_R5 = "208";// 11选5任5标准
    public static String X115_PLAY_BZ_R8 = "214";// 11选5任8标准
    public static String X115_PLAY_BZ_B3 = "216";// 11选5前3标准
    
    public static String GDX115_PLAY_BZ_R3 = "204";// gd11选5任3标准
    public static String GDX115_PLAY_BZ_R5 = "208";// gd11选5任5标准
    public static String GDX115_PLAY_BZ_R7 = "212";// gd11选5任7标准
    public static String GDX115_PLAY_BZ_Q2 = "220";// gd11选5前2标准

    public static String SFC14_PLAY_DZ = "101";// 胜负彩14场单式
    public static String SFC14_PLAY_BZ = "102";// 胜负彩14场标准

    public static String R9_PLAY_DZ = "103";// 任选9场单式
    public static String R9_PLAY_BZ = "104";// 任选9场标准
    public static String R9_PLAY_DT = "267";// 任选9场胆拖

    public static String QXC_PLAY_JX = "301";//七星彩单式
    public static String QXC_PLAY_ZX = "302";//七星彩复式
    public static String QXC_PIAY_HM = "235";//七星彩合买

    public static String X155_PIAY_JX = "120";//15选5机选->120
    public static String X155_PIAY_ZX = "121";//15选5标准手选->121
    public static String X155_PIAY_DT = "337";//15选5胆拖->337
    public static String X155_PIAY_HM = "124";//15选5跟单->124

    public static String QLC_PIAY_JX = "1301";//七乐彩机选单式
    public static String QLC_PIAY_ZX = "1302";//七乐彩复式
    public static String QLC_PIAY_DT = "127";//七乐彩胆拖->127
    public static String QLC_PIAY_HM = "130"; //七乐彩跟单->130

    public static String SSC_PIAY_ZX5 = "175"; //五星直选手选->175
    public static String SSC_PIAY_ZX4 = "180";//四星手选->190
    public static String SSC_PIAY_ZX3 = "179";//三星直选手选->179
    public static String SSC_PIAY_ZX2 = "186";//二星直选手选->186
    public static String SSC_PIAY_ZX1 = "190";//一星手选->190
    public static String SSC_PIAY_ZXDXDS = "192"; //大小单双手选->192
    
    // 彩种--777
    public static String SSQ = "5";
    public static String FC3D = "6";
    public static String DLT = "39";
    public static String PL3 = "63";
    public static String PL5 = "64";
    public static String SF14 = "1";
    public static String BJDC = "45";
    public static String JCZQ = "72";
    public static String QLC = "13";//七乐彩
    public static String JCZQSPF = "7205";//竞足胜平负
    public static String JCZQRQSPF = "7201";//竞足让球胜平负
    public static String JCZQ_BF = "7202";//竞足比分
    public static String JCZQ_ZJQ = "7203";//竞足总进球
    public static String JCZQ_BQSPF = "7204";//竞足半全场胜平负
    public static String JCZQ_HH = "7206";//竞足混合
    public static String QXC = "3"; //七星彩  
    public static String LC = "73";//篮彩
    public static String BCSFP = "4055";//半场胜负平
    public static String LCDXF = "7304";//篮彩大小分
    public static String LCSFC = "7303";//篮彩胜分差
    public static String LCRFSF = "7302";//篮彩让分胜负
    public static String LCSF = "7301";//篮彩胜负
    public static String LCHH = "7305";//篮彩混合过关
    public static String K3_NM = "80"; //内蒙快3
    public static String K3_HB = "83"; //湖北快3
    public static String K3_JL = "82"; //吉林快3
    public static String K3_JS = "81"; //江苏快3
    public static String K3_JX = "85"; //江西快3
    public static String GDX115 = "70"; //广东11选5
    public static String shiShicai = "28"; //时时彩     
    public static String JX11X5 = "90"; //江西11选5
    public static String SD11X5 = "91"; //山东11选5
    public static String AH11X5 = "93"; //安徽11选5
    public static String SSC_JX = "61"; //江西时时彩     
    public static String K3_AH = "84"; //安徽快3
    public static String XJ11X5 = "94"; //新疆11选5  
    public static String OZCGJ = "74"; //冠军  
    public static String OZCGYJ = "75"; //冠亚军  
    public static String HB11X5 = "95"; //湖北11选5
    
	/** 竞彩足球已选赛事列表 */
	public static ArrayList<JczqSchema> JCZQ_Select_SHAMES = new ArrayList<JczqSchema>();
	
	/**  北单-期号 */
	public static final String BD_QIHAO_ID = "bd_qihao_id";
	
	/** 北京单场最少场数 */
	public static int BD_SPF_MIN = 1;
	
	/** 竞彩篮球最少场数 */
	public static int JCLQ_MIN = 2;
	
	/** 竞彩足球最少场数 */
	public static int JCZQ_MIN = 2;
	
	/** 竞彩足球最少场数 */
	public static int JCZQ_SPF_MIN = 1;
	
	public static int JC_MIN_2 = 2;
	
	public static int JC_MIN_1 = 1;
	
	/** 北单过关方式最大值 */
	public static int BD_Clearance_MAX = 15;
	/** 竞彩足球过关方式最大值--比分 */
	public static int JCZQ_Clearance_BF_MAX = 4;
	/** 竞彩足球过关方式最大值--半全场 */
	public static int JCZQ_Clearance_BQSPF_MAX = 4;
	/** 竞彩足球过关方式最大值--总进球 */
	public static int JCZQ_Clearance_ZJQ_MAX = 6;
	
	/** 竞彩足球过关方式最大值 */
	public static int JCZQ_Clearance_MAX = 8;
	
	/** 竞足-让球胜平负 */
	public static final String JZ_RQSPF = "rqspf";
	
	/** 竞足-胜平负 */
	public static final String JZ_SPF = "spf";
	
	/** 竞足-半全场胜平负 */
	public static final String JZ_BQSPF = "bqspf";
	
	/** 竞足-比分 */
	public static final String JZ_BF = "bf";
	
	/** 竞足-总进球 */
	public static final String JZ_ZJQ = "zjq";
	
	/** 竞足-混合 */
	public static final String JZ_HH = "hhgg";
	
	/** 竞足-玩法 */
	public static final String JZ_PLAY = "jz_play";
	
	/** 竞篮-玩法 */
	public static final String JL_PLAY = "jl_play";
	
	/** 竞彩可选最大场数 */
	public static int Max_Able_Selects = 15;
	
    /** 过关方式对应最小个数 */
    public static HashMap<String, String> ClearanceList = new HashMap<String, String>();
    
	/** 主胜(3)、平(1)、负(0) */
	public static final String host_win = "3";
	public static final String host_ping = "1";
	public static final String host_loss = "0";
	
	/** 篮球大小分 用于jni预算奖金 */
	public static final String host_df = "2";
	public static final String host_xf = "1";
	
	public static final String  fu = "负";
	public static final String  ping = "平";
	public static final String  sheng = "胜";
	
	/** 足球投注项分隔符，例如：3/1 */
	public static final String ZqBetConItem = "/";
	public final static String CONTENTSPLITEFLAG_VerticalLine = "|";
	
	/** 足球最大注数 */
	public static final int JZ_MaxZhuShu = 10000;
	
	/** 球的颜色 */
	public static final int BALL_COLOR_ORANGE = 0;// 橙色
	public static final int BALL_COLOR_RED = 1;// 红色
	public static final int BALL_COLOR_BULE = 2;// 蓝色
	public static final int BALL_COLOR_NO = 3;// 无色
	
	/**
	 * 玩法
	 */
	public static final String BET_TYPE_DEFAULE = "";// 默认玩法（没有玩法的时候用这个）

	/** 选号结果 */
	public static final int msg_ball = 10;	/** 福彩3D复式玩法 */
	public static final String FC3D_DUPLEX_PLAY = "7_2";
	/** 福彩3D单式玩法 */
	public static final String FC3D_SIMPLEX_PLAY = "7_1";
	/** 福彩3D组3玩法 */
	public static final String FC3D_Z3_PLAY = "10_2";
	/** 福彩3D组6玩法 */
	public static final String FC3D_Z6_PLAY = "9_2";
	/** 福彩3D组3和值玩法 */
    public static final String FC3D_Z3HZ_PLAY = "15_2";
    /** 福彩3D组6和值玩法 */
    public static final String FC3D_Z6HZ_PLAY = "14_2";
	
    /** 排列三组3和值玩法 */
    public static final String PL3_Z3HZ_PLAY = "32_2";
    /** 排列三组6和值玩法 */
    public static final String PL3_Z6HZ_PLAY = "33_2";
    /** 排列三组复式玩法 */
	public static final String PL3_DUPLEX_PLAY = "3_2";
	/** 排列三单式玩法 */
	public static final String PL3_SIMPLEX_PLAY = "3_1";
	
	public static final String CzTipsJJ = "1", CzTipsHot = "2", CzTipsStop = "3", CzTipsHide = "4", CzTipsKj = "5";
	
	public static final String SpFileName = "777zhongSp";

	/** 单关对阵标识 */
	public static final String Jczq_Match_Dg = "1";
	/** 单关默认最小倍数 */
	public static final int DefaultMultleDg  = 5;
	
	public static final String SP_JC = "sp_jc";//竞彩
    public static final String Key_Dg_Tips = "dg_tips_appear";//竞彩单关提示浮层
    public static final String Key_History_Tips = "dg_tips_history";//竞彩提示浮层
    
	/** 竞彩篮球过关方式最大值--胜分差 */
	public static int JCLQ_Clearance_SFC_MAX = 4;
	
	/** 竞彩篮球直播状态:-1  完；0   未；1   第1节；2   第2节；3   第3节；4   第4节；5   第1'OT；6   第2'OT；7   第3'OT；50  中场；-2  待定；-3   中断；-4   取消；-5  推迟 */
	public static final String Lq_State_ing_one = "1";
	public static final String Lq_State_ing_two = "2";
	public static final String Lq_State_ing_three = "3";
	public static final String Lq_State_ing_four = "4";
	public static final String Lq_State_ing_pause = "50";
	public static final String Lq_State_add_one = "5";
	public static final String Lq_State_add_two = "6";
	public static final String Lq_State_add_three = "7";
	public static final String Lq_State_un = "0"; 
	public static final String Lq_State_ed = "-1"; 
	public static final String Lq_State_cancal = "-4";
	public static final String Lq_State_daiding = "-2";
	public static final String Lq_State_tuic = "-5";
	public static final String Lq_State_zd = "-3";
	
    /** 0:未开,1:上半场,2:中场,3:下半场,4:加时,-10：取消,-11:待定,-12:腰斩,-13:中断,-14:推迟,-1:完场 */
	public static final String State_un_sever = "0"; 
	public static final String State_ing_up_sever = "1";
	public static final String State_ing_pause_sever = "2";
	public static final String State_ing_down_sever = "3";
	public static final String State_add_sever = "4";
	public static final String State_ed_sever = "-1"; 
	public static final String State_cancal_sever = "-10";
	public static final String State_daiding_sever = "-11";
	public static final String State_tuic_sever = "-14";
	public static final String State_yz_sever = "-12";
	public static final String State_zd_sever = "-13";
	
	/** 客户端使用 */
	public static final String State_ing_up = "03";
	public static final String State_ing_pause = "02";
	public static final String State_ing_down = "01";
	public static final String State_add = "04";
	public static final String State_un = "05"; 
	public static final String State_ed = "06"; 
	public static final String State_cancal = "07";
	public static final String State_daiding = "08";
	public static final String State_tuic = "09";
	public static final String State_yz = "10";
	public static final String State_zd = "11";
	
	public static Map<String, String> stateMap = new HashMap<String, String>();
	
	public static Map<String, String> state_client = new HashMap<String, String>();
	static{
		state_client.put(State_ing_up_sever, State_ing_up);
		state_client.put(State_ing_pause_sever, State_ing_pause);
		state_client.put(State_ing_down_sever, State_ing_down);
		state_client.put(State_add_sever, State_add);
		state_client.put(State_un_sever, State_un);
		state_client.put(State_ed_sever, State_ed);
		state_client.put(State_cancal_sever, State_cancal);
		state_client.put(State_daiding_sever, State_daiding);
		state_client.put(State_tuic_sever, State_tuic);
		state_client.put(State_yz_sever, State_yz);
		state_client.put(State_zd_sever, State_zd);
	}
	
    public final static String PARAM_PAGE_INFO = "param_page_info";
    
	/** 数字彩多行投注内容分隔符 */
	public static String digitBetConMulRowSplit = "\n\r";
	
	// 胜负彩14场
    public static int SFC14_MIN = 14;
    public static int SFC14_Item_MIN = 1;// 每个对阵至少一个结果
    
    public static int PRICE = 2;
    
    
    public static int MATCH_NOT_FINISH = 0;//比赛未结束
    public static int MATCH_FINISH = 1;//比赛结束
    public static int MATCH_CANCEL = 2;//比赛取消
    
    
    /**
     * 0 - 彩票中包含的期次都与比赛结果还未比对完
     * 1 - 彩票中包含的期次都与比赛结果都完成比对
     * 
     * */
    public static final String NOT_FINISH_COMPARE ="0";
    public static final String FINISH_COMPARE ="1";
    
    
    //投注提前时间，单位为秒
    public static final int BET_PRESET_TIME = 780;
  //彩票信息的缓存时长,单位分
    public final static long BET_INFO_EXPIRE_TIME = 10;
    
	/**
	 * 通用的表示 是和否的常量
	 */
	public final static String ONE_YES = "1";
	public final static String ZERO_NO = "0";
	
    //订单状态
    public static final Integer ORDER_STATUS_NOT_PAY = 0;         //待付款
    public static final Integer ORDER_STATUS_PAY_FAIL_LOTTERY = 1;//待出票
    public static final Integer ORDER_STATUS_FAIL_LOTTERY = 2;    //出票失败
    public static final Integer ORDER_STATUS_STAY = 3;            //待开奖
    public static final Integer ORDER_STATUS_NOT = 4;             //未中奖
    public static final Integer ORDER_STATUS_ALREADY = 5;         //已中奖
    public static final Integer ORDER_STATUS_REWARDING = 6;       //派奖中
    public static final Integer ORDER_STATUS_REWARDED = 7;        //已派奖
    
    public static final Integer PAY_STATUS_STAY = 0;   //待支付
    public static final Integer PAY_STATUS_ALREADY = 1;//已支付
	
    public static final int PRINT_STATUS_SUCCESS = 16;   //出票成功
    public static final int PRINT_STATUS_FAIL = 17;//出票失败
    public static final int PRINT_STATUS_PRINT = 8;//出票中
	
    
    //文章收藏
    //用户已收藏
    public static final String IS_COLLECTED = "1";
    
    //用户未收藏
    public static final String IS_NOT_COLLECT = "0";
    
    //咨询版
    public static final String INFO_VERSION = "1";
    
    //交易版
    public static final String DEAL_VERSION = "2";
}
