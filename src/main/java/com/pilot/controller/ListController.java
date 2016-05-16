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
import com.pilot.service.PostService;

@Controller
@RequestMapping("list")
public class ListController {

	@Autowired 
	private PostService postService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	AuthorizeService authorizeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(@RequestParam("page") Integer page, @CookieValue(value = "token", required = false) String token, Model model, RedirectAttributes reAttr) {

		
		/*
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(cookie.getName() != null){
					System.err.println(" 사용자 이메일 : " + cookie.getValue());
				}
			}
		}
		*/
		
		// 첫 로그인시 쿠키는 5초간 살아있게 해놨음(시뮬레이션)
		if(null == token){
			System.err.println("사용자정보가 없음.");
		}
		
		if(page <= 1) page = 1;
		if(page >= postService.getMaxEnd()) page = postService.getMaxEnd();
		
		List<Post> posts = postService.selectPost(page);
		List<PostModel> postDTOs = postService.createPostDTOs(posts);

		model.addAttribute("userInfo", authorizeService.getMyInform(token));
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
