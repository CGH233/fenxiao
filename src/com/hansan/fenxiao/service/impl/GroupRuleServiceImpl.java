package com.hansan.fenxiao.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hansan.fenxiao.dao.IGroupRuleDao;
import com.hansan.fenxiao.entities.GroupRule;
import com.hansan.fenxiao.service.IGroupRuleService;

@Service("groupRuleService")
@Scope("prototype")
public class GroupRuleServiceImpl<T extends GroupRule> extends BaseServiceImpl<T> implements IGroupRuleService<T>{
	

	@Resource(name="groupRuleDao")
	private IGroupRuleDao groupRuleDao;
	 
	@Override
	public List<GroupRule> getGroupRuleList() {
		List<GroupRule> list = new ArrayList<GroupRule>();
		list = groupRuleDao.getGroupRuleList();
		return list;
	}

	@Override
	public boolean editGroupRule(GroupRule grouprule) {
		boolean result;
		result = groupRuleDao.editGroupRule(grouprule);
		return result;
	}

}
