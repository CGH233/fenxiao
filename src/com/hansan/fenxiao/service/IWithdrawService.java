package com.hansan.fenxiao.service;

import com.hansan.fenxiao.entities.Withdraw;

public abstract interface IWithdrawService<T extends Withdraw> extends IBaseService<T>
{
	public abstract Withdraw findByNo(String paramString);
}
