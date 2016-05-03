package com.pilot.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pilot.entity.Post;

@Component
public class PostWriter extends WriterImpl {

	@Autowired
	PostDao postDao;
	
	private static final Logger logger = LoggerFactory.getLogger(PostWriter.class);
	
	@Override
	public void write() {
		Post post = postConstructor();
		
		logger.info("사용자가 입력한 게시글의 내용 : {}, 사용자의 이름 : {}", post.getContent(), post.getUser().getName());
		postDao.write(post);
	}
}
