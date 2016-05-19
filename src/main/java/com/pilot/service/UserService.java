package com.pilot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.UserDao;
import com.pilot.entity.User;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	public User join(User user){
		return userDao.save(user);
	}
	
	public User findOne(Integer id){
		return userDao.findOne(id);
	}
	
	public User findByEmail(String email){
		return userDao.findByEmail(email);
	}
	
	public String setNickname(String nickname, User user){
		if(user != null){
			user.setNickname(nickname);
			join(user);
			return "닉네임 설정에 성공하였습니다.";
		}
		
		return "닉네임 설정에 실패하였습니다.";
	}
	
}
