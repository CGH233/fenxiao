 package com.hansan.fenxiao.action;
 
 import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.entities.Config;
 import com.hansan.fenxiao.entities.Financial;
 import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.service.IBounsRuleService;
import com.hansan.fenxiao.service.IConfigService;
 import com.hansan.fenxiao.service.IFinancialService;
 import com.hansan.fenxiao.service.IUserService;
 import com.hansan.fenxiao.utils.BjuiJson;
 import com.hansan.fenxiao.utils.BjuiPage;
 import com.hansan.fenxiao.utils.FreemarkerUtils;
 import com.hansan.fenxiao.utils.IpUtils;
 import com.hansan.fenxiao.utils.Md5;
import com.hansan.fenxiao.utils.VisualQRCode;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.hansan.fenxiao.utils.HttpURLConnection;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;  


import freemarker.template.Configuration;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
 import java.io.PrintWriter;
 import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import javax.annotation.Resource;
 import javax.servlet.RequestDispatcher;
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.json.JSONException;
import org.omg.CORBA.TCKind;
import org.springframework.context.annotation.Scope;
 import org.springframework.stereotype.Controller;
 
 @Controller("userAction")
 @Scope("prototype")
 public class UserAction extends BaseAction
 {
   private static final long serialVersionUID = 1L;
 
   @Resource(name="userService")
   private IUserService<User> userService;
 
   @Resource(name="configService")
   private IConfigService<Config> configService;
 
   @Resource(name="financialService")
   private IFinancialService<Financial> financialService;
   
   @Resource(name="bounsRuleService")
	private IBounsRuleService<BounsRule> bounsRuleService;
   
   private User user;
   private String ftlFileName;
 
   private HttpURLConnection httpGetConnection;
   
   public void list()
   {
     String key = this.request.getParameter("key");
     String countHql = "select count(*) from User where deleted=0";
     String hql = "from User where deleted=0";
     if (StringUtils.isNotEmpty(key)) {
       countHql = countHql + " and (name='" + key + "' or no='" + key + "' or phone='" + key + "')";
       hql = hql + " and (name='" + key + "' or no='" + key + "' or phone='" + key + "')";
     }
     hql = hql + " order by id desc";
 
     int count = 0;
     count = this.userService.getTotalCount(countHql, new Object[0]);
     this.page = new BjuiPage(this.pageCurrent, this.pageSize);
     this.page.setTotalCount(count);
     this.cfg = new Configuration();
 
     this.cfg.setServletContextForTemplateLoading(
       this.request.getServletContext(), "WEB-INF/templates/admin");
     List userList = this.userService.list(hql, this.page.getStart(), this.page.getPageSize(), new Object[0]);
     List<BounsRule> bounsRuleList = this.bounsRuleService.getBounsRuleList();
     Map root = new HashMap();
     root.put("userList", userList);
     root.put("bounsRuleList", bounsRuleList);
     root.put("page", this.page);
     root.put("key", key);
     FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
   }
 
   public void add()
   {
     this.cfg = new Configuration();
 
     this.cfg.setServletContextForTemplateLoading(
       this.request.getServletContext(), "WEB-INF/templates/admin");
     Map root = new HashMap();
     FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
   }
 
   //用户注册
   public void register()
   {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     String tuijianren = this.request.getParameter("tuijianren");
     String rePassword = this.request.getParameter("repassword");
     String province = this.request.getParameter("cmbProvince");
     String city = this.request.getParameter("cmbCity");
     String area = this.request.getParameter("cmbArea");
     String address = province+"|"+city+"|"+area;
    
     User tjrUser = this.userService.getUserByNo(tuijianren);
     if (tjrUser != null) tuijianren = tjrUser.getNo();
     JSONObject json = new JSONObject();
     json.put("status", "0");
     if (this.user == null) {
//       json.put("status", "0");
       json.put("message", "参数错误");
     } else if (this.user.getName().isEmpty()) {
//       json.put("status", "0");
       json.put("message", "姓名为空");
     } else if (this.user.getPhone().isEmpty()) {
//       json.put("status", "0");
       json.put("message", "手机号为空");
     }
     else if (this.user.getPassword().isEmpty()) {
//       json.put("status", "0");
       json.put("message", "密码为空");
     }else if (!this.user.getPassword().equals(rePassword)) {
//       json.put("status", "0");
       json.put("message", "两次密码输入不一致");
     }
     else if (this.userService.getUserByName(this.user.getName()) != null) {
//       json.put("status", "0");
       json.put("message", "账号已存在");
     }
       else if (this.userService.getUserByPhone(this.user.getPhone()) != null) {
//       json.put("status", "0");
       json.put("message", "手机号已存在");
     } 
//     else if (tjrUser == null) {
//       json.put("status", "0");
//       json.put("message", "推荐人不存在");
//     } 
     else {
    	 if (tjrUser == null){
    	     int arealevel = 4;
    	  	   List<User> aList = this.userService.list("from User where deleted = 0 and level = "+ arealevel);
    	  	   for (User user:aList) {
    	  		   if (user.getAddress().split("\\|")[2] == area) {
    	  			   tjrUser = user;
    	  			   tuijianren = user.getNo();
    	  		   }
    	  	   }    	   
         }
         if (tjrUser != null) {
        	if (tjrUser.getStatus().intValue() == 0) {
//           json.put("status", "0");
        		json.put("message", "推荐人未激活");
         } else if (tjrUser.getLevel() <= 1) {
//    	    	json.put("status", "0");
    	        json.put("message", "推荐人权限不足");
         } else {
           
           //上级关系转接，保证无上限
        
    	       if (StringUtils.isEmpty(tjrUser.getSuperior())) {
    	         this.user.setSuperior(";" + tuijianren + ";");
    	       }
    	//       else if (tjrUser.getSuperior().split(";").length > 3)
    	//         this.user.setSuperior(";" + tjrUser.getSuperior().split(";", 3)[2] + tuijianren + ";");
    	       else {
    	         this.user.setSuperior(tjrUser.getSuperior() + tuijianren + ";");
    	       } 
           }
         }
         try {
             String ip = IpUtils.getIpAddress(this.request);
             this.user.setRegisterIp(ip);
           } catch (Exception e) {
             this.user.setRegisterIp("0.0.0.0");
           }
           this.user.setPassword(Md5.getMD5Code(this.user.getPassword()));
           this.user.setLoginCount(Integer.valueOf(0));
           this.user.setStatus(Integer.valueOf(0));
           this.user.setBalance(Double.valueOf(0.0D));
           this.user.setCommission(Double.valueOf(0.0D));
           this.user.setDeleted(false);
           this.user.setCreateDate(new Date());
           this.user.setAddress(address);
           
           boolean res = this.userService.saveOrUpdate(this.user);
           if (res) {
             User loginUser = this.userService.getUserByName(this.user.getName());
     
             loginUser.setLoginCount(Integer.valueOf(loginUser.getLoginCount().intValue() + 1));
             try
             {
               String ip = IpUtils.getIpAddress(this.request);
               loginUser.setLastLoginIp(ip);
             } catch (Exception e) {
               loginUser.setLastLoginIp("0.0.0.0");
             }
             loginUser.setLastLoginTime(new Date());
             this.userService.saveOrUpdate(loginUser);
             HttpSession session = this.request.getSession();
             session.setAttribute("loginUser", loginUser);
             json.put("status", "1");
             json.put("message", "注册成功");
             } else {
//             json.put("status", "0");
             json.put("message", "注册失败，请重试");
           } 
     }
     try {
    	 
    	 this.request.setAttribute("status", json.get("status").toString());
		 this.request.setAttribute("message", json.get("message").toString());
    	 if (json.get("status") == "0") {	
    		 this.request.setAttribute("user", user);
    		 this.request.getRequestDispatcher("register.jsp").forward(this.request, this.response);
    	 }else {
    		 this.request.getRequestDispatcher("login.jsp").forward(this.request, this.response);
    	 }
 	} catch (ServletException | IOException e1) {
 		e1.printStackTrace();
 	}
  }
 
   public void info()
     throws JSONException
   {
     String idStr = this.request.getParameter("id");
     String callbackData = "";
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
 
     if ((idStr == null) || ("".equals(idStr))) {
       callbackData = BjuiJson.json("300", "参数不能为空", "", "", "", 
         "", "", "");
     } else {
       int id = 0;
       
       try {
         id = Integer.parseInt(idStr);
         
       }
       catch (Exception e) {
         callbackData = BjuiJson.json("300", "参数错误", "", "", "", 
           "", "", "");
       }
       User findUser = (User)this.userService.findById(
         User.class, id);
       if (findUser == null)
       {
         callbackData = BjuiJson.json("300", "用户不存在", "", "", 
           "", "", "", "");
       } else {
    	 
         this.cfg = new Configuration();
         List<BounsRule> bounsRuleList = this.bounsRuleService.getBounsRuleList();
         this.cfg.setServletContextForTemplateLoading(
           this.request.getServletContext(), 
           "WEB-INF/templates/admin");
         Map root = new HashMap();
         root.put("user", findUser);
         root.put("bounsRuleList", bounsRuleList);
         FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
       }
     }
     out.print(callbackData);
     out.flush();
     out.close();
   }
 
   public void update()
   {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     String callbackData = "";
     try {
    	 
       if (this.user == null) {
         callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", 
           "", "");
       } else {
         User findUser = (User)this.userService.findById(User.class, this.user.getId().intValue());
         if (StringUtils.isNotEmpty(this.user.getPassword())) {
           findUser.setPassword(Md5.getMD5Code(this.user.getPassword()));
         }
         findUser.setName(user.getName());
         findUser.setPhone(user.getPhone());
         findUser.setStatus(user.getStatus());
         findUser.setLevel(user.getLevel());
         //findUser.setStatusDate();
         boolean result = this.userService.saveOrUpdate(findUser);
 
         if (result) {
           callbackData = BjuiJson.json("200", "修改成功", "", 
             "", "", "true", "", "");
         }
         else
           callbackData = BjuiJson.json("300", "修改失败", "", 
             "", "", "", "", "");
       }
     }
     catch (JSONException e) {
       e.printStackTrace();
     }
     out.print(callbackData);
     out.flush();
     out.close();
   }
 
   public void delete()
   {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     String callbackData = "";
     try {
       String idStr = this.request.getParameter("id");
 
       if ((idStr == null) || ("".equals(idStr))) {
         callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", 
           "", "");
       } else {
         int id = 0;
         try {
           id = Integer.parseInt(idStr);
         }
         catch (Exception e) {
           callbackData = BjuiJson.json("300", "参数错误", "", "", "", 
             "", "", "");
         }
         if (id == 1) {
           callbackData = BjuiJson.json("300", "该用户不能删除", "", "", 
             "", "true", "", "");
         } else {
           User findUser = (User)this.userService.findById(User.class, id);
           if (findUser == null)
           {
             callbackData = BjuiJson.json("300", "用户不存在", "", "", 
               "", "true", "", "");
           } else {
             boolean result = this.userService.delete(findUser);
             if (result)
               callbackData = BjuiJson.json("200", "删除成功", "", 
                 "", "", "", "", "");
             else
               callbackData = BjuiJson.json("300", "删除失败", "", 
                 "", "", "", "", "");
           }
         }
       }
     }
     catch (JSONException e) {
       e.printStackTrace();
     }
     out.print(callbackData);
     out.flush();
     out.close();
   }
 
   public void login()
   {
     PrintWriter  out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     HttpSession session = this.request.getSession();
     String prePage = (String) session.getAttribute("prePage");
     //session.removeAttribute("prePage");
     JSONObject json = new JSONObject();
     User user1 = this.userService.getUserByPhone(this.user.getName());
     if (this.user == null || user1 == null) {
       json.put("status", "0");
       json.put("message", "手机号错误");
     } else {   	
       User loginUser = this.userService.login(user1.getPhone(), Md5.getMD5Code(this.user.getPassword()));
       if (loginUser == null) {
         json.put("status", "0");
         json.put("message", "密码错误");
       }
       else {
         loginUser.setLoginCount(Integer.valueOf(loginUser.getLoginCount().intValue() + 1));
         session.setAttribute("loginUser", loginUser);
         try
         {
           String ip = IpUtils.getIpAddress(this.request);          
           loginUser.setLastLoginIp(ip);
         } catch (Exception e) {
           loginUser.setLastLoginIp("0.0.0.0");
         }
         loginUser.setLastLoginTime(new Date());
         this.userService.saveOrUpdate(loginUser);
         json.put("status", "1");
         json.put("message", "登录成功");
         if(prePage!=null){
        	 json.put("prePage",prePage);
         }
         System.out.println("prepage:"+prePage);
       }
     }
     out.print(json.toString());
     out.flush();
     out.close();
   }
 
   public void promote()
   {
     String no = this.request.getParameter("no");
     User findUser = this.userService.getUserByNo(no);
     if (findUser == null) {
       this.request.setAttribute("status", "0");
       this.request.setAttribute("message", "推广链接无效");
     }
     else if (findUser.getStatus().intValue() == 0) {
       this.request.setAttribute("status", "0");
       this.request.setAttribute("message", "推广链接无效");
     } else {
       this.request.setAttribute("status", "1");
       this.request.setAttribute("no", no);
     }
     try
     {
       this.request.getRequestDispatcher("promoteRegister.jsp").forward(this.request, this.response);
     } catch (ServletException e1) {
       e1.printStackTrace();
     } catch (IOException e1) {
       e1.printStackTrace();
     }
   }
 
   //二维码
   public void QRCode(){
	   //String tragetUrl = "http://"+this.request.getServerName()+":"+this.request.getServerPort()+"/fenxiao/user/promoteQRCode.jsp";
	   //String tragetUrl = "http://aiwac.net/fenxiao/user/promoteQRCode.jsp";

 	   HttpSession session = this.request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
//       String timestamp = (String)session.getAttribute("timestamp");
//       Long timeNow = System.currentTimeMillis() / 1000; 
//	   checkandsign(tragetUrl);
		/*
		 * if (timestamp == null ){ checkandsign(tragetUrl); } else { Long timeLast =
		 * Long.parseLong(timestamp); if (timeNow - timeLast >= 7150) {
		 * checkandsign(tragetUrl); } }
		 */
       String bgImgUrl = UserAction.class.getClassLoader().getResource("../../images/code_image.jpg").getPath(); //背景图片地址
       String content = "http://"+this.request.getServerName()+":"+this.request.getServerPort()+"/fenxiao/register.jsp?tuijianren="+loginUser.getNo(); //二维码内容，指向注册地址
       
       //测试内容
       //String contentTest = "http://aiwac.net/fenxiao/register.jsp?tuijianren="+loginUser.getPhone();
       //session.setAttribute("link", content);
       try {
    	   OutputStream output = this.response.getOutputStream();
           VisualQRCode.createQRCode(content,
        	   bgImgUrl, 
               output, 
               'H', 
               new Color(70, 130, 180), 
               250, //二维码x轴起点
               950, //二维码y轴起点
               200, //二维码宽度
               false,
               VisualQRCode.POSITION_DETECTION_SHAPE_MODEL_ROUND_RECTANGLE, 
               VisualQRCode.FILL_SHAPE_MODEL_RECTANGLE);        
       } catch (IOException e) {
           e.printStackTrace();
       }       
   }
   
   public void logout() throws IOException {
     HttpSession session = this.request.getSession();
     session.setAttribute("loginUser", null);
     this.response.sendRedirect("../login.jsp");
   }
 
   public void changePassword() {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     String oldPassword = this.request.getParameter("oldPassword");
     String newPassword = this.request.getParameter("newPassword");
     HttpSession session = this.request.getSession();
     User loginUser = (User)session.getAttribute("loginUser");
     User findUser = (User)this.userService.findById(User.class, loginUser.getId().intValue());
     JSONObject json = new JSONObject();
     try {
       if (!StringUtils.equals(findUser.getPassword(), Md5.getMD5Code(oldPassword))) {
         json.put("status", "0");
         json.put("message", "旧密码错误");
       } else {
         findUser.setPassword(Md5.getMD5Code(newPassword));
         this.userService.saveOrUpdate(findUser);
         json.put("status", "1");
         json.put("message", "密码修改成功");
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
     out.print(json);
     out.flush();
     out.close();
   }
 
   public void resetPassword() {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     String password = this.request.getParameter("password");
     String phone = this.request.getParameter("phone");
     User findUser = this.userService.getUserByPhone(phone);
     JSONObject json = new JSONObject();
     try {
       if (findUser == null) {
         json.put("status", "0");
         json.put("message", "用户不存在");
       } else {
         findUser.setPassword(Md5.getMD5Code(password));
         this.userService.saveOrUpdate(findUser);
         json.put("status", "1");
         json.put("message", "密码重置成功");
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
     out.print(json);
     out.flush();
     out.close();
   }
 
   //创建用户编号
   public void createUserNo()
   {
     User findUser = null;
     String no = "";
     do {
       Random random = new Random();
       int num = random.nextInt(899999) + 100000; //随机生成100000~999999的正整数
       //this.user = this.userService.getUserByNo(""+ num);
       findUser = this.userService.getUserByNo(""+ num);
       no=""+num;
     }while (findUser != null);
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     out.print(no);
     out.flush();
     out.close();
   }
 
   public void userInfoJson()
   {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     HttpSession session = this.request.getSession();
     User loginUser = (User)session.getAttribute("loginUser");
     User findUser = (User)this.userService.findById(User.class, loginUser.getId().intValue());
     JSONObject json = (JSONObject)JSONObject.toJSON(findUser);
     out.print(json.toString());
     out.flush();
     out.close();
   }
 
   public void commissionToBalance()
   {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     JSONObject json = new JSONObject();
     String moneyStr = this.request.getParameter("money");
     Double money = Double.valueOf(0.0D);
     try {
       money = Double.valueOf(Double.parseDouble(moneyStr));
     } catch (Exception e) {
       json.put("status", "0");
       json.put("message", "参数错误");
       out.print(json.toString());
       out.flush();
       out.close();
       return;
     }
     if (money.doubleValue() <= 0.0D) {
       json.put("status", "0");
       json.put("message", "金额必须大于0");
     } else {
       HttpSession session = this.request.getSession();
       User loginUser = (User)session.getAttribute("loginUser");
       User findUser = (User)this.userService.findById(User.class, loginUser.getId().intValue());
       if (money.doubleValue() > findUser.getCommission().doubleValue()) {
         json.put("status", "0");
         json.put("message", "佣金额度不足");
       } else {
         findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() + money.doubleValue()));
         findUser.setCommission(Double.valueOf(findUser.getCommission().doubleValue() - money.doubleValue()));
         this.userService.saveOrUpdate(findUser);
 
         Financial financial = new Financial();
         financial.setType(Integer.valueOf(1));
         financial.setMoney(money);
         financial.setNo("" +System.currentTimeMillis());
 
         financial.setOperator(loginUser.getName());
 
         financial.setUser(findUser);
 
         financial.setCreateDate(new Date());
         financial.setDeleted(false);
 
         financial.setBalance(findUser.getBalance());
         financial.setPayment("佣金转入");
         financial.setRemark("佣金转入");
         this.financialService.saveOrUpdate(financial);
         json.put("status", "1");
         json.put("message", "转入成功");
       }
     }
     out.print(json.toString());
     out.flush();
     out.close();
   }
 
   public void balanceToUser()
   {
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     JSONObject json = new JSONObject();
     String moneyStr = this.request.getParameter("money");
     String userno = this.request.getParameter("userno");
     Double money = Double.valueOf(0.0D);
     try {
       money = Double.valueOf(Double.parseDouble(moneyStr));
     } catch (Exception e) {
       json.put("status", "0");
       json.put("message", "参数错误");
       out.print(json.toString());
       out.flush();
       out.close();
       return;
     }
     User toUser = this.userService.getUserByNo(userno);
     if (toUser == null) {
       json.put("status", "0");
       json.put("message", "会员编号不存在");
     }
     else if (money.doubleValue() <= 0.0D) {
       json.put("status", "0");
       json.put("message", "金额必须大于0");
     } else {
       HttpSession session = this.request.getSession();
       User loginUser = (User)session.getAttribute("loginUser");
       User findUser = (User)this.userService.findById(User.class, loginUser.getId().intValue());
       if (money.doubleValue() > findUser.getBalance().doubleValue()) {
         json.put("status", "0");
         json.put("message", "货币额度不足");
       } else {
         findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() - money.doubleValue()));
         toUser.setBalance(Double.valueOf(toUser.getBalance().doubleValue() + money.doubleValue()));
         this.userService.saveOrUpdate(findUser);
         this.userService.saveOrUpdate(toUser);
 
         Financial financial = new Financial();
         financial.setType(Integer.valueOf(0));
         financial.setMoney(Double.valueOf(-money.doubleValue()));
         financial.setNo(""+System.currentTimeMillis());
 
         financial.setOperator(loginUser.getName());
 
         financial.setUser(findUser);
 
         financial.setCreateDate(new Date());
         financial.setDeleted(false);
 
         financial.setBalance(findUser.getBalance());
         financial.setPayment("会员转账");
         financial.setRemark("会员转账，转入到会员编号【" + userno + "】");
         this.financialService.saveOrUpdate(financial);
 
         Financial financial2 = new Financial();
         financial2.setType(Integer.valueOf(1));
         financial2.setMoney(money);
         financial2.setNo(""+System.currentTimeMillis());
 
         financial2.setOperator(loginUser.getName());
 
         financial2.setUser(toUser);
 
         financial2.setCreateDate(new Date());
         financial2.setDeleted(false);
 
         financial2.setBalance(toUser.getBalance());
         financial2.setPayment("会员转账");
         financial2.setRemark("由会员编号【" + loginUser.getNo() + "】转入");
         this.financialService.saveOrUpdate(financial2);
         json.put("status", "1");
         json.put("message", "转入成功");
       }
     }
 
     out.print(json.toString());
     out.flush();
     out.close();
   }
 
   public void levelUserList() {
     HttpSession session = this.request.getSession();
     User loginUser = (User)session.getAttribute("loginUser");
     loginUser = this.userService.findById(User.class, loginUser.getId());
     List<User> levelUserList = this.userService.levelUserList(loginUser.getNo());
     JSONArray userList = new JSONArray();    
     
//     int firstLevelNum = 0;
// 
//     int secondLevelNum = 0;
// 
//     int thirdLevelNum = 0;
 
     int allLevelNum = 0;
 
//     int unStatusUserNum = 0;
// 
//     int todayRegUserNum = 0;
// 
//     int todayStatusUserNum = 0;
 
     for (User user : levelUserList)
     {
//       if (user.getStatus().intValue() == 0) {
//         unStatusUserNum++;
//       }
//       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
// 
//       String todayDate = sdf.format(new Date());
// 
//       String createDate = sdf.format(user.getCreateDate());
// 
//       String statusDate = "";
//       if (user.getStatusDate() != null) {
//         statusDate = sdf.format(user.getStatusDate());
//       }
// 
//       if (StringUtils.equals(createDate, todayDate)) {
//         todayRegUserNum++;
//       }
// 
//       if (StringUtils.equals(statusDate, todayDate)) {
//         todayStatusUserNum++;
//       }
 
//       String levelNos = user.getSuperior();
//       if (!StringUtils.isEmpty(levelNos)) {
//    	 
//         String[] leverNoArr = levelNos.split(";");
//         
//         int i = leverNoArr.length - 1;
         
//         for (int j = 1; i > 0; j++) {       	
//           if (!StringUtils.isEmpty(leverNoArr[i])) {
//             User levelUser = this.userService.getUserByNo(leverNoArr[i]);
             if (user != null)
             {
            	 JSONObject user1 = new JSONObject();           	 
            	 if (user.getLevel() < loginUser.getLevel()) {            		 
//		               if ((j == 1) && (StringUtils.equals(loginUser.getNo(), leverNoArr[i])))
//		                 firstLevelNum++;
//		               else if ((j == 2) && (StringUtils.equals(loginUser.getNo(), leverNoArr[i])))
//		                 secondLevelNum++;
//		               else if ((j == 3) && (StringUtils.equals(loginUser.getNo(), leverNoArr[i])))
//		                 thirdLevelNum++;
            		   if (checkIdentity(user, loginUser)) {
			               BounsRule identity = this.bounsRuleService.bounsRuleByLevel(user.getLevel());
			               user1.put("name", user.getName());
			               user1.put("phone", user.getPhone());
			               if (identity != null) {
			            	   user1.put("level", identity.getIdentityName());
			               } else {
			            	   user1.put("level", "非会员");
			               }
			               userList.add(user1);
			               allLevelNum += 1; 
            		   }
            	 }           	
             }
           }
//           i--;
//         }
 
//       }
 
//     }
     
     JSONObject json = new JSONObject();   
     json.put("userList", userList);
//     json.put("firstLevelNum", Integer.valueOf(firstLevelNum));
//     json.put("secondLevelNum", Integer.valueOf(secondLevelNum));
//     json.put("thirdLevelNum", Integer.valueOf(thirdLevelNum));
     json.put("allLevelNum", Integer.valueOf(allLevelNum));
//     json.put("unStatusUserNum", Integer.valueOf(unStatusUserNum));
//     json.put("todayRegUserNum", Integer.valueOf(todayRegUserNum));
//     json.put("todayStatusUserNum", Integer.valueOf(todayStatusUserNum));
     PrintWriter out = null;
     try {
       out = this.response.getWriter();
     } catch (IOException e) {
       e.printStackTrace();
     }
     out.print(json.toString());
     out.flush();
     out.close();
   }
 
   public User getUser() {
     return this.user;
   }
 
   public void setUser(User user) {
     this.user = user;
   }
 
   public String getFtlFileName() {
     return this.ftlFileName;
   }
 
   public void setFtlFileName(String ftlFileName) {
     this.ftlFileName = ftlFileName;
   }
   public static void main(String[] args) {
//     String a = ";1;2;3;";
//     System.out.println(a.split(";").length);
// 
//     if (a.split(";").length > 3) {
//       String[] b = a.split(";", 3);
//       System.out.println(b[2]);
//     }
     String b= "";
     String c = null;
     System.out.println(StringUtils.isEmpty(b));
   }
   
   //下级检验：如果被检测用户在到登录用户之间的关系链中，有不小于登录用户的上级，则不为登录用户的下级
   public boolean checkIdentity(User user, User loginUser) { 
	   String superListStr = user.getSuperior();
       String[] superList = superListStr.split(";");
       for(int i=superList.length-1;i>0;i--) {		  			   	 
		   if (superList[i].equals(loginUser.getNo())) {
			   break;
		   }
		   User findUser = this.userService.getUserByNo(superList[i]);
		   if (findUser != null && !findUser.isDeleted()) {
			   if (findUser.getLevel() >= loginUser.getLevel()) { 
				   return false; 
			   }  
		   }
	   }
	   return true;
   } 
   
   //生成微信验证签名部分
   public Map checkandsign(String tragetUrl) {
	   String url = "https://api.weixin.qq.com/cgi-bin/token?";
       String[] keyStrings = {"grant_type", "appid", "secret"};
	   Map<String, String> map = new HashMap<String, String>();
	   map.put("grant_type", "client_credential");
	   map.put("appid", "");
	   map.put("secret", "");
	   for(int i = 0;i < map.size();i++) {
       	url += keyStrings[i] +"="+ map.get(keyStrings[i]);
       	if (i != map.size() - 1) 
       		url += "&";
       }	
	   String token = "";
	   String ticket = "";
	   token = this.httpGetConnection.sendGet(url);
	   JSONObject tokenJsonObject = JSONObject.parseObject(token);
	   
	   url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+tokenJsonObject.getString("access_token")+"&type=jsapi";
	   ticket = this.httpGetConnection.sendGet(url);
	   JSONObject ticketJsonObject = JSONObject.parseObject(ticket);
	   
	   String jsapi_ticket = ticketJsonObject.getString("ticket");
	   HttpSession session = this.request.getSession();
	   
       Map<String, String> ret = sign(jsapi_ticket, tragetUrl);
       
       session.setAttribute("signature", ret.get("signature"));
       session.setAttribute("timestamp", ret.get("timestamp"));
       session.setAttribute("appId", map.get("appid"));
       session.setAttribute("nonceStr", ret.get("nonceStr"));
       return ret;
   };
   

   public static Map<String, String> sign(String jsapi_ticket, String url) {
       Map<String, String> ret = new HashMap<String, String>();
       String nonce_str = create_nonce_str();
       String timestamp = create_timestamp();
       String string1;
       String signature = "";

       //注意这里参数名必须全部小写，且必须有序
       string1 = "jsapi_ticket=" + jsapi_ticket +
                 "&noncestr=" + nonce_str +
                 "&timestamp=" + timestamp +
                 "&url=" + url;
       try
       {
           MessageDigest crypt = MessageDigest.getInstance("SHA-1");
           crypt.reset();
           crypt.update(string1.getBytes("UTF-8"));
           signature = byteToHex(crypt.digest());
       }
       catch (NoSuchAlgorithmException e)
       {
           e.printStackTrace();
       }
       catch (UnsupportedEncodingException e)
       {
           e.printStackTrace();
       }

       ret.put("url", url);
       ret.put("jsapi_ticket", jsapi_ticket);
       ret.put("nonceStr", nonce_str);
       ret.put("timestamp", timestamp);
       ret.put("signature", signature);

       return ret;
   }

   private static String byteToHex(final byte[] hash) {
       Formatter formatter = new Formatter();
       for (byte b : hash)
       {
           formatter.format("%02x", b);
       }
       String result = formatter.toString();
       formatter.close();
       return result;
   }

   private static String create_nonce_str() {
       return UUID.randomUUID().toString();
   }

   private static String create_timestamp() {
       return Long.toString(System.currentTimeMillis() / 1000);
   }
 }