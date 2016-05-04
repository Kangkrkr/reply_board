package com.pilot.dao;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.pilot.repository.UserRepository;
import com.pilot.validator.LoginForm;

@Transactional
@Repository
public class UserDao {

	@Autowired private SessionFactory sessionFactory;
	@Autowired private UserRepository userRepository;
	
	static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	
	public long count(){
		return userRepository.findAll().size();
	}
	
	public User join(User user){
		logger.info("Success Save User : UserName is  {}, Email is {} " ,user.getName(), user.getEmail());
		user.setPassword(DigestUtils.sha512Hex(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User findOne(Integer id){
		return userRepository.findOne(id);
	}
	
	public User findByUsername(String username){
		User user = (User)sessionFactory.getCurrentSession().createCriteria(User.class).add(Restrictions.eq("name", username)).list().get(0);
		return user;
	}
	
	public List<User> findAll(){
		return sessionFactory.getCurrentSession().createCriteria(User.class).list();
	}
	
	public User login(LoginForm loginData){
		
		Criteria userCR = sessionFactory.getCurrentSession().createCriteria(User.class);
		
		Criterion email = Restrictions.eq("email", loginData.getEmail());
		Criterion password = Restrictions.eq("password", loginData.getPassword());
		
		userCR.add(Restrictions.and(email, password));
		
		return (User)userCR.list().get(0);
	}
}
