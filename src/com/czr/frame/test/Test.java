package com.czr.frame.test;

import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.czr.frame.beans.User;

/**
 * 
 * @author chenzhirong
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.configure();
		@SuppressWarnings("deprecation")
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		User user = new User();
		user.setCreatetime(new Date());
		user.setUpdatetime(new Date());
		s.save(user);
		tx.commit();
		s.close();
	}

}