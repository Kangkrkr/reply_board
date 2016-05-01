package com.pilot.util;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomUtil {

	@Autowired
	SessionFactory sessionFactory;
	
	public Criteria orderGenerator(Class<?> classType, String... orderConditions){
		Criteria criteria = getSession().createCriteria(classType);
		
		for(String order : orderConditions){
			criteria = criteria.addOrder(Order.asc(order));
		}
		
		return criteria;
	}

	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
}
