package com.pilot.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.Post;
import com.pilot.entity.Reply;
import com.pilot.entity.User;
import com.pilot.repository.ReplyRepository;
import com.pilot.validator.WriteForm;

@Transactional
@Repository		// 또 다른 스프링의 스테레오 타입 어노테이션 중 하나로, 스프링의 컴포넌트 스캐닝에 의해 스캔됨.
public class ReplyDao {
	
	@Autowired private ReplyRepository replyRepository;
	@Autowired private EntityManager entityManager;

	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Reply findOne(Integer id){
		return replyRepository.findOne(id);
	}
	
	public void write(Reply reply){
		replyRepository.save(reply);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){
		Reply reply = replyRepository.findOne(Integer.parseInt(writeForm.getType().split("#")[1]));
		
		Reply update = entityManager.find(Reply.class, reply.getId());
		update.setImage(fixedPath);
		update.setContent(writeForm.getContent());
		update.setRegdate(new Date());
		update.setUser((User)session.getAttribute("userInfo"));
		
		entityManager.merge(update);
	}
	
	public List<Reply> findRepliesByPost(Post post){
		return sessionFactory.getCurrentSession().createCriteria(Reply.class).add(Restrictions.eq("post", post)).list();
	}
	
	public void delete(Reply reply){
		replyRepository.delete(reply.getId());
	}

	public void deleteAllByPost(Post post){
		replyRepository.deleteAllByPost(post);
	}
}
