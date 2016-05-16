package com.pilot.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.User;
import com.pilot.form.LoginForm;
import com.pilot.repository.UserRepository;

@Transactional
@Repository
public class UserDao {

	@Autowired 
	private SessionFactory sessionFactory;
	
	@Autowired 
	private UserRepository userRepository;
	
	static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	
	public User save(User user){
		return userRepository.save(user);
	}
	
	public User findOne(Integer id){
		return userRepository.findOne(id);
	}
	
	public User findByEmail(String email){
		return userRepository.findByEmail(email);
	}
	
	// 로그인 검증
	public User login(LoginForm loginData){
		
		Criteria userCR = sessionFactory.getCurrentSession().createCriteria(User.class);
		
		Criterion email = Restrictions.eq("email", loginData.getEmail());
		Criterion password = Restrictions.eq("password", loginData.getPassword());
		
		userCR.add(Restrictions.and(email, password));
		
		return (User)userCR.list().get(0);
	}
}
