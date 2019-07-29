package com.hansan.fenxiao.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.service.IBounsRuleService;
import com.hansan.fenxiao.utils.BjuiPage;
import com.hansan.fenxiao.utils.FreemarkerUtils;

import freemarker.template.Configuration;

@Controller("bounsRuleAction")
@Scope("prototype")
public class BounsRuleAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="bounsRuleService")
	private IBounsRuleService<BounsRule> bounsRuleService;
	
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
}
