package com.hansan.fenxiao.wechat;

public class WechatConstant {
	
	//测试号，待替换
    public static String AppId = "wx9222289d6f0f7242";
    public static String AppSecret = "b7da37a85cf12b7fdc40a3bd16c060e3";
    
    //本地域名，待替换为服务器域名
    public static String WechatURL = "https://api.weixin.qq.com";
    public static String MENUURL = WechatURL + "/cgi-bin/menu/";
    
    //商城接入网址
    public static String FenxiaoUrl = "http://aiwac.net/fenxiao/index.jsp";
    
	
	String ACCESS_TOKEN;
	
	public void setAccessToken(String token){
		this.ACCESS_TOKEN=token;
	}
	
	public String getAccessToken(){
		return this.ACCESS_TOKEN;
	}
}
