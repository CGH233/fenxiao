package com.hansan.fenxiao.service;

import com.hansan.fenxiao.entities.GroupRule;

import java.util.List;

public abstract interface IGroupRuleService<T extends GroupRule> extends IBaseService<T> {
	public abstract List<GroupRule> getGroupRuleList();
	public abstract boolean editGroupRule(GroupRule bounsrule);
}
