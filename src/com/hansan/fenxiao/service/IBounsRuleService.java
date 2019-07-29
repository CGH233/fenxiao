package com.hansan.fenxiao.service;

import com.hansan.fenxiao.entities.BounsRule;

import java.util.List;

public abstract interface IBounsRuleService<T extends BounsRule> extends IBaseService<T> {
	public abstract List<BounsRule> getBounsRuleList();
	public abstract boolean editBounsRule(BounsRule bounsrule);
	public abstract BounsRule bounsRuleByLevel(int level);
}
