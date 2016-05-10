package com.pilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{}
