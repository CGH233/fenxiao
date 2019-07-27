package com.hansan.fenxiao.service.impl;

import com.hansan.fenxiao.dao.IRechargeDao;
import com.hansan.fenxiao.dao.IWithdrawDao;
import com.hansan.fenxiao.entities.Recharge;
import com.hansan.fenxiao.entities.Withdraw;
import com.hansan.fenxiao.service.IWithdrawService;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("withdrawService")
@Scope("prototype")
public class WithdrawServiceImpl<T extends Withdraw> extends BaseServiceImpl<T>
  implements IWithdrawService<T>
{
	@Resource(name="withdrawDao")
	   private IWithdrawDao withdrawDao;
	 
	   public Withdraw findByNo(String no)
	   {
	     return this.withdrawDao.findByNo(no);
	   }
}