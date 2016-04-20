package com.pilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pilot.domain.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	/*
	@Query("SELECT x FROM User x WHERE x.email = ?1 AND x.password = ?2")
	User loginByInputInfo(String email, String password);
	*/
	
	List<User> findAllByOrderByEmailAsc();
	int countByEmailAndPassword(String email, String password);
}
