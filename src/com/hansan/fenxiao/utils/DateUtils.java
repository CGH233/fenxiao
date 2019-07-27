package com.hansan.fenxiao.utils;

public class DateUtils {
     
	 /*
	  * 将String类型的日期转换成mysql日期格式
	  */
     public static String toDate(String date){
    	 String hqlFormat = "%Y-%m-%d-%H-%h-%i";//mysql日期格式
    	 StringBuffer bf = new StringBuffer();
    	 bf.append("str_to_date('");
    	 bf.append(date);
    	 bf.append("','");
    	 bf.append(hqlFormat);
    	 bf.append("')");
    	 return bf.toString();
     }
}
