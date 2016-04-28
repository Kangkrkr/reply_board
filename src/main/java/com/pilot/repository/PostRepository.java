package com.pilot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.domain.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	public void deletePostById(Integer postId);
	public Page<Post> findAll(Pageable pageable);
}
