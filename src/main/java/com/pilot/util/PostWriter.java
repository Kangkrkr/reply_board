package com.pilot.util;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pilot.domain.Post;
import com.pilot.service.PostService;

@Component
public class PostWriter extends WriterImpl {

	@Autowired
	PostService postService;

	@Override
	public void write() {
		Post post = postConstructor();
		post.setRegdate(new Date());
		
		postService.write(post);
	}
}
