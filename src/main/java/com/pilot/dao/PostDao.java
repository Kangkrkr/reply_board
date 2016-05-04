package com.pilot.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.Post;
import com.pilot.entity.Reply;
import com.pilot.entity.User;
import com.pilot.repository.PostRepository;
import com.pilot.validator.WriteForm;

@Transactional
@Repository		// 또 다른 스프링의 스테레오 타입 어노테이션 중 하나로, 스프링의 컴포넌트 스캐닝에 의해 스캔됨.
public class PostDao {
	
	private static final Logger logger = LoggerFactory.getLogger(PostDao.class);

	@Autowired 
	private ReplyDao replyDao;
	
	@Autowired 
	private SessionFactory sessionFactory;
	
	@Autowired 
	private PostRepository postRepository;
	
	@PersistenceContext 
	private EntityManager entityManager;

	
	public Post findOne(Integer id){
		return postRepository.findOne(id);
	}
	
	public List<Post> findAll(){
		return postRepository.findAll();
	}
	
	public Page<Post> findAllByPage(Pageable pageable){
		return postRepository.findAll(pageable);
	}
	
	public void write(Post post){
		entityManager.merge(post);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){
		
		Integer postId = Integer.parseInt(writeForm.getType().split("#")[1]);
		
		Post update = entityManager.find(Post.class, postId);
		update.setImage(fixedPath);
		update.setContent(writeForm.getContent());
		update.setRegdate(new Date());
		update.setUser((User)session.getAttribute("userInfo"));
		
		entityManager.merge(update);
	}
	
	public void delete(Integer postId){
		Post post = findOne(postId);
		// 먼저 하위 댓글들을 모두 지운뒤, 게시글을 삭제한다.
		for(Reply reply : replyDao.findRepliesByPost(post)){
			replyDao.delete(reply);
		}
		postRepository.delete(post);
	}
	
	public List<Post> selectPost(int currentPage, int pageSize){
		try{
			Criteria result = sessionFactory.getCurrentSession().createCriteria(Post.class);
			
			return result.setFirstResult(currentPage).setMaxResults(pageSize).list();
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return null;
	}
}
