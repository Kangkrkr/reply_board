package com.pilot.service;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.PostDao;
import com.pilot.entity.Post;
import com.pilot.validator.WriteForm;

@Service
public class PostService extends Writer {

	@Autowired PostDao postDao;

	public static final int MAX_SIZE = 3;
	private static final Logger logger = LoggerFactory.getLogger(PostService.class);
	
	public void write() {
		Post post = super.postConstructor();
		
		logger.info("사용자가 입력한 게시글의 내용 : {}, 사용자의 이름 : {}", post.getContent(), post.getUser().getName());
		postDao.write(post);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){
		postDao.update(writeForm, session, fixedPath);
	}
	
	public void delete(Integer postId){
		postDao.delete(postId);
	}
	
	public int count(){
		return postDao.findAll().size();
	}
}
