package com.pilot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.User;
import com.pilot.repository.UserRepository;

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
		System.out.println("다음의 사용자가 가입을 시도함 : " + user.getEmail());
		return userRepository.save(user);
	}
	
	public int login(User user){
		System.out.println("다음의 사용자가 로그인을 시도함 : " + user.getEmail());
		return userRepository.countByEmailAndPassword(user.getEmail(), user.getPassword());
	}
	
	public User findByEmail(String email){
		return userRepository.findUserByEmail(email);
	}
}
