package com.hansan.fenxiao.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hansan.fenxiao.dao.IBounsRullDao;
import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.service.IBounsRuleService;

@Service("bounsRuleService")
@Scope("prototype")
public class BounsRuleServiceImpl<T extends BounsRule> extends BaseServiceImpl<T> implements IBounsRuleService<T>{
	

	@Resource(name="bounsRuleDao")
	private IBounsRullDao bounsRullDao;
	 
	@Override
	public List<BounsRule> getBounsRuleList() {
		List<BounsRule> list = new ArrayList<BounsRule>();
		list = bounsRullDao.getBounsRuleList();
		return list;
	}

	@Override
	public boolean editBounsRule(BounsRule bounsrule) {
		boolean result;
		result = bounsRullDao.editBoundsRule(bounsrule);
		return result;
	}

	@Override
	public BounsRule bounsRuleByLevel(int level) {
		BounsRule bounsrule = null;
		bounsrule = bounsRullDao.bounsRuleByLevel(level);
		return bounsrule;
	}
	
	

}
