package com.hansan.fenxiao.dao;

import com.hansan.fenxiao.entities.GroupRule;

import java.util.List;

public abstract interface IGroupRuleDao extends IBaseDao<GroupRule>{
	public abstract List<GroupRule> getGroupRuleList();
	public abstract boolean editGroupRule(GroupRule grouprule);
}
