package com.hansan.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.entities.Orders;
import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.service.IBounsRuleService;
import com.hansan.fenxiao.service.IOrdersService;
import com.hansan.fenxiao.utils.BjuiJson;
import com.hansan.fenxiao.utils.BjuiPage;
import com.hansan.fenxiao.utils.FreemarkerUtils;
import com.hansan.fenxiao.utils.Md5;

import freemarker.template.Configuration;

@Controller("bounsRuleAction")
@Scope("prototype")
public class BounsRuleAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="bounsRuleService")
	private IBounsRuleService<BounsRule> bounsRuleService;
	private BounsRule bounsrule;
	
	public void bounsRuleList() {
		 String key = this.request.getParameter("key");
		/*
		 String key = this.request.getParameter("key");
	     String countHql = "select count(*) from BounsRule where deleted=0";
	     String hql = "from User where deleted=0";
	     if (StringUtils.isNotEmpty(key)) {
	       countHql = countHql + " and (name='" + key + "' or no='" + key + "' or phone='" + key + "')";
	       hql = hql + " and (name='" + key + "' or no='" + key + "' or phone='" + key + "')";
	     }
	     hql = hql + " order by id desc";
	
	     int count = 0;
	     count = this.userService.getTotalCount(countHql, new Object[0]);**/
	     this.page = new BjuiPage(this.pageCurrent, this.pageSize);
	     this.page.setTotalCount(2);
	     this.cfg = new Configuration();
	     String hql = "from User where deleted=0";
	     this.cfg.setServletContextForTemplateLoading(
	       this.request.getServletContext(), "WEB-INF/templates/admin");
	     List<BounsRule> bounsRuleList = this.bounsRuleService.getBounsRuleList();
	     System.out.println(bounsRuleList.get(0).toString());
	     Map root = new HashMap();
	     root.put("bounsRuleList", bounsRuleList);
	     root.put("page", this.page);
	     root.put("key", key);
	     FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
	}
	
	public void editBounsRule() {
		 PrintWriter out = null;
	     try {
	       out = this.response.getWriter();
	     } catch (IOException e) {
	       e.printStackTrace();
	     }
	     String callbackData = "";
	     try {
	    	 BounsRule bounsrule = this.bounsrule;
	         boolean result = this.bounsRuleService.editBounsRule(bounsrule);
	 
	         if (result) {
	           callbackData = BjuiJson.json("200", "修改成功", "", 
	             "", "", "true", "", "");
	         }
	         else
	           callbackData = BjuiJson.json("300", "修改失败", "", 
	             "", "", "", "", "");
	     }
	     catch (JSONException e) {
	       e.printStackTrace();
	     }
	     out.print(callbackData);
	     out.flush();
	     out.close();
	}
	
	public void bounsRuleByLevel()
	 {
	    Integer level = Integer.valueOf(this.request.getParameter("level"));
	    level = Integer.valueOf(level);
	    BounsRule bounsrule = this.bounsRuleService.bounsRuleByLevel(level);
	    if (bounsrule == null) {
	      this.request.setAttribute("status", "0");
	      this.request.setAttribute("message", "不存在");
	    } else {
//	      HttpSession session = this.request.getSession();
//	      User loginUser = (User)session.getAttribute("loginUser");
//	      if (bounsrule.getUser().getId() != loginUser.getId()) {
//	        this.request.setAttribute("status", "0");
//	        this.request.setAttribute("message", "没有权限");
//	      } else {
	        this.request.setAttribute("bounsRule", bounsrule);
	        try {
	          this.request.getRequestDispatcher("oneBounsRule.jsp").forward(this.request, this.response);
	        } catch (ServletException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	    }
    }
}

