package com.hansan.fenxiao.dao;

import com.hansan.fenxiao.entities.Commission;

import java.sql.Date;
import java.util.List;

public abstract interface ICommissionDao extends IBaseDao<Commission>
{
  public abstract List<Commission> getByUser(Integer paramInteger);
  
  public abstract Double getSumByPeriod(Integer paramInteger,String paramString1,String paramString2);
}

/* Location:           D:\360安全浏览器下载\WeFenxiao_A5\WeFenxiao_V1.0.1\WEB-INF\classes\
 * Qualified Name:     com.hansan.fenxiao.dao.ICommissionDao
 * JD-Core Version:    0.6.0
 */