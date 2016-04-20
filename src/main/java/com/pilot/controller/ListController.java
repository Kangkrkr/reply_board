package com.pilot.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.domain.Post;
import com.pilot.domain.User;
import com.pilot.service.PostService;
import com.pilot.service.UserService;

@Controller
@RequestMapping("list")
public class ListController {
	
	@Autowired
	PostService postService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(){
		return "list";
	}
	
	@RequestMapping(value = "write", method = RequestMethod.GET)
	public String write(){

		// test
		Post post = new Post();
		post.setContent("테스트용 게시물");
		post.setPassword("123");
		post.setRegdate(new Date());
		post.setUser(userService.findOne(1));
		
		postService.write(post);
		
		return "list";
	}
}
