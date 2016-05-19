package com.pilot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.entity.Post;
import com.pilot.model.PostModel;
import com.pilot.service.AuthorizeService;
import com.pilot.service.PostService;
import com.pilot.service.RedisService;

@Controller
@RequestMapping("/list")
public class ListController {

	@Autowired 
	private PostService postService;
	
	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(@RequestParam("page") Integer page, @CookieValue(value = "tk", required = false) String tk, Model model) {

		// 쿠키 만료시 다시 인증시키게 한다.
		if(null == tk){
			return "redirect:" + authorizeService.getAuthorizationPageUri().toString();
		}
		
		if(page <= 1) page = 1;
		if(page >= postService.getMaxEnd()) page = postService.getMaxEnd();
		
		List<Post> posts = postService.selectPost(page);
		List<PostModel> postDTOs = postService.createPostDTOs(posts);

		// redis에서 tmid 키로 사용자 정보를 가져온다.
		model.addAttribute("userInfo", redisService.getUserByEmail(authorizeService.getMyInform(tk).getEmail()));
		model.addAttribute("posts", postDTOs);

		return "list";
	}
}
