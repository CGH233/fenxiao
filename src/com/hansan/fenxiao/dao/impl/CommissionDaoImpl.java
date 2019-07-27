package com.hansan.fenxiao.dao.impl;

import com.hansan.fenxiao.dao.ICommissionDao;
import com.hansan.fenxiao.entities.Commission;
import com.hansan.fenxiao.utils.DateUtils;

import java.sql.Date;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("commissionDao")
@Scope("prototype")
public class CommissionDaoImpl extends BaseDaoImpl<Commission>
  implements ICommissionDao
{

  @Resource(name="sessionFactory")
  private SessionFactory sessionFactory;

  private Session getSession()
  {
    return this.sessionFactory.getCurrentSession();
  }

  public List<Commission> getByUser(Integer userId)
  {
    String hql = "from Commission where user.id=:userId and deleted=0";

    List commissionList = getSession().createQuery(hql)
      .setInteger("userId", userId.intValue()).list();
    return commissionList;
  }
  
  //查询月度业绩
  public Double getSumByPeriod(Integer userId,String startDate,String endDate){
	  String hql = "select sum(money) from Commission where user.id=:userId and deleted=0 ";
	  hql = hql + "and createDate>="+DateUtils.toDate(startDate)+" and createDate<="+DateUtils.toDate(endDate);
	  
	  Double sum = (Double)getSession().createQuery(hql).setInteger("userId",userId.intValue()).uniqueResult();
	  return sum;
  }
}
