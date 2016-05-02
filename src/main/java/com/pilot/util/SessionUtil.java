package com.pilot.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

	@Autowired
	SessionFactory sessionFactory;
	
	/*
	public Criteria orderGenerator(Class<?> classType, String... orderConditions){
		Criteria criteria = getSession().createCriteria(classType);
		
		for(String order : orderConditions){
			criteria = criteria.addOrder(Order.asc(order));
		}
		
		return criteria;
	}
	*/

	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
}
