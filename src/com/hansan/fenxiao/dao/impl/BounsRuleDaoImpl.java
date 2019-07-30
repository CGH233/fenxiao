package com.hansan.fenxiao.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.hansan.fenxiao.dao.IBounsRullDao;
import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.entities.User;

@Repository("bounsRuleDao")
@Scope("prototype")
public class BounsRuleDaoImpl extends  BaseDaoImpl<BounsRule> implements IBounsRullDao{
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	 
	private Session getSession(){
	     return this.sessionFactory.getCurrentSession();
	}
	 
	@Override
	public List<BounsRule> getBounsRuleList() {
		List<BounsRule> listResult = new ArrayList<BounsRule>();
		listResult = list("from BounsRule");
		return listResult;
	}

	@Override
	public boolean editBoundsRule(BounsRule bounsrule) {
		
		boolean result = saveOrUpdate(bounsrule);
		return result;
	}

	@Override
	public BounsRule bounsRuleByLevel(int level) {
		String hql = "from BounsRule where level=:level";
		BounsRule result = (BounsRule)getSession().createQuery(hql)
			       .setInteger("level", level).uniqueResult();
		return result;
	}
}
