package com.czr.frame.spring.config.run;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.czr.frame.test.DBMethom;

public class SpringRun {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context = new ClassPathXmlApplicationContext("service.xml");
		DBMethom dbmethom =  (DBMethom)context.getBean("dBMethom");
		dbmethom.findByID();
	}
}
