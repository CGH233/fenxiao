package com.hansan.fenxiao.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.hansan.fenxiao.dao.IGroupRuleDao;
import com.hansan.fenxiao.entities.GroupRule;

@Repository("groupRuleDao")
@Scope("prototype")
public class GroupRuleDaoImpl extends  BaseDaoImpl<GroupRule> implements IGroupRuleDao{
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	 
	private Session getSession(){
	     return this.sessionFactory.getCurrentSession();
	}
	 
	@Override
	public List<GroupRule> getGroupRuleList() {
		List<GroupRule> listResult = new ArrayList<GroupRule>();
		listResult = list("from GroupRule");
		return listResult;
	}

	@Override
	public boolean editGroupRule(GroupRule grouprule) {
		boolean result;
		result = saveOrUpdate(grouprule);
		return result;
	}
}
