package com.pilot.service;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.Post;
import com.pilot.repository.PostRepository;
import com.pilot.util.CustomUtil;

@Service
@Transactional
public class PostService {
	
	public static final int MAX_SIZE = 3;

	@Autowired
	CustomUtil customUtil;
	
	@Autowired
	private PostRepository postRepository;

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
