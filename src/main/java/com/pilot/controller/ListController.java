package com.pilot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.domain.User;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;
import com.pilot.service.UserService;

@Controller
@RequestMapping("list")
public class ListController {

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;

	@Autowired
	ReplyService replyService;

	@RequestMapping(method = RequestMethod.GET)
	public String showList(Model model, HttpSession session) {

		User userInfo = (User) session.getAttribute("userInfo");

		if (userInfo == null) {
			return "redirect:/form/login";
		} else {
			List<PostDTO> posts = new ArrayList<>();

			for (Post post : postService.findAll()) {
				PostDTO dto = new PostDTO();

				dto.setPost(post);
				dto.setRepliesToPost(replyService.findAllByPost(post.getId()));

				System.out.println(dto);

				posts.add(dto);
			}

			model.addAttribute("posts", posts);

			/*
			 * SessionFactory sessionFactory =
			 * HibernateUtil.getSessionFactory(); Session sess =
			 * sessionFactory.getCurrentSession(); Criteria cre =
			 * sess.createCriteria(User.class);
			 * 
			 * ProjectionList projectionList = Projections.projectionList();
			 * projectionList.add(Projections.id().as("id"));
			 * projectionList.add(Projections.property("content").as("content"))
			 * ;
			 * 
			 * cre.setProjection(projectionList); cre.setResultTransformer(new
			 * AliasToBeanResultTransformer(User.class));
			 * 
			 * cre.list().forEach(System.out::println);
			 */

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

	// 겟터와 셋터가 있어야 타임리프에서 접근이 가능하다.
	public class PostDTO {
		private User user;
		private Post post;
		private String replySize;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Post getPost() {
			return post;
		}

		public String getReplySize() {
			return replySize;
		}

		public void setReplySize(String replySize) {
			this.replySize = replySize;
		}

		public void setPost(Post post) {
			this.post = post;
			this.user = userService.findOne(post.getUser());
		}

		public void setRepliesToPost(List<Reply> replies) {
			post.setReplies(replies);
			this.replySize = String.valueOf(replies.size());
		}
	}
}
