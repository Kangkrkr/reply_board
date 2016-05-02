package com.pilot.service;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.repository.ReplyRepository;
import com.pilot.util.CustomUtil;

@Service
@Transactional
public class ReplyService {
	
	@Autowired
	ReplyRepository replyRepository;
	
	@Autowired
	CustomUtil util;
	
	public Reply findOne(Integer id){
		return replyRepository.findOne(id);
	}
	
	public void write(Reply reply){
		replyRepository.save(reply);
	}
	
	public List<Reply> findRepliesByPost(Post post){
		// Reply안의 post는 타입이 Post이기 때문에 비교할 대상 역시 클래스 객체여야한다.
		return util.getSession().createCriteria(Reply.class).add(Restrictions.eq("post", post)).list();
	}
	
	public void delete(Reply reply){
		replyRepository.delete(reply.getId());
	}

	public void deleteAllByPost(Post post){
		replyRepository.deleteAllByPost(post);
	}
}
