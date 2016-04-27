package com.pilot.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	public List<Reply> findAllByPost(Integer id);
	public void deleteByPost(Integer id);
}
