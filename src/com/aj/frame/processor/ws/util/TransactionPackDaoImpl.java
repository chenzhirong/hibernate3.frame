package com.aj.frame.processor.ws.util;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.ast.ASTQueryTranslatorFactory;
import org.hibernate.transform.Transformers;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.aj.frame.web.util.dao.GenericDAO;
import com.aj.frame.web.util.dao.TransactionPackaged;
import com.aj.frame.web.util.dao.impl.TransactionPackagedAbstract;
import com.aj.frame.web.util.table.TableResult;

/**
 * 为非web项目 生成的事务打包dao
 * @author Administrator
 *
 */
public class TransactionPackDaoImpl extends HibernateDaoSupport implements GenericDAO{
	private TransactionTemplate transactionTemplate;
	private String bean_id;
	
	public String getBean_id() {
		return bean_id;
	}

	public void setBean_id(String bean_id) {
		this.bean_id = bean_id;
	}

	public void setTransactionManager(
	            PlatformTransactionManager transactionManager) {
	        this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@Override
	public void save(Object entity) {			
		this.getHibernateTemplate().save(entity);
	}

	@Override
	public void delete(Object entity) {		
		this.getHibernateTemplate().delete(entity);
	}
	
	@Override
	public void update(Object entity) {		
		this.getHibernateTemplate().update(entity);
	}
	
	@Override
	public void bacthInsert(final Collection<?> entities){
		this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException {
				if (getHibernateTemplate().isCheckWriteOperations() && 
						getHibernateTemplate().getFlushMode() != HibernateTemplate.FLUSH_EAGER &&
						session.getFlushMode().lessThan(FlushMode.COMMIT)) {
					throw new InvalidDataAccessApiUsageException(
							"Write operations are not allowed in read-only mode (FlushMode.MANUAL): "+
							"Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.");
				}
				for (Object entity : entities) {
					session.save(entity);
				}
				return null;
			}
		});
	}
	
	@Override
	public void bacthDelete(final Collection<?> entities) {		
		this.getHibernateTemplate().deleteAll(entities);
	}
	
	@Override
	public void bacthUpdate(final Collection<?> entities){
		this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException {
				if (getHibernateTemplate().isCheckWriteOperations() && 
						getHibernateTemplate().getFlushMode() != HibernateTemplate.FLUSH_EAGER &&
						session.getFlushMode().lessThan(FlushMode.COMMIT)) {
					throw new InvalidDataAccessApiUsageException(
							"Write operations are not allowed in read-only mode (FlushMode.MANUAL): "+
							"Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.");
				}
				for (Object entity : entities) {
					session.update(entity);
				}
				return null;
			}
		});
	}
	
	@Override
	public void saveOrUpdate(Object entity) {		
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	@Override
	public void bacthSaveOrUpdate(final Collection<?> entities) {		
		this.getHibernateTemplate().saveOrUpdateAll(entities);
	}
	@Override
	public void move(Object entity){
		this.update(entity);
	}
	@Override
	public void bacthMove(final Collection<?> entities) {		
		this.bacthUpdate(entities);
	}	

	@Override
	public <T> T findById(Class<T> entityClass,Object id) {		
		return (T)this.getHibernateTemplate().get(entityClass,(Serializable)id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findAll(Class<?> entityClass) {		
		return (List<Object>)this.getHibernateTemplate().loadAll(entityClass);
	}

	@Override
	public List<Object> queryHql(String hql) {				
		return this.queryAllHql(hql, -1, -1, true, false, 0);
	}

	@Override
	public List<Map<?,?>> queryHqlResultMap(String hql) {		
		return this.queryAllHql(hql, -1, -1, true, true, 0);
	}

	@Override
	public Object queryHqlResultSingle(String hql) {		
		return this.queryAllHql(hql, -1, -1, false, false, 0);
	}

	@Override
	public Map<?,?> queryHqlResultSingleMap(String hql) {		
		return this.queryAllHql(hql, -1, -1, false, true, 0);
	}

	@Override
	public List<Object> queryHqlPart(String hql, int start, int count) {		
		return this.queryAllHql(hql, start, count, true, false, 0);
	}

	@Override
	public List<Map<?,?>> queryHqlPartResultMap(String hql, int start, int count) {		
		return this.queryAllHql(hql, start, count, true, true, 0);
	}

	@Override
	public List<Object> queryHqlMap(String hql, Map<String, Object> param) {		
		return this.queryAllHql(hql, -1, -1, true, false, 1, param);
	}

	@Override
	public List<Map<?,?>> queryHqlMapResultMap(String hql , Map<String, Object> param) {		
		return this.queryAllHql(hql, -1, -1, true, true, 1, param);
	}

	@Override
	public Object queryHqlMapResultSingle(String hql, Map<String, Object> param) {		
		return this.queryAllHql(hql, -1, -1, false, false, 1, param);
	}

	@Override
	public Map<?,?> queryHqlMapResultSingleMap(String hql, Map<String, Object> param) {		
		return this.queryAllHql(hql, -1, -1, false, true, 1, param);
	}

	@Override
	public List<Object> queryHqlMapPart(String hql, int start, int count, 
			Map<String, Object> param) {		
		return this.queryAllHql(hql, start, count, true, false, 1, param);
	}
	
	@Override
	public List<Map<?,?>> queryHqlMapPartResultMap(String hql, int start, int count, 
			Map<String, Object> param) {		
		return this.queryAllHql(hql, start, count, true, true, 1, param);
	}

	@Override
	public List<Object> queryHqlList(String hql, List<Object> param) {		
		return this.queryAllHql(hql, -1, -1, true, false, 2, param);
	}

	@Override
	public List<Map<?,?>> queryHqlListResultMap(String hql, List<Object> param) {		
		return this.queryAllHql(hql, -1, -1, true, true, 2, param);
	}

	@Override
	public Object queryHqlListResultSingle(String hql, List<Object> param) {		
		return this.queryAllHql(hql, -1, -1, false, false, 2, param);
	}

	@Override
	public Map<?,?> queryHqlListResultSingleMap(String hql, List<Object> param) {		
		return this.queryAllHql(hql, -1, -1, false, true, 2, param);
	}

	@Override
	public List<Object> queryHqlListPart(String hql, int start,	int count, List<Object> param) {		
		return this.queryAllHql(hql, start, count, true, false, 2, param);
	}

	@Override
	public List<Map<?,?>> queryHqlListPartResultMap(String hql, int start, int count, 
			List<Object> param) {		
		return this.queryAllHql(hql, start, count, true, true, 2, param);
	}

	@Override
	public List<Object> queryHqlVarargs(String hql, Object... params) {		
		return this.queryAllHql(hql, -1, -1, true, false, 3, params);
	}

	@Override
	public List<Map<?,?>> queryHqlVarargsResultMap(String hql, Object... params) {		
		return this.queryAllHql(hql, -1, -1, true, true, 3, params);
	}

	@Override
	public Object queryHqlVarargsResultSingle(String hql, Object... params) {		
		return this.queryAllHql(hql, -1, -1, false, false, 3, params);
	}

	@Override
	public Map<?,?> queryHqlVarargsResultSingleMap(String hql, Object... params) {		
		return this.queryAllHql(hql, -1, -1, true, true, 3, params);
	}

	@Override
	public List<Object> queryHqlVarargsPart(String hql, int start, int count, Object... params) {		
		return this.queryAllHql(hql, start, count, true, false, 3, params);
	}

	@Override
	public List<Map<?,?>> queryHqlVarargsPartResultMap(String hql, int start, int count,
			Object... params) {		
		return this.queryAllHql(hql, start, count, true, true, 3, params);
	}

	@Override
	public List<Object> querySql(String sql) {		
		return this.queryAllSql(sql, -1, -1, true, false, 0);
	}

	@Override
	public List<Map<?,?>> querySqlResultMap(String sql) {		
		return this.queryAllSql(sql, -1, -1, true, true, 0);
	}

	@Override
	public Object querySqlResultSingle(String sql) {		
		return this.queryAllSql(sql, -1, -1, false, false, 0);
	}

	@Override
	public Map<?,?> querySqlResultSingleMap(String sql) {		
		return this.queryAllSql(sql, -1, -1, false, true, 0);
	}

	@Override
	public List<Object> querySqlPart(String sql, int start, int count) {		
		return this.queryAllSql(sql, start, count, true, false, 0);
	}

	@Override
	public List<Map<?,?>> querySqlPartResultMap(String sql, int start, int count) {		
		return this.queryAllSql(sql, start, count, true, true, 0);
	}

	@Override
	public List<Object> querySqlMap(String sql, Map<String,Object> param) {		
		return this.queryAllSql(sql, -1, -1, true, false, 1, param);
	}

	@Override
	public List<Map<?,?>> querySqlMapResultMap(String sql, Map<String,Object> param) {		
		return this.queryAllSql(sql, -1, -1, true, true, 1, param);
	}

	@Override
	public Object querySqlMapResultSingle(String sql, Map<String, Object> param) {		
		return this.queryAllSql(sql, -1, -1, false, false, 1, param);
	}

	@Override
	public Map<?,?> querySqlMapResultSingleMap(String sql, Map<String, Object> param) {		
		return this.queryAllSql(sql, -1, -1, false, true, 1, param);
	}

	@Override
	public List<Object> querySqlMapPart(String sql, int start, int count,
			Map<String,Object> param) {
		return this.queryAllSql(sql, start, count, true, false, 1, param);
	}

	@Override
	public List<Map<?,?>> querySqlMapPartResultMap(String sql, int start, int count,
			Map<String,Object> param) {
		return this.queryAllSql(sql, start, count, true, true, 1, param);
	}

	@Override
	public List<Object> querySqlList(String sql, List<Object> param) {		
		return this.queryAllSql(sql, -1, -1, true, false, 2, param);
	}

	@Override
	public List<Map<?,?>> querySqlListResultMap(String sql, List<Object> param) {		
		return this.queryAllSql(sql, -1, -1, true, true, 2, param);
	}

	@Override
	public Object querySqlListResultSingle(String sql, List<Object> param) {		
		return this.queryAllSql(sql, -1, -1, false, false, 2, param);
	}

	@Override
	public Map<?,?> querySqlListResultSingleMap(String sql, List<Object> param) {		
		return this.queryAllSql(sql, -1, -1, false, true, 2, param);
	}	
	
	@Override
	public List<Object> querySqlListPart(String sql, int start,	int count, List<Object> param) {		
		return this.queryAllSql(sql, start, count, true, false, 2, param);
	}
	
	@Override
	public List<Map<?,?>> querySqlListPartResultMap(String sql, int start, int count,
			List<Object> param) {		
		return this.queryAllSql(sql, start, count, true, true, 2, param);
	}

	@Override
	public List<Object> querySqlVarargs(String sql, Object... params) {		
		return this.queryAllSql(sql, -1, -1, true, false, 3, params);
	}

	@Override
	public List<Map<?,?>> querySqlVarargsResultMap(String sql, Object... params) {		
		return this.queryAllSql(sql, -1, -1, true, true, 3, params);
	}

	@Override
	public Object querySqlVarargsResultSingle(String sql, Object... params) {		
		return this.queryAllSql(sql, -1, -1, false, false, 3, params);
	}

	@Override
	public Map<?,?> querySqlVarargsResultSingleMap(String sql, Object... params) {		
		return this.queryAllSql(sql, -1, -1, false, true, 3, params);
	}

	@Override
	public List<Object> querySqlVarargsPart(String sql, int start, int count, Object... params) {		
		return this.queryAllSql(sql, start, count, true, false, 3, params);
	}

	@Override
	public List<Map<?,?>> querySqlVarargsPartResultMap(String sql, int start,int count,
			Object... params) {		
		return this.queryAllSql(sql, start, count, true, true, 3, params);
	}
	
	@Override
	public TableResult tableHql(final String hql) {		
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();					
				    Query query = session.createQuery(hql);
					sr.setResult(query.list());
					sr.setTotalCount(sr.getResult().size());					
					return sr;
			}
		});
	}

	@Override
	public TableResult tableHql(String hql, int start, int count) {		
		return this.tableHqlMap(hql, start, count, null);
	}

	@Override
	public TableResult tableHqlMap(final String hql, final int start, 
			final int count, final Map<String,Object> param) {		
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
				    
					Query countQuery= TransactionPackDaoImpl.this.getTotalCountQuery(hql,session);			
					if(param != null){
						//:.*? |:.* 把:查出来带空格
						//(?<==).*?(?=( |$)) =开头 空格结尾
						//:\\S++ 把:查出来不带空格
						//(?<=:).*?\\S++
						Matcher matcher = Pattern.compile("(?<=:)\\S++").matcher(hql);
						int i = 0;
						while(matcher.find()){
							countQuery.setParameter(i++,param.get(matcher.group()));
						}
					}
					sr.setTotalCount(countQuery.uniqueResult().toString());
					
				    Query query = session.createQuery(hql);
				    TransactionPackDaoImpl.this.setParamMap(query,start,count,param);
					sr.setResult(query.list());
					
					return sr;
			}
		});
	}

	@Override
	public TableResult tableHqlList(final String hql, final int start,
			final int count, final List<Object> param) {		
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
				    
					Query countQuery= TransactionPackDaoImpl.this.getTotalCountQuery(hql,session);					
					TransactionPackDaoImpl.this.setParamList(countQuery,0,0,param);
					sr.setTotalCount(countQuery.uniqueResult().toString());
				    
				    Query query = session.createQuery(hql);
				    TransactionPackDaoImpl.this.setParamList(query,start,count,param);
					sr.setResult(query.list());
					
					return sr;				
			}
		});
	}

	@Override
	public TableResult tableHqlVarargs(final String hql, final int start,
			final int count,final Object... params) {		
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
				    
					Query countQuery= TransactionPackDaoImpl.this.getTotalCountQuery(hql,session);					
					TransactionPackDaoImpl.this.setParamVarargs(countQuery,0,0,params);
					sr.setTotalCount(countQuery.uniqueResult().toString());
				    
				    Query query = session.createQuery(hql);
				    TransactionPackDaoImpl.this.setParamVarargs(query,start,count,params);
					sr.setResult(query.list());

					return sr;				
			}
		});
	}
	
	@Override
	public TableResult tableSql(final String sql) {
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
				    Query query = session.createSQLQuery(sql);
					sr.setResult(query.list());
					sr.setTotalCount(sr.getResult().size());
					return sr;
			}
		});
	}

	@Override
	public TableResult tableSql(String sql, int start, int count) {		
		return this.tableSqlMap(sql, start, count, null);
	}

	@Override
	public TableResult tableSqlMap(final String sql, final int start,
			final int count,final Map<String,Object> param) {
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
					
				    Query countQuery=session.createSQLQuery(TransactionPackDaoImpl.this.getTotalCountSql(sql));
				    TransactionPackDaoImpl.this.setParamMap(countQuery,0,0,param);
					sr.setTotalCount(countQuery.uniqueResult().toString());
				    
				    Query query = session.createSQLQuery(sql);
				    TransactionPackDaoImpl.this.setParamMap(query,start,count,param);
				    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					sr.setResult(query.list());

					return sr;				
			}
		});
	}

	@Override
	public TableResult tableSqlList(final String sql, final int start,
			final int count,final List<Object> param) {		
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
					
				    Query countQuery=session.createSQLQuery(TransactionPackDaoImpl.this.getTotalCountSql(sql));
				    TransactionPackDaoImpl.this.setParamList(countQuery,0,0,param);
					sr.setTotalCount(countQuery.uniqueResult().toString());
				    
				    Query query = session.createSQLQuery(sql);
				    TransactionPackDaoImpl.this.setParamList(query,start,count,param);
				    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					sr.setResult(query.list());

					return sr;				
			}
		});
	}

	@Override
	public TableResult tableSqlVarargs(final String sql, final int start,
			final int count,final Object... params) {		
		return (TableResult)this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>(){
			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
					TableResult sr = new TableResult();
					
				    Query countQuery=session.createSQLQuery(TransactionPackDaoImpl.this.getTotalCountSql(sql));
				    TransactionPackDaoImpl.this.setParamVarargs(countQuery,0,0,params);
					sr.setTotalCount(countQuery.uniqueResult().toString());
				    
				    Query query = session.createSQLQuery(sql);
				    TransactionPackDaoImpl.this.setParamVarargs(query,start,count,params);
				    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					sr.setResult(query.list());
					
					return sr;
			}
		});
	}

	@Override
	public int executeDelOrUpdate(final String hql, final boolean isHql) {		
		return (Integer)this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>(){
			@Override
			public Object doInHibernate(final Session session)
					throws HibernateException, SQLException {				
				return (isHql ? session.createQuery(hql):
						 session.createSQLQuery(hql)).executeUpdate();
			}
		});
	}

	@Override
	public int executeDelOrUpdate(final String hql, final boolean isHql,
			final Map<String,Object> param) {		
		return (Integer)this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>(){
			@Override
			public Object doInHibernate(final Session session)
					throws HibernateException, SQLException {				
				Query query = isHql ? session.createQuery(hql):
					 session.createSQLQuery(hql);
				TransactionPackDaoImpl.this.setParamMap(query,0,0,param);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public int executeDelOrUpdate(final String hql, final boolean isHql,
			final List<Object> param) {		
		return (Integer)this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>(){
			@Override
			public Object doInHibernate(final Session session)
					throws HibernateException, SQLException {				
				Query query = isHql ? session.createQuery(hql):
					 session.createSQLQuery(hql);
				TransactionPackDaoImpl.this.setParamList(query,0,0,param);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public int executeDelOrUpdateVarargs(final String hql, final boolean isHql,
			final Object... params) {		
		return (Integer)this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>(){
			@Override
			public Object doInHibernate(final Session session)
					throws HibernateException, SQLException {				
				Query query = isHql ? session.createQuery(hql):
					 session.createSQLQuery(hql);
				TransactionPackDaoImpl.this.setParamVarargs(query,0,0,params);
				return query.executeUpdate();
			}
		});
	}
	/*
	 * 事务回调接口dao改回直接传递 满足非web项目
	 * 修改者：章启湘
	 * 修改日期：2013-12-2
	 */
	@Override
	public <T> T transaction(final TransactionPackaged<T> action) {	
		return (T) transactionTemplate.execute(new TransactionCallback<T>(){
			@Override
			public T doInTransaction(TransactionStatus status) {
				try {
					if(action instanceof TransactionPackagedAbstract)
						return ((TransactionPackagedAbstracts<T>)action).setStatus(status).doInHibernate(TransactionPackDaoImpl.this);
					else
						return action.doInHibernate(TransactionPackDaoImpl.this);
				} catch (Exception e) {
                    status.setRollbackOnly();
					e.printStackTrace();
					return null;
				}
			}
		 });
	}

	/**
	 * <b>总查询</b>
	 * @param isHql				是否是 Hibernate语言(true:Hibernate查询语言;false:结构化查询语言)
	 * @param hql/sql			Hibernate查询语言/结构化查询语言
	 * @param start				从第几条数据开始读取
	 * @param count				一共读取多少条,0为读取所有数据
	 * @param resultIsList  	设置返回的结果是列表还是单个实例(true:返回的结果是对象列表List<?>;false:返回的结果是单个实例或者null)
	 * @param resultObjectMap	设置返回的结果对象是否为Map(Hibernate查询语言在设置select ?? from 下生效)
	 * @param paramType			查询语句中的参数类型(0:无参数;1:键值对参数;2:对象列表 参数;3:可变参数<对象>)
	 * @param params    		查询中用到的参数(可变参数<对象>)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T query(final boolean isHql, final String hql,
			final int start, final int count,
			final boolean resultIsList, final boolean resultObjectMap,
			final int paramType, final Object... params) {
		return (T) this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(org.hibernate.Session session)
				throws HibernateException, SQLException {				
				Query query = isHql ? session.createQuery(hql):session.createSQLQuery(hql);
				switch(paramType){
					case 1:
						TransactionPackDaoImpl.this.setParamMap(query,start,count,(Map<String, ?>)params[0]);
						break;
					case 2:
						TransactionPackDaoImpl.this.setParamList(query,start,count,(List<Object>)params[0]);
						break;
					case 3:
						TransactionPackDaoImpl.this.setParamVarargs(query,start,count,params);						
						break;
					case 0:
					default:
						break;
				}
				if(resultObjectMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(resultIsList){
					return query.list();
				}else{
					query.setFirstResult(0);
					query.setMaxResults(1);
					return query.uniqueResult();
				}
			}
		});
	}

	/**
	 * <b>Hibernate查询语言</b>
	 * @param <T>
	 * @param hql				Hibernate查询语言
	 * @param start				从第几条数据开始读取
	 * @param count				一共读取多少条,0为读取所有数据
	 * @param resultIsList  	设置返回的结果是列表还是单个实例(true:返回的结果是对象列表List<?>;false:返回的结果是单个实例或者null)
	 * @param resultObjectMap	设置返回的结果对象是否为Map(Hibernate查询语言在设置select ?? from 下生效)
	 * @param paramType			查询语句中的参数类型(0:无参数;1:键值对参数;2:对象列表 参数;3:可变参数<对象>)
	 * @param params    		查询中用到的参数(可变参数<对象>)
	 * @return
	 */
	private <T> T queryAllHql(String hql, int start, int count, boolean resultIsList,
			boolean resultObjectMap, int paramType, Object... params) {
		return this.query(true, hql, start, count, resultIsList, resultObjectMap, paramType, params);
	}

	/**
	 * <b>结构化查询语言</b>
	 * @param sql				结构化查询语言
	 * @param start				从第几条数据开始读取
	 * @param count				一共读取多少条,0为读取所有数据
	 * @param resultIsList  	设置返回的结果是列表还是单个实例(true:返回的结果是对象列表List<?>;false:返回的结果是单个实例或者null)
	 * @param resultObjectMap	设置返回的结果对象是否为Map(Hibernate查询语言在设置select ?? from 下生效)
	 * @param paramType			查询语句中的参数类型(0:无参数;1:键值对参数;2:对象列表 参数;3:可变参数<对象>)
	 * @param params    		查询中用到的参数(可变参数<对象>)
	 * @return
	 */
	private <T> T queryAllSql(String sql, int start, int count, boolean resultIsList,
			boolean resultObjectMap, int paramType, Object... params) {		
		return this.query(false, sql, start, count, resultIsList, resultObjectMap, paramType, params);
	}

	/**
	 * <b>参数绑定(命名参数)</b>
	 * @param query			Hibernate查询实体对象
	 * @param start			从第几条数据开始读取
	 * @param count			一共读取多少条,0为读取所有数据
	 * @param param         查询中用到的参数(键值对)
	 */
	private void setParamMap(Query query,int start,int count,Map<String, ?> param){
		if(param != null){
			Iterator<String> keyIter = param.keySet().iterator();
			String key;
			while(keyIter.hasNext()){
				key = keyIter.next();
				query.setParameter(key, param.get(key));
			}
		}
		if(start > 0)query.setFirstResult(start);
		if(count > 0)query.setMaxResults(count);
	}

	/**
	 * <b>参数绑定(位置参数)</b>
	 * @param query			Hibernate查询实体对象
	 * @param start			从第几条数据开始读取
	 * @param count			一共读取多少条,0为读取所有数据
	 * @param param			查询中用到的参数(对象列表)
	 */
	private void setParamList(Query query,int start,int count,List<Object> param){
		if(param != null){
			int size=param.size();
			for(int i = 0; i < size; i++) {
				query.setParameter(i, param.get(i));
			}
		}
		if(start > 0)query.setFirstResult(start);
		if(count > 0)query.setMaxResults(count);		
	}

	/**
	 * <b>参数绑定(位置参数)</b>
	 * @param query			Hibernate查询实体对象
	 * @param start			从第几条数据开始读取
	 * @param count			一共读取多少条,0为读取所有数据
	 * @param params		查询中用到的参数(可变参数<对象>)
	 */
	private void setParamVarargs(Query query,int start,int count,Object... params){
		if(params != null) {
			int size=params.length;
			for(int i = 0; i < size; i++) {
				query.setParameter(i, params[i]);
			}
		}
		if(start > 0)query.setFirstResult(start);
		if(count > 0)query.setMaxResults(count);
	}

	/**
	 * @param hql
	 * @param session
	 * @return
	 */
	private Query getTotalCountQuery(String hql,Session session){
		return session.createSQLQuery(getTotalCountSql(hqlToSql(hql,session.getSessionFactory())));		
	}

	/**
	 * @param sql
	 * @return
	 */
	private String getTotalCountSql(String sql){
		StringBuffer countsql = new StringBuffer();
		countsql.append("select count(*) from (");
		countsql.append(sql);
		countsql.append(")");
		return countsql.toString();
	}

	/**
	 * <b>Hibernate查询语言 转 结构化查询语言</b>
	 * @param hql
	 * @param sessionFactory
	 * @return 结构化查询语言
	 */
	private String hqlToSql(String hql,SessionFactory sessionFactory){
				
		/*QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(hql, Collections.EMPTY_MAP,
		(org.hibernate.engine.SessionFactoryImplementor) sessionFactory);*/
				
		QueryTranslator queryTranslator =  new ASTQueryTranslatorFactory()
			.createQueryTranslator(hql, hql, Collections.EMPTY_MAP,
				(org.hibernate.engine.SessionFactoryImplementor) sessionFactory);
		
		queryTranslator.compile(Collections.EMPTY_MAP, false);				
		return queryTranslator.getSQLString();
	}
}
