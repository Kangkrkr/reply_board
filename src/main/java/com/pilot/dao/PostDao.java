package com.pilot.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.entity.Post;
import com.pilot.repository.PostRepository;

@Transactional
@Repository
public class PostDao {
	
	private static final Logger logger = LoggerFactory.getLogger(PostDao.class);

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
	
	public Post save(Post post){
		return postRepository.save(post);
	}
	
	public void persist(Post post){
		entityManager.persist(post);
	}
	
	public void update(Post update){
		entityManager.merge(update);
	}
	
	public void detach(Post post){
		entityManager.detach(post);
	}
	
	public void delete(Post post){
		postRepository.delete(post);
	}
	
	public void delete(Integer postId){
		postRepository.delete(findOne(postId));
	}
	
	public List<Post> selectPost(int currentPage, int maxPostSize){
		try{
			Criteria result = sessionFactory.getCurrentSession().createCriteria(Post.class);
			
			return result.setFirstResult(currentPage).setMaxResults(maxPostSize).addOrder(Order.asc("path")).list();
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		return null;
	}
}
