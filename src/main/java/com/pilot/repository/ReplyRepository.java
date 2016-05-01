package com.pilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	public void deleteAllByPost(Post post);
}
