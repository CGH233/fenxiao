package com.hansan.fenxiao.dao.impl;

import com.hansan.fenxiao.dao.IBaseDao;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("baseDao")
@Scope("prototype")
public class BaseDaoImpl<T>
  implements IBaseDao<T>
{

  @Resource(name="sessionFactory")
  protected SessionFactory sessionFactory;
  Log log = LogFactory.getLog(AdminDaoImpl.class);

  private Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  public T findById(Class<T> clazz, int id)
  {
	return (T)getSession().get(clazz,id);
  }

  public boolean saveOrUpdate(T baseBean)
  {
    try {
      getSession().saveOrUpdate(baseBean);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }return false;
  }

  public boolean delete(T baseBean)
  {
    try
    {
      getSession().delete(baseBean);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }return false;
  }

  public List<T> list(String hql)
  {
    List list =  createQuery(hql).list();
    return list;
  }
  /*
   * 查询(non-Javadoc)
   * hql：hql查询语句
   * firstResult:结果中的第一行
   * maxResult：结果的最大行数
   * params：hql的命名参数
   * @see com.hansan.fenxiao.dao.IBaseDao#list(java.lang.String, int, int, java.lang.Object[])
   */
  public List<T> list(String hql, int firstResult, int maxResult, Object[] params)
  {
    Query query = createQuery(hql);
    for (int i = 0; (params != null) && (i < params.length); i++) {
      query.setParameter(i + 1, params[i]);//设置hql参数
    }

    List list = createQuery(hql).setFirstResult(firstResult).setMaxResults(maxResult).list();
    return list;
  }

  public int getTotalCount(String hql, Object[] params)
  {
    Query query = createQuery(hql);
    for (int i = 0; (params != null) && (i < params.length); i++) {
      query.setParameter(i + 1, params[i]);
    }

    Object obj = query.uniqueResult();
    return ((Long)obj).intValue();
  }

  public Query createQuery(String hql)
  {
    return getSession().createQuery(hql);
  }

  public void deleteAll(String entity, String where)
  {
    Query query = createQuery("delete from " + entity + " where " + where);
    query.executeUpdate();
  }
}