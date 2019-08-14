package com.hansan.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hansan.fenxiao.dao.IBaseDao;
import com.hansan.fenxiao.entities.Admin;
import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.entities.Commission;
import com.hansan.fenxiao.entities.GroupRule;
import com.hansan.fenxiao.entities.Orders;
import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.service.IBaseService;
import com.hansan.fenxiao.service.IBounsRuleService;
import com.hansan.fenxiao.service.ICommissionService;
import com.hansan.fenxiao.service.IGroupRuleService;
import com.hansan.fenxiao.service.IOrdersService;
import com.hansan.fenxiao.service.IUserService;
import com.hansan.fenxiao.utils.BjuiJson;
import com.hansan.fenxiao.utils.BjuiPage;
import com.hansan.fenxiao.utils.FreemarkerUtils;
import com.hansan.fenxiao.utils.Md5;

import freemarker.template.Configuration;

@Controller("groupRuleAction")
@Scope("prototype")
public class GroupRuleAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="groupRuleService")
	private IGroupRuleService<GroupRule> groupRuleService;
	@Resource(name="userService")
	private IUserService<User> userService;
	@Resource(name="bounsRuleService")
	private IBounsRuleService<BounsRule> bounsRuleService;
	@Resource(name="ordersService")
	private IOrdersService<Orders> ordersService;
	@Resource(name="commissionService")
	private ICommissionService<Commission> commissionService;
	private GroupRule GroupRule;
	Date date = new Date();	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat m = new SimpleDateFormat("MM");
	SimpleDateFormat y = new SimpleDateFormat("yyyy");
	int year = Integer.valueOf(y.format(date));
	int month = Integer.valueOf(m.format(date));
	Calendar cal = Calendar.getInstance();
	
	public void groupRuleList() {
		 String key = this.request.getParameter("key");
	     this.page = new BjuiPage(this.pageCurrent, 6);
	     this.page.setTotalCount(2);
	     this.cfg = new Configuration();
	     String hql = "from User where deleted=0";
	     this.cfg.setServletContextForTemplateLoading(
	       this.request.getServletContext(), "WEB-INF/templates/admin");
	     List<GroupRule> GroupRuleList = this.groupRuleService.getGroupRuleList();
	     Map root = new HashMap();
	     root.put("groupRuleList", GroupRuleList);
	     root.put("page", this.page);
	     root.put("key", key);
	     FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
	}

	public void editGroupRule() {
		 PrintWriter out = null;
	     try {
	       out = this.response.getWriter();
	     } catch (IOException e) {
	       e.printStackTrace();
	     }
	     String callbackData = "";
	     try {
	    	 
	    	 
	    	 GroupRule GroupRule = new GroupRule();
	    	 GroupRule GroupRuleUnsave = new GroupRule();
	    	 int id = Integer.parseInt(this.request.getParameter("groupRule.id"));
	    	 GroupRule.setLower_limit(Double.valueOf(this.request.getParameter("groupRule.lower_limit")));
	    	 String upper_limitString = this.request.getParameter("groupRule.upper_limit");
	    	 if (upper_limitString.equals("无上限")) upper_limitString = null;
	    	 GroupRule.setUpper_limit(Double.valueOf(upper_limitString));
	    	 GroupRule.setCommission(this.request.getParameter("groupRule.commission"));
	    	 GroupRule.setLevel(Integer.parseInt(this.request.getParameter("groupRule.level")));
	    	 if(id < 0) {
	    		 callbackData = BjuiJson.json("300", "参数错误", "", 
	    	             "", "", "true", "", "");
	    	 }else {
	    		 if (id > 1) {	    			 
	    			 GroupRule checkGroupRulefront = this.groupRuleService.findById(GroupRule.class, id-1);	    			 
	    			 if (checkGroupRulefront.getUpper_limit() > GroupRule.getLower_limit()) {
	    				 callbackData = BjuiJson.json("403", "修改值超出上一个范围的上限", "", 
		             "", "", "", "", "");
	    			 }
	    			 if (GroupRule.getUpper_limit() != null) {
	    				 GroupRule checkGroupRulebehind = this.groupRuleService.findById(GroupRule.class, id+1);
	    				 if (GroupRule.getUpper_limit() > checkGroupRulebehind.getLower_limit()) {
		    				 callbackData = BjuiJson.json("403", "修改值超出下一个范围的下限", "", 
			             "", "", "", "", "");
		    			 }
	    			 }
	    		 }else {
		    		 GroupRuleUnsave = this.groupRuleService.findById(GroupRule.class, id);
		    		 GroupRule.setCreateDate(GroupRuleUnsave.getCreateDate());
		    		 GroupRule.setVersion(GroupRuleUnsave.getVersion());
		    		 GroupRule.setId(GroupRuleUnsave.getId());
		    		 
		    		 boolean result;
		    		 result = this.groupRuleService.editGroupRule(GroupRule);
		    		 
			         if (result) {
			           callbackData = BjuiJson.json("200", "修改成功", "", 
			             "", "", "true", "", "");
			         }
			         else
			           callbackData = BjuiJson.json("300", "修改失败", "", 
			             "", "", "", "", "");
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
	
	public void groupRuleById()
	 {
	    Integer id = Integer.valueOf(this.request.getParameter("id"));
	    //level = Integer.valueOf(level);
	    GroupRule GroupRule = this.groupRuleService.findById(GroupRule.class, id);
	    if (GroupRule == null) {
	      System.out.println("GroupRule is null");
	    } else {
	    	 this.cfg = new Configuration();
	         this.cfg.setServletContextForTemplateLoading( this.request.getServletContext(), 
	        		 										"WEB-INF/templates/admin");
	    	Map root = new HashMap();
	        root.put("groupRule", GroupRule);
	    	FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
	    }
    }
	
	//团队奖列表
	public void groupcommissionList() { 
	     String countHql = "select count(*) from User where (level = 4 or level = 5 or level = 6) and deleted=0";
	     String hql = "from User where (level = 4 or level = 5 or level = 6) and deleted = 0 order by id desc";	 
	     HttpSession session = this.request.getSession();
	     int count = 0;
	     count = this.userService.getTotalCount(countHql, null);
	     this.page = new BjuiPage(this.pageCurrent, this.pageSize);
	     this.page.setTotalCount(count);
	     this.cfg = new Configuration();
	 
	     this.cfg.setServletContextForTemplateLoading(
	       this.request.getServletContext(), "WEB-INF/templates/admin");
	     List<User> userList = this.userService.list(hql, this.page.getStart(), this.page.getPageSize(), new Object[0]);
	     List<BounsRule> bounsRuleList = this.bounsRuleService.getBounsRuleList();	     
	     	  
	     for (User user:userList) {
			user.setCommission(subordinateCommission(user));	
		 }
	     session.setAttribute("userList", userList);
	     Map root = new HashMap();
	     root.put("userList", userList);
	     root.put("bounsRuleList", bounsRuleList);
	     root.put("page", this.page);
	     FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
	}
	
	//一个代理本月的营业额所应得的团队奖
	public Double subordinateCommission(User user) {	    
	     List<User> levelUserList = this.userService.levelUserList(user.getNo());  	     
	     Double sumDouble = 0.00d;
	     
	     for (User auser : levelUserList){
	         if (auser != null){         	 
	        	 if (auser.getLevel() < user.getLevel()) {            		 
	        		 if (checkIdentity(auser, user)) {
				         sumDouble += oneMonthCommission(auser);
	        		 }           	
	             }
	         }
	     }
	     sumDouble *= 0.0001d;
	     List<GroupRule> groupRules = this.groupRuleService.getGroupRuleList();
	     for (GroupRule gr:groupRules) {
	    	 if (gr.getLevel() == 6 || gr.getLevel() == 12 || gr.getLevel() == 13) continue;
	    	 if (gr.getUpper_limit() == null) {
	    		 gr.setUpper_limit(sumDouble+1);
	    	 }
	    	 if (sumDouble < gr.getLower_limit()) break;
	    	 if (sumDouble >= gr.getLower_limit() && sumDouble < gr.getUpper_limit()){
	    		 sumDouble = sumDouble * Double.valueOf(gr.getCommission().substring(0,gr.getCommission().length()-1))* 0.01d;
	    		 sumDouble *= 10000;
	    	     return sumDouble;
	    	 }
	     }
	     return 0.00d;
	}
	
	//一个下级用户本月的营业额
	public Double oneMonthCommission(User user) {
		Double sum = 0.00d;
		String hql = "from Orders where user="+user.getId() +"and status = 1 and deleted = 0";
		List<Orders> orderList = this.ordersService.list(hql);	
		for (Orders order:orderList) {
			Date date = order.getPayDate();
			cal.setTime(date);
			//cal默认从0开始
			if ((cal.get(Calendar.MONTH)+1) == month) {
				sum += order.getMoney();
			}
		}
		return sum;
	}
	
	//支付团队奖到佣金
	public void groupPay() {
		PrintWriter out = null;
	     try {
	       out = this.response.getWriter();
	     } catch (IOException e) {
	       e.printStackTrace();
	     }
	     String callbackData = "";
	     try {
	    	GroupRule groupdate = this.groupRuleService.findById(GroupRule.class, 12);
			HttpSession session = this.request.getSession();
			Admin loginUser = (Admin)session.getAttribute("loginAdmin");
			List<User> userList = (List<User>)session.getAttribute("userList");
			int set = 0;
			if (groupdate.getCreateDate() != null) {
				cal.setTime(groupdate.getCreateDate());
				if (cal.get(Calendar.MONTH)+1 < month && cal.get(Calendar.YEAR) < year) {
					set = 1;
				}
			} else {
				set = 1;
			}
			if (set == 1){
				for (User auser:userList) {
					if (auser.getCommission() == 0) {
						continue;
					}
					User user = this.userService.findById(User.class, auser.getId());
					Commission commission = new Commission();
					commission.setMoney(Double.valueOf(auser.getCommission()));
					user.setCommission(user.getCommission()+auser.getCommission());
					this.userService.saveOrUpdate(user);					 					
					commission.setRemark("来自用户（手机号："+user.getPhone()+",用户名："+user.getName()+"）  的团队奖奖励");		    
					commission.setType(Integer.valueOf(1));			            
		            commission.setNo(""+System.currentTimeMillis());
		            commission.setOperator(loginUser.getName());
		            commission.setUser(user);
		            commission.setCreateDate(date);
		            commission.setDeleted(false);
		            commission.setLevel(1);
		            this.commissionService.saveOrUpdate(commission);
				}
				groupdate.setCreateDate(date);
				this.groupRuleService.saveOrUpdate(groupdate);
				callbackData = BjuiJson.json("200", "提交完成", "", "", "", "true", "", "");
			}else {
				callbackData = BjuiJson.json("403", "提交失败，本月已提交", "", "", "", "true", "", "");
			}
	     }catch (JSONException e) {
		       e.printStackTrace();
		 }
		 out.print(callbackData);
		 out.flush();
		 out.close();
		
	}
	
	//股东分红列表
	public void stockholderCommissionList() { 
	     String countHql = "select count(*) from User where level = 6 and deleted=0";
	     String hql = "from User where level = 6 and deleted = 0 order by id desc";	 
	     HttpSession session = this.request.getSession();
	     int count = 0;
	     count = this.userService.getTotalCount(countHql, null);
	     this.page = new BjuiPage(this.pageCurrent, this.pageSize);
	     this.page.setTotalCount(count);
	     this.cfg = new Configuration();
	 
	     this.cfg.setServletContextForTemplateLoading(
	       this.request.getServletContext(), "WEB-INF/templates/admin");
	     List<User> userList = this.userService.list(hql, this.page.getStart(), this.page.getPageSize(), new Object[0]);
	     List<BounsRule> bounsRuleList = this.bounsRuleService.getBounsRuleList();	     
	     	  
	     for (User user:userList) {
			user.setCommission(stockHolderCommission(user));	
		 }
	     session.setAttribute("userList", userList);
	     Map root = new HashMap();
	     root.put("userList", userList);
	     root.put("bounsRuleList", bounsRuleList);
	     root.put("page", this.page);
	     FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
	}	
	
	//一个月的股东分红
	public Double stockHolderCommission(User user) {
	     List<User> levelUserList = this.userService.levelUserList(user.getNo());  	     
	     Double sumDouble = 0.00d;
	     
	     for (User auser : levelUserList){
	         if (auser != null){         	 
	        	 if (auser.getLevel() < user.getLevel()) {            		 
	        		 if (checkIdentity(auser, user)) {
				         sumDouble += oneMonthCommission(auser);
	        		 }           	
	             }
	         }
	     }
	     sumDouble *= 0.0001d;
	     List<GroupRule> groupRules = this.groupRuleService.getGroupRuleList();
	     for (GroupRule gr:groupRules) {
	    	 if (gr.getLevel() != 6) continue;
	    	 if (gr.getUpper_limit() == null) {
	    		 gr.setUpper_limit(sumDouble+1);
	    	 }
	    	 if (sumDouble < gr.getLower_limit()) break;
	    	 if (sumDouble >= gr.getLower_limit() && sumDouble < gr.getUpper_limit()){
	    		 sumDouble = Double.valueOf(gr.getCommission());
	    	     return sumDouble;
	    	 }
	     }
	     return 0.00d;
	}
	
	//支付股东分红到佣金
	public void stockholderPay() {
		PrintWriter out = null;
		     try {
		       out = this.response.getWriter();
		     } catch (IOException e) {
		       e.printStackTrace();
		     }
		     String callbackData = "";
		     try {
		    	GroupRule groupdate = this.groupRuleService.findById(GroupRule.class, 13);
				HttpSession session = this.request.getSession();
				Admin loginUser = (Admin)session.getAttribute("loginAdmin");
				List<User> userList = (List<User>)session.getAttribute("userList");
				int set = 0;
				if (groupdate.getCreateDate() != null) {
					cal.setTime(groupdate.getCreateDate());
					if (cal.get(Calendar.MONTH)+1 < month && cal.get(Calendar.YEAR) < year) {
						set = 1;
					}
				} else {
					set = 1;
				}
				if (set == 1){
					for (User auser:userList) {
						if (auser.getCommission() == 0) {
							continue;
						}
						User user = this.userService.findById(User.class, auser.getId());
						Commission commission = new Commission();
						commission.setMoney(Double.valueOf(auser.getCommission()));
						user.setCommission(user.getCommission()+auser.getCommission());
						this.userService.saveOrUpdate(user);					 					
						commission.setRemark("来自用户（手机号："+user.getPhone()+",用户名："+user.getName()+"）  的股东分红奖励");		    
						commission.setType(Integer.valueOf(1));			            
			            commission.setNo(""+System.currentTimeMillis());
			            commission.setOperator(loginUser.getName());
			            commission.setUser(user);
			            commission.setCreateDate(date);
			            commission.setDeleted(false);
			            commission.setLevel(1);
			            this.commissionService.saveOrUpdate(commission);
					}
					groupdate.setCreateDate(date);
					this.groupRuleService.saveOrUpdate(groupdate);
					callbackData = BjuiJson.json("200", "提交完成", "", "", "", "true", "", "");
				}else {
					callbackData = BjuiJson.json("403", "提交失败，本月已提交", "", "", "", "true", "", "");
				}
		     }catch (JSONException e) {
			       e.printStackTrace();
			 }
			 out.print(callbackData);
			 out.flush();
			 out.close();
			
	}
	
	public GroupRule getGroupRule() {
		return GroupRule;
	}

	public void setGroupRule(GroupRule GroupRule) {
		this.GroupRule = GroupRule;
	}
	
	//确认是否为下级
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
}


