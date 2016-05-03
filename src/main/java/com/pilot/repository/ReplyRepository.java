package com.pilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.entity.Post;
import com.pilot.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	public void deleteAllByPost(Post post);
}
