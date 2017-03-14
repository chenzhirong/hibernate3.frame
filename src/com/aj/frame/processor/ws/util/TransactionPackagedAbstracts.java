package com.aj.frame.processor.ws.util;

import org.springframework.transaction.TransactionStatus;

import com.aj.frame.web.util.dao.TransactionPackaged;

public abstract class TransactionPackagedAbstracts<T> implements TransactionPackaged<T>{
	private TransactionStatus status;

	protected final TransactionPackagedAbstracts<T> setStatus(TransactionStatus status) {
		this.status = status;
		return this;
	}
	
	/**
	 * <b>事务回滚</b> 
	 * 回滚后请立即返回结束并且结束事务打包回调方法中的业务逻辑
	 * @return null
	 */
	public final T rollback(){
		if(status!=null)status.setRollbackOnly();
		return null;
	}

}
