package com.hansan.fenxiao.dao.impl;

import com.hansan.fenxiao.dao.IWithdrawDao;
import com.hansan.fenxiao.entities.Recharge;
import com.hansan.fenxiao.entities.Withdraw;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("withdrawDao")
@Scope("prototype")
public class WithdrawDaoImpl extends BaseDaoImpl<Withdraw>
  implements IWithdrawDao
{
	   @Resource(name="sessionFactory")
	   private SessionFactory sessionFactory;
	 
	   private Session getSession()
	   {
	     return this.sessionFactory.getCurrentSession();
	   }
	 
	   public Withdraw findByNo(String no) {
	     String hql = "from Withdraw where no=:no";
	     Withdraw withdraw = (Withdraw)getSession().createQuery(hql)
	       .setString("no", no).uniqueResult();
	     return withdraw;
	   }
}
