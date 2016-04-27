package com.pilot.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.repository.ReplyRepository;

@Service
@Transactional
public class ReplyService {

	@Autowired
	ReplyRepository replyReposioty;
	
	public void write(Reply reply){
		replyReposioty.save(reply);
	}
	
	public Reply findOne(Integer id){
		return replyReposioty.findOne(id);
	}
	
	public List<Reply> findAllByPost(Integer id){
		return replyReposioty.findAllByPost(id);
	}
	
	public void delete(Integer id){
		replyReposioty.delete(id);
	}
	
	public void deleteByPost(Integer id){
		replyReposioty.deleteByPost(id);
	}
}
