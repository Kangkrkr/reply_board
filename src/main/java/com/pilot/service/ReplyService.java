package com.pilot.service;

import java.util.List;

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
	
	public List<Reply> findAllByPost(Integer id){
		return replyReposioty.findAllByPost(id);
	}
}
