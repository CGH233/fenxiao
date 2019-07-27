package com.hansan.fenxiao.action;


import com.hansan.fenxiao.entities.Config;
import com.hansan.fenxiao.entities.Financial;
import com.hansan.fenxiao.entities.Orders;
import com.hansan.fenxiao.entities.Recharge;
import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.pay.alipay.AlipayConfig;
import com.hansan.fenxiao.pay.wxpay.MyWXPayConfig;
import com.hansan.fenxiao.pay.wxpay.WXPay;
import com.hansan.fenxiao.pay.wxpay.WXPayConstants.SignType;
import com.hansan.fenxiao.pay.wxpay.WXPayUtil;
import com.hansan.fenxiao.service.IConfigService;
import com.hansan.fenxiao.service.IFinancialService;
import com.hansan.fenxiao.service.IOrdersService;
import com.hansan.fenxiao.service.IRechargeService;
import com.hansan.fenxiao.service.IUserService;
import com.mysql.jdbc.log.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("wxpayAction")
@Scope("prototype")
public class WxpayAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Resource(name = "ordersService")
	private IOrdersService<Orders> ordersService;

	@Resource(name = "userService")
	private IUserService<User> userService;
	private Orders orders;
	private String ftlFileName;

	@Resource(name = "configService")
	private IConfigService<Config> configService;

	@Resource(name = "financialService")
	private IFinancialService<Financial> financialService;

	@Resource(name = "rechargeService")
	private IRechargeService<Recharge> rechargeService;


	/**
	 * 统一下单
     * 除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，
     * 返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
	 * @return void
	 * @throws Exception 
	 */
	public void wxpayApi() throws Exception{
		
		MyWXPayConfig config = new MyWXPayConfig();
        WXPay wxpay = new WXPay(config,config.getNotifyUrl(),config.getAutoReport(),config.getUseSandBox());//传入配置信息和异步通知地址
        
        //参考开发文档准备接口的请求参数
        //appid、mch_id、nonce_str、sign_type、sign、notify_url公共参数已统一封装
		//商户订单号，必填
		Random random = new Random();
		int n = random.nextInt(9999);
		n += 10000;
		String out_trade_no = "" + System.currentTimeMillis() + n ;
	    System.out.println("===========================支付宝下单，商户订单号为"+out_trade_no+"======================");
		//付款金额，必填
	    String total_fee = this.request.getParameter("money");
	    //商品描述，必填
	    String body = "Aiwac分销商城-在线充值";
	    //终端ip，必填 支持IPV4和IPV6两种格式
	    String spbill_create_ip = "";
	    //交易类型，必填
	    String trade_type = "JSAPI";
	    
	    Map<String,String> data = new HashMap<String,String>();
	    data.put("body", body);
	    data.put("out_trade_no", out_trade_no);
	    data.put("total_fee", total_fee);
	    data.put("spbill_create_ip", spbill_create_ip);
	    data.put("trade_type", trade_type);
	    
	    try {
            Map<String, String> resp = wxpay.unifiedOrder(data);//微信支付返回预付单信息
            System.out.println("=====================预付单信息:");
            System.out.println(resp);
            //预支付交易会话标识
            Map<String,String> params = new HashMap<String,String>();
            JSONObject json = new JSONObject();
            params.put("appId", config.getAppID());
            params.put("timeStamp",""+WXPayUtil.getCurrentTimestamp());
            params.put("nonceStr",WXPayUtil.generateNonceStr());
            params.put("prepay_id", resp.get("prepay_id"));
            SignType signType;
            if(config.getUseSandBox()){
            	params.put("signType","MD5");
            	signType = SignType.MD5;
            }else{
            	params.put("signType","HMAC-SHA256");
            	signType = SignType.HMACSHA256;
            }
            params.put("paySign",WXPayUtil.generateSignature(data, config.getKey(), signType));
            this.response.getWriter().write(params.toString());
            this.response.getWriter().flush();
            this.response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	   
	}
	
	/**
	 * 微信异步通知接口
	 * @return void
	 * @throws Exception 
	 */
	public void notifyUrl() throws Exception{
		MyWXPayConfig conf = new MyWXPayConfig();
		//获取返回参数
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = this.request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		System.out.println("微信支付异步通知结果：");
		System.out.println(params);
		
		//返回状态码
		String return_code = this.request.getParameter("return_code");//为SUCCESS时才有以下字段返回
		//业务结果
		String result_code = this.request.getParameter("result_code");
		//错误代码
		String err_code = this.request.getParameter("err_code");
		//错误信息
		String err_code_des = this.request.getParameter("err_code_des");	
		//商户单号
		String out_trade_no = this.request.getParameter("out_trade_no");
		//交易金额
		String total_fee = this.request.getParameter("total_fee");
		//商户号
		String mch_id = this.request.getParameter("mch_id");
		//签名类型
		String sign_type = this.request.getParameter("sign_type");
		SignType signType;
		if(sign_type.equals("MD5"))
			signType=SignType.MD5;
		else
			signType=SignType.HMACSHA256;
		
		//验签
		boolean verify_result =  WXPayUtil.isSignatureValid(params, conf.getKey(), signType);	
		
		if(verify_result&&return_code.equals("SUCCESS")){
			Recharge findRecharge = this.rechargeService.findByNo(out_trade_no);
			if(findRecharge!=null&&Double.parseDouble(total_fee)==findRecharge.getMoney()&&mch_id.equals(conf.getMchID())){
				if(findRecharge.getStatus()==Integer.valueOf(0)){
					//订单未处理
					User findUser = this.userService.findById(User.class, findRecharge.getUser().getId().intValue());
					switch(result_code){
					case "FAIL":
						//交易失败
						findRecharge.setStatus(Integer.valueOf(2));						
						break;
					case "SUCCESS":	
						
						//修改数据库记录
						Double balance = findUser.getBalance().doubleValue() + Double.parseDouble(total_fee);
						findUser.setBalance(Double.valueOf(balance)); 
						this.userService.saveOrUpdate(findUser);
						findRecharge.setStatus(Integer.valueOf(1));
						this.rechargeService.saveOrUpdate(findRecharge);
						
						//添加财务明细
						Financial financial1 = new Financial();
						financial1.setNo(out_trade_no);
						financial1.setType(Integer.valueOf(0));
						financial1.setCreateDate(new Date());
						financial1.setDeleted(false);
						financial1.setMoney(Double.valueOf(total_fee));
						financial1.setBalance(findUser.getBalance());
						financial1.setPayment("微信付款");
						financial1.setRemark("在线充值");
						financial1.setUser(findUser);
						financial1.setOperator(findUser.getName());
						this.financialService.saveOrUpdate(financial1);
						break;
				     }
					System.out.println("err_code:"+err_code+","+err_code_des);
					System.out.println("=====================订单处理完成=======================");
			      }else{
			    	  System.out.println("=====================已处理过的订单=======================");
			      }
				
			     //收到正确支付结果通知后，返回参数给微信支付.格式严格不能更改
			     Map<String,String> return_params = new HashMap<String,String>();
			     return_params.put("return_code", "SUCCESS");
			     return_params.put("return_msg", "OK");
			     String resXml = WXPayUtil.mapToXml(return_params);
			     this.response.setContentType("text/xml;charset=UTF-8"); 
			     this.response.getWriter().write(resXml);
			     this.response.getWriter().flush();
			     this.response.getWriter().close();
		   }else{
			   System.out.println("错误通知：交易订单不匹配,请忽略");
               
		   }
		}else{
			   System.out.println("=================================错误通知：验签失败========================");
		}
		
	}
    
	/**
	 * 查询订单
	 * @return void
	 * @param out_trade_no商户订单号
	 * @throws Exception 
	 */
	public void wxQueryOrder(String out_trade_no, String signType) throws Exception {
		MyWXPayConfig conf = new MyWXPayConfig();
		WXPay wxpay = new WXPay(conf,conf.getNotifyUrl(),conf.getAutoReport(),conf.getUseSandBox());//传入配置信息和异步通知地址
		//请求参数
		Map<String,String> params = new HashMap<String, String>();
		params.put("out_trade_no", out_trade_no);
		//发送请求
		Map<String,String> resp = wxpay.orderQuery(params);
		
		this.response.getWriter().write(resp.toString());
		this.response.getWriter().flush();
		this.response.getWriter().close();
	}
	
	public Orders getOrders() {
		return this.orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public String getFtlFileName() {
		return this.ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}
}

/*
 * Location: D:\360��ȫ���������\WeFenxiao_A5\WeFenxiao_V1.0.1\WEB-INF\classes\
 * Qualified Name: com.hansan.fenxiao.action.AlipayAction JD-Core Version: 0.6.0
 */