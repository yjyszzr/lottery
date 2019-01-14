//package com.dl.shop.base.tj;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.dl.shop.base.tj.entity.TOrderEntity;
//import com.dl.shop.base.tj.entity.TUserEntity;
//import com.dl.shop.lottery.configurer.DataBaseCfg;
//
//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//
//public class Tj {
//
//	public Tj() {
//		
////		test();
//		List<String> rList = testXml();
//		showList(rList);
//		for(String str: rList) {
//			updateUserSuperWhite(str);
//		}
//	}
//
//	private void updateUserSuperWhite(String str) {
//		DataBaseCfg cfg = new DataBaseCfg();
//		//jdbc:mysql://localhost:3306/jewel
////		cfg.setUrl("jdbc:mysql://172.17.0.100:3306/cxm_app");
////		cfg.setUserName("cxm_user_admin");
////		cfg.setUserPass("mwkQag0MNtF1");
//		
////		cfg.setUrl("jdbc:mysql://62.234.222.65:3306/store");
////		cfg.setUserName("hanghang_db");
////		cfg.setUserPass("hanghangabc2o18!");
//		
//		cfg.setDriver("com.mysql.jdbc.Driver");
//		
//		TUserEntity userEntity = new TUserEntity();
//		userEntity.mobile = str;
//		
//		TUserImple userImple = new TUserImple(cfg);
//		int cnt = userImple.updateUserInfo(userEntity);
//		System.out.println("mobile:" + str + " 设置为白名单:" + cnt);
//	}
//	
//	private void showList(List<String> mList) {
//		System.out.println("===================");
//		for(String str : mList) {
//			System.out.println(str);
//		}
//		System.out.println("===================");
//	}
//	
//	private List<String> testXml() {
//		List<String> rList = new ArrayList<String>();
//		try{     
//            Workbook book  =  Workbook.getWorkbook(new File("c://白名单20190114.xls" ));     
//             //  获得第一个表格对象      
//             Sheet sheet  =  book.getSheet( 0 );     
//             //拿到表格的行数  
//             int row = sheet.getRows();  
//             //拿到表格的列数  
//             int col = sheet.getColumns();  
//             System.out.println("行："+ row);  
//             System.out.println("列："+ col);  
//            //用二维数组保存表格的数据  
//            String[][] result = new String[row][col];  
//              
//            //遍历表格拿到表格数据  
//            for(int i =0;i<row;i++)  
//                for(int j=0;j<col;j++){  
//                   Cell cell =  sheet.getCell(j,i);  
//                   result[i][j] = cell.getContents();  
//                  
//            }  
//            //遍历二维数组输出 到控制台  
//            for(int i =0;i<row;i++){  
//                for(int j=0;j<col;j++){  
////                  System.out.print(result[i][j]+"\t");  
//                  rList.add(result[i][j]);
//                }
////                System.out.println();  
//            }  
//              
//            book.close();     
//        } catch  (Exception e)   {     
//          //System.out.println(e);     
//        	e.printStackTrace();  
//        }     
//		return rList;
//	}
//	
//	private void test() {
//		DataBaseCfg cfg = new DataBaseCfg();
//		//jdbc:mysql://localhost:3306/jewel
//		cfg.setUrl("jdbc:mysql://172.17.0.100:3306/cxm_app");
//		cfg.setUserName("cxm_user_admin");
//		cfg.setUserPass("mwkQag0MNtF1");
//		cfg.setDriver("com.mysql.jdbc.Driver");
//		TOrderImple orderImple = new TOrderImple(cfg);
//		List<TOrderEntity> list = orderImple.listAll();
//		System.out.println("list.size:" + list.size());
//		TOrderDetailImple detailImple = new TOrderDetailImple(cfg);
//		for(TOrderEntity order : list) {
//			String orderSn = order.orderSn;
//			int count = detailImple.listByOrderSn(orderSn);
//			if(count > 8) {
//				System.out.println("order sn:" + orderSn + " 赛场:" + count + " 金额:" + order.moneyPaid.floatValue());
//			}
//		}
//	}
//	
////	public static void main(String[] args) {
////		new Tj();
////	}
//}
