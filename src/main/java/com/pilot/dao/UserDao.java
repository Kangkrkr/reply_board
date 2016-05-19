package com.pilot.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.User;
import com.pilot.repository.UserRepository;

@Transactional
@Repository
public class UserDao {

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
}
