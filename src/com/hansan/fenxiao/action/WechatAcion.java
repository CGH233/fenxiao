package com.hansan.fenxiao.action;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hansan.fenxiao.utils.SHA1;
import com.hansan.fenxiao.wechat.WechatConstant;

/**
 * 处理微信公众号的消息和事件
 * @author xinyu
 * @date 2019年7月5日
 */
@Controller("wechatAction")
@Scope("prototype")
public class WechatAcion extends BaseAction{
	private static final long serialVersionUID = 1L;

	/**
	 * 公众号接入
	 */
	public void wechatValidate() throws NoSuchAlgorithmException, IOException{
		System.out.println(this.request.getParameterMap().toString());
		 String signature = this.request.getParameter("signature");
		 String timestamp = this.request.getParameter("timestamp");
		 String nonce = this.request.getParameter("nonce");
		 String echostr = this.request.getParameter("echostr");
		 String token = "asdfghjkl";
		//1. 将token、timestamp、nonce三个参数进行字典序排序
         String[] arr = {timestamp,nonce,token};
         Arrays.sort(arr);
         //2. 将三个参数字符串拼接成一个字符串进行sha1加密
         StringBuilder sb = new StringBuilder();
         for (String temp : arr) {
            sb.append(temp);
         }
         //3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         if(SHA1.sha1(sb.toString()).equals(signature)){
             //接入成功
        	 this.response.getWriter().write(echostr);
        	 this.response.getWriter().flush();
        	 this.response.getWriter().close();
         }
       
	}
	
	
	
	
}
