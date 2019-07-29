package com.hansan.fenxiao.dao;

import com.hansan.fenxiao.entities.BounsRule;

import java.util.List;

public abstract interface IBounsRullDao extends IBaseDao<BounsRule>{
	public abstract List<BounsRule> getBounsRuleList();

}
