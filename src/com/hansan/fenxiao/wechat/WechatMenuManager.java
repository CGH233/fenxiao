package com.hansan.fenxiao.wechat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WechatMenuManager {

	/**
	 * 创建菜单
	 */
	public static void createMenu(){
		String url = WechatConstant.MENUURL+"/create";
		
		JSONObject json = new JSONObject();		
		JSONArray buttons = new JSONArray();	
		JSONObject button1 = new JSONObject();
		JSONObject button2 = new JSONObject();
		JSONObject button3 = new JSONObject();
		try {
			//菜单1 
			button1.put("name", "菜单1");
			JSONObject subbutton1 = new JSONObject();
			subbutton1.put("name", "子菜单1");
			subbutton1.put("type", "view");
			subbutton1.put("url", WechatConstant.FenxiaoUrl);
			button1.put("sub_button", subbutton1);
			buttons.put(button1);
			//菜单2
			button2.put("name", "菜单2");
			JSONObject subbutton2 = new JSONObject();
			subbutton2.put("name", "子菜单1");
			subbutton2.put("type", "view");
			subbutton2.put("url", WechatConstant.FenxiaoUrl);
			button2.put("sub_button", subbutton2);
			buttons.put(button2);
			//菜单3
			button3.put("name", "商城");
			button3.put("type", "view");
			button3.put("url", WechatConstant.FenxiaoUrl);
			buttons.put(button3);
			json.put("button", buttons);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String arg[]){
		createMenu();
	}
}
