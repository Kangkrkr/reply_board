package com.pilot.dao;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pilot.domain.Post;
import com.pilot.service.PostService;
import com.pilot.util.ExtraInfo;
import com.pilot.validator.WriteForm;

@Component
public class PostWriter extends WriterImpl {

	@Autowired
	PostService postService;

	@Override
	public void write() {
		Post post = postConstructor();
		post.setRegdate(new Date());
		
		String distictions[] = getWriteForm().getType().split("#");
		String typeDistinction = distictions[0];
		
		if(typeDistinction.equals("post")){
			post.setDepth(0);
			post.setPost(post);	// 0은 제일 최상위 루트 부모를 의미.
		}else{
			int targetId = Integer.parseInt(distictions[1]);
			if(typeDistinction.contains("reply")){
				Post originalPost = postService.findOne(targetId);
				post.setDepth(originalPost.getDepth() + 1);
				post.setPost(originalPost);
				//post.setPost(originalPost);
				// 원본 게시글의 아이디를 댓글게시글에 저장.
			}
		}
		
		System.err.println(post.getContent());
		
		postService.write(post);
	}
}
