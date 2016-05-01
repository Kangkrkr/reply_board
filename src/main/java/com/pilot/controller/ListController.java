package com.pilot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.domain.Post;
import com.pilot.domain.User;
import com.pilot.dto.PostDTO;
import com.pilot.service.PostService;
import com.pilot.service.UserService;
import com.pilot.util.HibernateUtil;

@Controller
@RequestMapping("list")
public class ListController {

	private static final int SIZE = 10;

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String showList(@PathParam("page") Integer page, Model model, HttpSession session) {

		page = (page == null || page < 0) ? 0 : page;

		User userInfo = (User) session.getAttribute("userInfo");

		if (userInfo == null) {
			return "redirect:/form/login";
		} else {

			/*
			 * for (Post post : postService.findAll()) { PostDTO dto = new
			 * PostDTO();
			 * 
			 * dto.setPost(post);
			 * dto.setRepliesToPost(replyService.findAllByPost(post.getId()));
			 * 
			 * System.out.println(dto);
			 * 
			 * posts.add(dto); }
			 */

			// Pageable pageable = new PageRequest(page, 10);
			
			// selectPost()의 첫번째 인자는 쿼리결과의 시작점을 의미. 페이지마다 SIZE 갯수 만큼씩 건너뛰게 한다.
			// 두번째 인자는 한페이지당 출력할 갯수를 의미.
			int first = (page * SIZE);

			// Data Transfer Object를 담을 Collection 객체 생성.
			List<Post> posts = postService.selectPost(first, SIZE);
			List<PostDTO> postDTOs = new ArrayList<>();
			
			if(null != posts){
				for (Post post : postService.selectPost(first, SIZE)) {
					PostDTO dto = new PostDTO();
					dto.setPost(post);

					postDTOs.add(dto);
				}
			}

			model.addAttribute("posts", postDTOs);

			/*
			 * Post post = postService.findOne(1);
			 * replyService.findAllByPost(post).forEach(System.out::println);
			 */

			/*
			 * for(Reply reply : post.getReplies()){ System.out.println(reply);
			 * }
			 */

			return "list";
		}
	}
}
