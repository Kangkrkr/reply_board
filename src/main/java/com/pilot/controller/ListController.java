package com.pilot.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.dto.PostDTO;
import com.pilot.entity.Post;
import com.pilot.service.PostService;

@Controller
@RequestMapping("list")
public class ListController {

	@Autowired 
	private PostService postService;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(@RequestParam("page") Integer page, Model model) {

		if (session.getAttribute("userInfo") == null) {
			return "redirect:/form/login";
		} else {
			List<Post> posts = postService.selectPost(page);
			List<PostDTO> postDTOs = postService.createPostDTOs(posts);

			model.addAttribute("posts", postDTOs);

			return "list";
		}
	}
}
