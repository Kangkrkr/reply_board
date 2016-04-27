package com.pilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.domain.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	public void deletePostById(Integer postId);
}
