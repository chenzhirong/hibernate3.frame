package com.czr.frame.test;


import java.util.Date;

import com.aj.frame.web.util.dao.GenericDAO;
import com.czr.frame.beans.User;

public class DBMethom {

	private GenericDAO genericDAO;
	
	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void findByID(){
		User user = new User();
		user.setUsername("chenzhirong");
		user.setUsercode("chenzhirong");
		user.setZt(1L);
		user.setCreatetime(new Date());
		user.setUpdatetime(new Date());
		user.setEmail("email");
		user.setJb(1L);
		user.setPassword("111111");
		genericDAO.save(user);
//		User user = genericDAO.findById(User.class, 1000000000000003L);
//		System.out.println(user.getUsername());
	}

}
