package com.pilot.service;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.PostDao;
import com.pilot.dto.PostDTO;
import com.pilot.entity.Post;

@Service
public class PostService {

	@Autowired 
	private PostDao postDao;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public static final int MAX_SIZE = 10;
	private static final Logger logger = LoggerFactory.getLogger(PostService.class);
	
	public Post findOne(Integer id){
		return postDao.findOne(id);
	}
	
	public void write(Post post) {
		postDao.save(post);
	}
	
	public void update(Post update){
		postDao.update(update);
	}
	
	public void delete(Integer postId){
				//postDao.findAllByRootPost(postDao.findOne(postId));
		
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
	
	public List<PostDTO> createPostDTOs(List<Post> posts){
		
		List<PostDTO> postDTOs = new ArrayList<>();
		
		if(null != posts && posts.size() > 0){
			for (Post post : posts) {
				PostDTO dto = new PostDTO();
				dto.setPost(post);
				
				postDTOs.add(dto);
			}
		}
		
		return postDTOs;
	}
	
	public void refreshReplies(List<Post> posts, int originalIdx, Post toAdd){
		
		// 영속성 컨텍스트에서 제거된 엔티티들을 담을 리스트.
		List<Post> removedList = new ArrayList<>();
		
		// 전체 게시물 중 새로운 게시물이 삽입될 위치부터 끝까지 영속성 컨텍스트에서 제거(준영속상태로 만듦).
		for(int start = originalIdx + 1; start < posts.size(); start++){	
			postDao.detach(posts.get(start));	// 해당 엔티티를 영속성 컨텍스트에서 때어낸다.
			removedList.add(posts.get(start));
		}
		
		/*
		 * 게시글을 2개 이상 달면 다른쪽 게시글에 댓글이 달리지않는 현상 - 2016.05.09 fixed
		 * 댓글을 삭제한 부분이 있는 게시글에서, 댓글을 재게시할 때 삽입위치의 이전 글이 다시 게시되는 현상 발생.
		 */
		
		if(null != posts && posts.size() > 0){
			
			// 부모 게시글을 가져온다.
			Post rootPost = posts.get(originalIdx);
			
			//postDao.delete(posts.get(originalIdx).getId() + 1);
			
			// 남은 게시물 뒤에 새 게시물을 삽입한다.
			toAdd.setId(posts.get(originalIdx).getId() + 1);
			toAdd.setDepth(rootPost.getDepth() + 1);
			toAdd.setRootPost(rootPost);
			
			postDao.save(toAdd);			// 영속 상태로 만듦.
			
			
			Iterator<Post> it = removedList.iterator();
			while(it.hasNext()){
				Post post = it.next();
				
				post.setId(post.getId() + 1);
				postDao.save(post);
			}
		}
	}
}
