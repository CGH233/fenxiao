package com.hansan.fenxiao.dao;

import com.hansan.fenxiao.entities.Withdraw;

public abstract interface IWithdrawDao extends IBaseDao<Withdraw>
{

	public abstract Withdraw findByNo(String paramString);
}
