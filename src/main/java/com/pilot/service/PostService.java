package com.pilot.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.Post;
import com.pilot.domain.User;
import com.pilot.repository.PostRepository;
import com.pilot.util.CustomUtil;
import com.pilot.validator.WriteForm;

@Service
@Transactional
public class PostService {
	
	public static final int MAX_SIZE = 3;

	@Autowired
	CustomUtil customUtil;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private EntityManager persistenceEntityManager;
	
	@Autowired
    private EntityManagerFactory entityManagerFactory;
	
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
			Criteria result = customUtil.getSession().createCriteria(Post.class);
			return result.setFirstResult(currentPage).setMaxResults(pageSize).list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
