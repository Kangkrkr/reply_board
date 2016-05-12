package com.pilot.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.PostDao;
import com.pilot.entity.Post;
import com.pilot.model.PostModel;

@Service
public class PostService {

	@Autowired 
	private PostDao postDao;
	
	public static final int MAX_SIZE = 10;
	
	public Post findOne(Integer id){
		return postDao.findOne(id);
	}
	
	public Post write(Post post) {
		return postDao.save(post);
	}
	
	public void update(Post update){
		postDao.update(update);
	}
	
	public void delete(Integer postId){
		postDao.delete(postId);
	}
	
	public int count(){
		return postDao.findAll().size();
	}

	public List<Post> findAll(){
		return postDao.findAll();
	}
	
	public List<Post> selectPost(int offset){
		return postDao.selectPost((offset - 1) * MAX_SIZE, MAX_SIZE);
	}
	
	public List<PostModel> createPostDTOs(List<Post> posts){
		
		List<PostModel> postDTOs = new ArrayList<>();
		
		if(null != posts && posts.size() > 0){
			for (Post post : posts) {
				postDTOs.add(PostModel.create(post));
			}
		}
		return postDTOs;
	}
}
