package com.pilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{}
