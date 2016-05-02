package com.pilot.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.Post;
import com.pilot.domain.User;
import com.pilot.repository.PostRepository;
import com.pilot.util.SessionUtil;
import com.pilot.validator.WriteForm;

@Service
@Transactional
public class PostService {
	
	public static final int MAX_SIZE = 3;

	@Autowired
	SessionUtil customUtil;
	
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
	
	public void test(){

		// 해당 게시글과 그에 따른 댓글 뽑아오기.
		Criteria c = customUtil.getSession().createCriteria(Post.class).createCriteria("replies")
											.add(Restrictions.eq("post.id", 17)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		// 자식댓글의 부모 게시글을 뽑아온다.
		System.err.println("부모 게시글 : " + c.createCriteria("post").setProjection(Projections.property("content")).list().get(0));
		
		// 댓글들의 내용을 뽑아낸다.
		System.err.println("자식 댓글들 : " + c.setProjection(Projections.property("content")).list());
		
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
