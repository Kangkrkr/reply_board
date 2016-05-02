package com.pilot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.domain.User;
import com.pilot.dto.PostDTO;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;
import com.pilot.service.UserService;
import com.pilot.util.SessionUtil;

@Controller
@RequestMapping("list")
public class ListController {

	private static final int SIZE = 3;

	@Autowired
	ReplyService replyService;
	
	@Autowired
	PostService postService;

	@Autowired
	UserService userService;
	
	@Autowired
	SessionUtil util;

	@RequestMapping(method = RequestMethod.GET)
	public String showList(@PathParam("page") Integer page, Model model, HttpSession session) {

		page = (page == null || page < 0) ? 0 : page;

		User userInfo = (User) session.getAttribute("userInfo");

		if (userInfo == null) {
			return "redirect:/form/login";
		} else {

			// selectPost()의 첫번째 인자는 쿼리결과의 시작점을 의미. 페이지마다 SIZE 갯수 만큼씩 건너뛰게 한다.
			// 두번째 인자는 한페이지당 출력할 갯수를 의미.
			int first = (page * SIZE);

			// Data Transfer Object를 담을 Collection 객체 생성.
			List<Post> posts = postService.selectPost(first, SIZE);
			List<PostDTO> postDTOs = new ArrayList<>();
			
			if(null != posts){
				for (Post post : posts) {
					PostDTO dto = new PostDTO();
					dto.setPost(post);
					
					List<Reply> replies = replyService.findRepliesByPost(post);
					dto.setReplies(replies);
					
					postDTOs.add(dto);
				}
			}

			model.addAttribute("posts", postDTOs);

			return "list";
		}
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam("type") String type, @RequestParam("postId") Integer postId, Model model){
		try{
			
			if(type.equals("post")){
				Post post = postService.findOne(postId);
				model.addAttribute("post", post);
			}else{
				Reply reply = replyService.findOne(postId);
				model.addAttribute("reply", reply);
			}
			
			return (type.equals("post") ? "edit_post" : "edit_reply");
		}catch(Exception e){
			e.printStackTrace();
			return "list";
		}
	}
}
