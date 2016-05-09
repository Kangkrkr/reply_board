package com.pilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	public List<Post> findAllByRootPost(Post rootPost);
}
