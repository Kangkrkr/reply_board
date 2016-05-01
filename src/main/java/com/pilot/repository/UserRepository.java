package com.pilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.domain.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	// 메소드 명명 규칙을 사용한 쿼리 메소드.
	List<User> findAllByOrderByEmailAsc();
	
	// 로그인 정보 확인.
	User findByEmailAndPassword(String email, String password);
	
	// 로그인시 폼에서 입력한 이메일로 유저 객체 얻어오기.
	User findUserByEmail(String email);
}
