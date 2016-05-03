package com.pilot.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.Post;
import com.pilot.entity.User;
import com.pilot.repository.PostRepository;
import com.pilot.validator.WriteForm;

@Transactional
@Repository		// 또 다른 스프링의 스테레오 타입 어노테이션 중 하나로, 스프링의 컴포넌트 스캐닝에 의해 스캔됨.
public class PostDao {
	
	public static final int MAX_SIZE = 3;

	@Autowired
	Session hibernateSession;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
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
		postRepository.save(post);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){
		Post post = postRepository.findOne(Integer.parseInt(writeForm.getType().split("#")[1]));
		
		Post p = postRepository.findOne(post.getId());
		System.err.println(p.getContent());
		
		Post update = entityManager.find(Post.class, post.getId());
		update.setImage(fixedPath);
		update.setContent(writeForm.getContent());
		update.setPassword(writeForm.getPassword());
		update.setRegdate(new Date());
		update.setUser((User)session.getAttribute("userInfo"));
		
		entityManager.merge(update);
	}
	
	// 게시글의 id가 아니라, 게시글을 작성한 유저의 id여야함.
	public void delete(Post post){
		postRepository.delete(post.getId());
	}
	
	public List<Post> selectPost(int currentPage, int pageSize){
		try{
			Criteria result = hibernateSession.createCriteria(Post.class);
			
			return result.setFirstResult(currentPage).setMaxResults(pageSize).list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
