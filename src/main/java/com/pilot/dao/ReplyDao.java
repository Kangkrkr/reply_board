package com.pilot.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(ReplyDao.class);
	
	@Autowired private ReplyRepository replyRepository;
	@Autowired private SessionFactory sessionFactory;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION) private EntityManager entityManager;
	
	public Reply findOne(Integer id){
		return replyRepository.findOne(id);
	}
	
	public void write(Reply reply){
		entityManager.merge(reply);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){

		Integer replyId = Integer.parseInt(writeForm.getType().split("#")[1]);
		
		Reply update = entityManager.find(Reply.class, replyId);
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
		entityManager.remove(reply);
	}

	public void deleteAllByPost(Post post){
		replyRepository.deleteAllByPost(post);
	}
	
	@Deprecated	// 추후 삭제후 개선 예정
	public void refreshReplies(List<Reply> replies){
		
		// 댓글의 부모게시글을 찾아 하위 댓글을 모두 삭제.
		deleteAllByPost(replies.get(0).getPost());
		
		// 새로 삽입...
		if(null != replies && 0 < replies.size()){
			for(Reply r : replies){
				write(r);
			}
		}
	}
}
