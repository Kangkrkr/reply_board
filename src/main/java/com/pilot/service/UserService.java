package com.pilot.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.UserDao;
import com.pilot.entity.User;
import com.pilot.form.JoinForm;
import com.pilot.form.LoginForm;

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
	
	public User login(LoginForm loginData){
		return userDao.login(loginData);
	}
	
}
