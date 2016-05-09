package com.pilot.service;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.UserDao;
import com.pilot.entity.User;
import com.pilot.valid.JoinForm;
import com.pilot.valid.LoginForm;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	public User join(JoinForm joinForm){
		User user = new User();
		user.setEmail(joinForm.getEmail());
		user.setName(joinForm.getName());
		user.setPassword(DigestUtils.sha512Hex(joinForm.getPassword()));
		
		return userDao.save(user);
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
