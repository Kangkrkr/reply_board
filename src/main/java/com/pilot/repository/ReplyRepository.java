package com.pilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;

import javafx.geometry.Pos;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	public List<Reply> findAllByPost(Integer id);
}
