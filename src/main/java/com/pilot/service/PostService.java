package com.pilot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pilot.domain.Post;
import com.pilot.repository.PostRepository;

@Service
@Transactional
public class PostService {

	@Autowired
	private PostRepository postRepository;

	public Post findOne(Integer id){
		return postRepository.findOne(id);
	}
	
	public List<Post> findAll(){
		return postRepository.findAll();
	}
	
	public Post write(Post post){
		System.out.println("다음의 글이 작성됨 : " + post.getContent());
		return postRepository.save(post);
	}
	
}
