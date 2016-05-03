package com.pilot.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.Post;
import com.pilot.entity.Reply;
import com.pilot.entity.User;
import com.pilot.repository.ReplyRepository;
import com.pilot.validator.WriteForm;

@Service
@Transactional
public class ReplyService {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private Session hibernateSession;
	
	public Reply findOne(Integer id){
		return replyRepository.findOne(id);
	}
	
	public void write(Reply reply){
		replyRepository.save(reply);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){
		Reply reply = replyRepository.findOne(Integer.parseInt(writeForm.getType().split("#")[1]));
		
		Reply r = replyRepository.findOne(reply.getId());
		
		Reply update = entityManager.find(Reply.class, reply.getId());
		update.setImage(fixedPath);
		update.setContent(writeForm.getContent());
		update.setPassword(writeForm.getPassword());
		update.setRegdate(new Date());
		update.setUser((User)session.getAttribute("userInfo"));
		
		entityManager.merge(update);
	}
	
	public List<Reply> findRepliesByPost(Post post){
		// Reply안의 post는 타입이 Post이기 때문에 비교할 대상 역시 클래스 객체여야한다.
		return hibernateSession.createCriteria(Reply.class).add(Restrictions.eq("post", post)).list();
	}
	
	public void delete(Reply reply){
		replyRepository.delete(reply.getId());
	}

	public void deleteAllByPost(Post post){
		replyRepository.deleteAllByPost(post);
	}
}
