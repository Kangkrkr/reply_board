package com.pilot.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pilot.entity.Post;
import com.pilot.model.PostModel;
import com.pilot.service.AuthorizeService;
import com.pilot.service.ChatService;
import com.pilot.service.PostService;
import com.pilot.service.UserService;

@Controller
@RequestMapping("list")
public class ListController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private PostService postService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private ChatService chatService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(@RequestParam("page") Integer page, @CookieValue(value = "token", required = false) String token, Model model, RedirectAttributes reAttr) {

		if(null == token){
			System.err.println("사용자정보가 없음.");
		}
		
		if(page <= 1) page = 1;
		if(page >= postService.getMaxEnd()) page = postService.getMaxEnd();
		
		List<Post> posts = postService.selectPost(page);
		List<PostModel> postDTOs = postService.createPostDTOs(posts);

		model.addAttribute("userInfo", userService.findByEmail(authorizeService.getMyInform(token).getEmail()));
		model.addAttribute("posts", postDTOs);

		return "list";
		
		/*
		if (session.getAttribute("userInfo") == null) {
			reAttr.addFlashAttribute("message", Message.INVALID_ACCESS);
			return "redirect:/form/login";
		} else {
			List<Post> posts = postService.selectPost(page);
			List<PostModel> postDTOs = postService.createPostDTOs(posts);

			model.addAttribute("posts", postDTOs);

			return "list";
		}
		*/
	}
}
