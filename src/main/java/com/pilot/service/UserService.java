package com.pilot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.UserDao;
import com.pilot.entity.User;
import com.pilot.validator.LoginForm;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	public User join(User user){
		return userDao.join(user);
	}
	
	public User findOne(Integer id){
		return userDao.findOne(id);
	}
	
	public User findByUsername(String username){
		return userDao.findByUsername(username);
	}
	
	public List<User> findAll(){
		return userDao.findAll();
	}
	
	public User login(LoginForm loginData){
		return userDao.login(loginData);
	}
	
}
