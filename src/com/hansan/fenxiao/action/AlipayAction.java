package com.hansan.fenxiao.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.hansan.fenxiao.entities.Config;
import com.hansan.fenxiao.entities.Financial;
import com.hansan.fenxiao.entities.Orders;
import com.hansan.fenxiao.entities.Recharge;
import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.entities.Withdraw;
import com.hansan.fenxiao.pay.alipay.AlipayConfig;
import com.hansan.fenxiao.service.IConfigService;
import com.hansan.fenxiao.service.IFinancialService;
import com.hansan.fenxiao.service.IOrdersService;
import com.hansan.fenxiao.service.IRechargeService;
import com.hansan.fenxiao.service.IUserService;
import com.hansan.fenxiao.service.IWithdrawService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("alipayAction")
@Scope("prototype")
public class AlipayAction extends BaseAction {
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
	
	@Resource(name = "withdrawService")
	private IWithdrawService<Withdraw> withdrawService;

//	public String alipayApi() throws Exception {
//		String payment_type = "1";
//
//		String notify_url = this.request.getScheme() + "://" + this.request.getServerName() + ":" + this.request.getServerPort()
//				+ this.request.getContextPath() + "/" + "notifyUrl";
//
//		String return_url = this.request.getScheme() + "://" + this.request.getServerName() + ":" + this.request.getServerPort()
//				+ this.request.getContextPath() + "/" + "returnUrl";
//
//		Random random = new Random();
//		int n = random.nextInt(9999);
//		n += 10000;
//		String out_trade_no = "" + System.currentTimeMillis() + n ;
//
//		String subject = out_trade_no;
//
//		String money = this.request.getParameter("money");
//
//		String body = out_trade_no;
//
//		String show_url = this.request.getScheme() + "://" + this.request.getServerName() + ":" + this.request.getServerPort()
//				+ this.request.getContextPath() + "/";
//
//		String anti_phishing_key = "";
//
//		String exter_invoke_ip = "";
//
//		String enable_paymethod = this.request.getParameter("enable_paymethod");
//		Config findConfig = (Config) this.configService.findById(Config.class, 1);
//
//		Map<String,String> sParaTemp = new HashMap<String,String>();
//		sParaTemp.put("service", "create_direct_pay_by_user");
//		sParaTemp.put("partner", findConfig.getAlipayPartner());
//		sParaTemp.put("optEmail", findConfig.getAlipaySellerEmail());
//		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
//		sParaTemp.put("payment_type", payment_type);
//		sParaTemp.put("notify_url", notify_url);
//		sParaTemp.put("return_url", return_url);
//		sParaTemp.put("out_trade_no", out_trade_no);
//		sParaTemp.put("subject", subject);
//		sParaTemp.put("total_fee", money);
//		sParaTemp.put("body", body);
//		sParaTemp.put("show_url", show_url);
//		sParaTemp.put("anti_phishing_key", anti_phishing_key);
//		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
//		sParaTemp.put("enable_paymethod", "debitCardExpress");
//
//		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认", findConfig.getAlipayKey());
//		
//		HttpSession session = this.request.getSession();
//		User loginUser = (User) session.getAttribute("loginUser");
//		Recharge recharge = new Recharge();
//		recharge.setNo(out_trade_no);
//		recharge.setMoney(Double.valueOf(Double.parseDouble(money)));
//		recharge.setUser(loginUser);
//		recharge.setStatus(Integer.valueOf(0));
//
//		recharge.setCreateDate(new Date());
//		this.rechargeService.saveOrUpdate(recharge);
//
//		PrintWriter out = this.response.getWriter();
//		out.println(sHtmlText);
//		out.flush();
//		out.close();
//		return null;
//	}
	/**
	 * 生成订单信息并调用支付宝SDK发送支付请求
	 * 支付宝接受请求后为开发者生成前台页面请求需要的完整form表单的html
	 * @return void
	 */
	public void alipayApi(){
		
		//商户订单号，必填
		Random random = new Random();
		int n = random.nextInt(9999);
		n += 10000;
		String out_trade_no = "" + System.currentTimeMillis() + n ;
	    System.out.println("===========================支付宝下单，商户订单号为"+out_trade_no+"======================");
	    //订单名称，必填
		String subject = "在线充值";
		//付款金额，必填
	    String total_amount = this.request.getParameter("money");
	    //商品描述，可空
	    String body = "Aiwac分销商城-在线充值";
	    // 超时时间 可空
	    String timeout_express="2m";
	    // 销售产品码，商家和支付宝签约的产品码，必填。该产品请填写固定值：QUICK_WAP_WAY
	    String product_code="QUICK_WAP_WAY";
	    /**********************/
	    // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
	    //调用RSA签名方式
	    AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
	    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();//使用alipay.trade.wap.pay.return接口
	   
	    
	    // 封装请求支付信息
	    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
	    model.setOutTradeNo(out_trade_no);
	    model.setSubject(subject);
	    model.setTotalAmount(total_amount);
	    model.setBody(body);
	    model.setTimeoutExpress(timeout_express);
	    model.setProductCode(product_code);
	    alipay_request.setBizModel(model);
	    // 设置异步通知地址
	    alipay_request.setNotifyUrl(AlipayConfig.notify_url);
	    // 设置同步地址
	    alipay_request.setReturnUrl(AlipayConfig.return_url);   
	    
	    HttpSession session = this.request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		Recharge recharge = new Recharge();
		recharge.setNo(out_trade_no);
		recharge.setMoney(Double.valueOf(Double.parseDouble(total_amount)));
		recharge.setUser(loginUser);
		recharge.setStatus(Integer.valueOf(0));

		recharge.setCreateDate(new Date());
		this.rechargeService.saveOrUpdate(recharge);
	    
	    // form表单生产
	    String form = "";
		try {
			// 调用SDK生成表单
			AlipayTradeWapPayResponse alipayResponse=client.pageExecute(alipay_request);//发送请求
			if(alipayResponse.isSuccess()){
				System.out.println("支付接口调用成功");
				form = alipayResponse.getBody(); //获取前台需要的form表单
				System.out.println(form);
				this.response.setContentType("text/html;charset=" + AlipayConfig.CHARSET); 
			    //response.getWriter().write(form);//直接将完整的表单html输出到页面 
				this.response.getWriter().print(form);//直接将完整的表单html输出到页面 
				this.response.getWriter().flush(); 
				this.response.getWriter().close();
			}else{
				System.out.println("支付接口调用失败!");
			}
			
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
//
//	public String notifyUrl() throws Exception {
//		PrintWriter out = this.response.getWriter();
//
//		Map params = new HashMap();
//		Map requestParams = this.request.getParameterMap();
//		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
//			String name = (String) iter.next();
//			String[] values = (String[]) requestParams.get(name);
//			String valueStr = "";
//			for (int i = 0; i < values.length; i++) {
//				valueStr = valueStr + values[i] + ",";
//			}
//
//			params.put(name, valueStr);
//		}
//
//		String out_trade_no = new String(this.request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
//
//		String trade_no = new String(this.request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
//
//		String trade_status = new String(this.request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
//
//		Config findConfig = (Config) this.configService.findById(Config.class, 1);
//		if (AlipayNotify.verify(params, findConfig.getAlipayKey())) {
//			if (trade_status.equals("TRADE_FINISHED")) {
//				Recharge findRecharge = this.rechargeService.findByNo(out_trade_no);
//				if (findRecharge.getStatus().intValue() == 0) {
//					findRecharge.setStatus(Integer.valueOf(1));
//					this.rechargeService.saveOrUpdate(findRecharge);
//					User findUser = (User) this.userService.findById(User.class, findRecharge.getUser().getId().intValue());
//					findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() + findRecharge.getMoney().doubleValue()));
//					this.userService.saveOrUpdate(findUser);
//				}
//
//			} else if (trade_status.equals("TRADE_SUCCESS")) {
//				Recharge findRecharge = this.rechargeService.findByNo(out_trade_no);
//				if (findRecharge.getStatus().intValue() == 0) {
//					findRecharge.setStatus(Integer.valueOf(1));
//					this.rechargeService.saveOrUpdate(findRecharge);
//					User findUser = (User) this.userService.findById(User.class, findRecharge.getUser().getId().intValue());
//					findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() + findRecharge.getMoney().doubleValue()));
//					this.userService.saveOrUpdate(findUser);
//				}
//
//			}
//
//			out.println("success");
//		} else {
//			out.println("fail");
//		}
//		out.flush();
//		out.close();
//		return null;
////	}
	
	/**
	 * 支付结果异步通知接口
	 * @throws Exception 
	 */
	public void notifyUrl() throws Exception {
		
		//获取支付宝POST过来反馈信息
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
		
	    System.out.println("支付宝异步通知结果："+params.toString());
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//原支付请求的商户交易号
		String out_trade_no = new String(this.request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		//交易金额
		String total_amount = new String(this.request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
		//卖家支付宝账号
		String seller_email = new String(this.request.getParameter("seller_email").getBytes("ISO-8859-1"),"UTF-8");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//使用SDK验签，计算得出通知验证结果
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
		
		PrintWriter out=null;
		try {
			out = this.response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(verify_result){//验证成功
			Recharge findRecharge = this.rechargeService.findByNo(out_trade_no);
		
			if(findRecharge != null && Double.parseDouble(total_amount)==findRecharge.getMoney()
					&& seller_email.equals(AlipayConfig.seller_email)){				
				if(findRecharge.getStatus()==Integer.valueOf(0)){
					//订单未处理
					User findUser = this.userService.findById(User.class, findRecharge.getUser().getId().intValue());
					switch(trade_status){
					case "WAIT_BUYER_PAY":
						//交易创建，等待买家付款
						break;
					case "TRADE_CLOSED":
						//未付款交易超时关闭，或支付完成后全额退款
						findRecharge.setStatus(Integer.valueOf(2));
						this.rechargeService.saveOrUpdate(findRecharge);				
						break;
					case "TRADE_SUCCESS":
						//交易支付成功
						//注意：
					    //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
					case "TRADE_FINISHED":	
						//交易结束，不可退款
						//注意：
						//如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
						//如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
						
						//修改数据库记录
						Double balance = findUser.getBalance().doubleValue() + Double.parseDouble(total_amount);
						findUser.setBalance(Double.valueOf(balance)); 
						this.userService.saveOrUpdate(findUser);
						findRecharge.setStatus(Integer.valueOf(1));
						this.rechargeService.saveOrUpdate(findRecharge);
						
						//添加财务明细
						Financial financial = new Financial();
						financial.setNo(out_trade_no);
						financial.setType(Integer.valueOf(1));
						financial.setCreateDate(new Date());
						financial.setDeleted(false);
						financial.setMoney(Double.valueOf(total_amount));
						financial.setBalance(findUser.getBalance());
						financial.setPayment("支付宝付款");
						financial.setRemark("在线充值");
						financial.setUser(findUser);
						financial.setOperator(findUser.getName());
						this.financialService.saveOrUpdate(financial);
						break;
					}
					System.out.println("============================订单处理完成===================");
				}
				
			 
				
			}else{
				System.out.println("异常通知，请忽略!");
		   
			}
				
			out.println("success");	//反馈给支付宝，请不要修改或删除

		}else{//验证失败
			System.out.println("==============================验签失败!==============================");
			out.println("fail");
			
		}
		
	}
	
	/**
	 * 支付宝订单查询
	 * @param
	 * @return void
	 * @throws Exception 
	 */
	public void aliQuery() throws Exception {
		if(this.request.getParameter("WIDout_trade_no")!=null||this.request.getParameter("WIDtrade_no")!=null){
			 //商户订单号，商户网站订单系统中唯一订单号，必填
			 String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			 //支付宝交易号
			 String trade_no = new String(request.getParameter("WIDtrade_no").getBytes("ISO-8859-1"),"UTF-8");
			 /**********************/
			 // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
			 AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
			 AlipayTradeQueryRequest alipay_request = new AlipayTradeQueryRequest();
			 
			 AlipayTradeQueryModel model=new AlipayTradeQueryModel();
		     model.setOutTradeNo(out_trade_no);
		     model.setTradeNo(trade_no);
		     alipay_request.setBizModel(model);
		     
		     AlipayTradeQueryResponse alipay_response =client.execute(alipay_request);
		     System.out.println(alipay_response.getBody());
		 }
	}

	/**
	 * 支付宝单笔转账接口(实时提现)
	 * @param
	 * @return void
	 */
	public void withDraw(){
		
		
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		 
		//商户转账订单号,必填
		Random random = new Random();
		int n = random.nextInt(9999);
		n += 10000;
		String out_biz_no = "" + System.currentTimeMillis() + n ;
		//收款方账户类型，必填
		String payee_type = "ALIPAY_LOGONID";//支付宝登录号，支持邮箱和手机号格式
		//收款方账户，必填
		String payee_account = this.request.getParameter("payee_account");
		//转账金额，必填
		String amount = this.request.getParameter("amount");
		//付款方姓名,显示在收款方的账单详情页，可选
		String payer_show_name = "Aiwac";
		//收款方真实姓名,可选，若不为空则会校验账户真实姓名是否一致
		String payee_real_name = this.request.getParameter("real_name");
		//转账备注，可选 。当付款方为企业账户，且转账金额达到（大于等于）50000元，remark不能为空
		String remark = "佣金提现";
		
		//手机号
		String phone = this.request.getParameter("phone");
		request.setBizContent("{" +
				"\"out_biz_no\":\""+out_biz_no+"\"," +
				"\"payee_type\":\""+payee_type+"\"," +
				"\"payee_account\""+payee_account+"\"," +
				"\"amount\":\""+amount+"\"," +
				"\"payer_show_name\":\""+payer_show_name+"\"," +
				"\"payee_real_name\":\""+payee_real_name+"\"," +
				"\"remark\":\""+remark+"\"" +
				"  }");
		
		HttpSession session = this.request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		User findUser = userService.getUserByName(loginUser.getName());
		Withdraw withdraw = new Withdraw();
		//withdraw.setNo(out_biz_no);
		withdraw.setMoney(Double.valueOf(amount));
		withdraw.setBank("支付宝");
		withdraw.setBankName(payee_real_name);
		withdraw.setBankNo(payee_account);
		withdraw.setPhone(phone);
		withdraw.setUser(loginUser);
		withdraw.setCreateDate(new Date());
		withdraw.setStatus(Integer.valueOf(0));
	    this.withdrawService.saveOrUpdate(withdraw);
		
		AlipayFundTransToaccountTransferResponse response;
		try {
			response = client.execute(request);
			if(response.isSuccess()){
				System.out.println("调用成功");
				//获取支付宝POST过来反馈信息
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
				
			    System.out.println("支付宝提现结果："+params.toString());
			    
			    if(this.request.getParameter("code").equals("10000")){			    	
			    	String out_biz_no_req = this.request.getParameter("out_biz_no_req");
			    	Withdraw withdraw1 = this.withdrawService.findByNo(out_biz_no_req); 
			    	if(withdraw1!=null&&out_biz_no_req == out_biz_no){
			    		//提现成功
			    	    withdraw1.setStatus(Integer.valueOf(1));
			    	    this.withdrawService.saveOrUpdate(withdraw);
			    	   
			    	    Double commission = findUser.getCommission() - Double.parseDouble(amount);		    	  
			    	    findUser.setCommission(commission);
			    	    this.userService.saveOrUpdate(findUser);
			    	    
			    	    //添加财务明细
						Financial financial = new Financial();
						financial.setNo(out_biz_no);
						financial.setType(Integer.valueOf(0));
						financial.setCreateDate(new Date());
						financial.setDeleted(false);
						financial.setMoney(Double.valueOf(amount));
						financial.setBalance(findUser.getBalance());
						financial.setPayment("提现");
						financial.setRemark("提现-支付宝");
						financial.setUser(findUser);
						financial.setOperator(findUser.getName());
						this.financialService.saveOrUpdate(financial);
			    	}
			    }
			} else {
				System.out.println("===========================接口调用失败======================");
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
//	public String returnUrl() throws Exception {
//		PrintWriter out = this.response.getWriter();
//
//		Map params = new HashMap();
//		Map requestParams = this.request.getParameterMap();
//		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
//			String name = (String) iter.next();
//			String[] values = (String[]) requestParams.get(name);
//			String valueStr = "";
//			for (int i = 0; i < values.length; i++) {
//				valueStr = valueStr + values[i] + ",";
//			}
//
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//			params.put(name, valueStr);
//		}
//
//		String out_trade_no = new String(this.request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
//
//		String trade_status = new String(this.request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
//		Config findConfig = (Config) this.configService.findById(Config.class, 1);
//
//		boolean verify_result = AlipayNotify.verify(params, findConfig.getAlipayKey());
//
//		if (verify_result) {
//			Recharge findRecharge = this.rechargeService.findByNo(out_trade_no);
//
//			if ((trade_status.equals("TRADE_FINISHED")) || (trade_status.equals("TRADE_SUCCESS"))) {
//				if (findRecharge.getStatus().intValue() == 0) {
//					findRecharge.setStatus(Integer.valueOf(1));
//					this.rechargeService.saveOrUpdate(findRecharge);
//					User findUser = (User) this.userService.findById(User.class, findRecharge.getUser().getId().intValue());
//					findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() + findRecharge.getMoney().doubleValue()));
//					this.userService.saveOrUpdate(findUser);
//				}
//
//			}
//
//		      out.println("<br>交易成功!<br>订单号:" + out_trade_no + "<br>支付金额:" + findRecharge.getMoney());
//		} else {
//			out.println("验证失败");
//		}
//		out.flush();
//		out.close();
//		return null;
//	}

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