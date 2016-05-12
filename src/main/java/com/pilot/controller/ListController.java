package com.pilot.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pilot.entity.Post;
import com.pilot.model.PostModel;
import com.pilot.service.PostService;
import com.pilot.util.Message;

@Controller
@RequestMapping("list")
public class ListController {

	@Autowired 
	private PostService postService;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(@RequestParam("page") Integer page, Model model, RedirectAttributes reAttr) {

		if (session.getAttribute("userInfo") == null) {
			reAttr.addFlashAttribute("message", Message.INVALID_ACCESS);
			return "redirect:/form/login";
		} else {
			
			// service에서 바로 PostDTO List를 반환.
			List<Post> posts = postService.selectPost(page);
			List<PostModel> postDTOs = postService.createPostDTOs(posts);

			model.addAttribute("posts", postDTOs);

			return "list";
		}
	}
}
