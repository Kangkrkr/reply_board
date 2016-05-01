package com.pilot.service;

import java.util.List;

//import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.User;
import com.pilot.repository.UserRepository;
import com.pilot.validator.LoginForm;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findOne(Integer id){
		return userRepository.findOne(id);
	}
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public List<User> findAllByOderEmail(){
		return userRepository.findAllByOrderByEmailAsc();
	}
	
	public User join(User user){
		// 사용자 비밀번호 암호화처리.
		//user.setPassword(DigestUtils.sha512Hex(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User login(LoginForm loginData){
		return userRepository.findByEmailAndPassword(loginData.getEmail(), loginData.getPassword());
	}
	
}
