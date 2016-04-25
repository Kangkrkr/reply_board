package com.pilot.rest;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.domain.User;
import com.pilot.repository.UserRepository;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;
import com.pilot.service.UserService;
import com.pilot.validator.WriteForm;

@RestController
public class RestService {

	@Autowired
	UserService userService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	ReplyService replyService;
	
	@RequestMapping(value = "write", method = RequestMethod.POST)
	public String write(@RequestParam("email") String email, @Validated WriteForm writeForm, BindingResult result){
		
		if(result.hasErrors()){
			if(result.hasFieldErrors("content")){
				return "실패 - 글자수는 2000자 이하여야합니다.";
			}
			if(result.hasFieldErrors("password")){
				return "실패 - 비밀번호는 8자 이상 20자 이하여야합니다.";
			}
			
			return "실패";
		}
		
		User user = userService.findByEmail(email);
		
		Post post = new Post();
		post.setContent(writeForm.getContent());
		post.setPassword(writeForm.getPassword());
		post.setRegdate(new Date());
		post.setUser(user.getId());
		
		postService.write(post);
		
		return "성공";
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public String logout(HttpSession session){
		
		if(null != session.getAttribute("userInfo")){
			session.invalidate();
			return "성공하였습니다.";
		}else{
			return "실패하였습니다.";
		}
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(@RequestParam("postId") Integer postId){
		try{
			postService.delete(postId);
			return "성공하였습니다.";
		}catch(Exception e){
			return "실패하였습니다.";
		}
	}
	
	// @RequestParam으로 받는 커맨드객체 외의 인자들은 무조건 String 형이다.
	@RequestMapping(value = "reply", method = RequestMethod.POST)
	public String reply(@RequestParam("postId") String postId, @RequestParam("userId") String userId, @Validated WriteForm writeForm, BindingResult result){
		System.out.println("댓글 쓴 유저 아이디 : " + userId);
		if(result.hasErrors()){
			if(result.hasFieldErrors("content")){
				return "실패 - 글자수는 2000자 이하여야합니다.";
			}
			if(result.hasFieldErrors("password")){
				return "실패 - 비밀번호는 8자 이상 20자 이하여야합니다.";
			}
			
			return "실패";
		}
		
		Post post = postService.findOne(Integer.parseInt(postId));
		
		Reply reply = new Reply();
		reply.setContent(writeForm.getContent());
		reply.setPassword(writeForm.getPassword());
		reply.setRegdate(new Date());
//		reply.setReply_user(userService.findOne(Integer.parseInt(userId)));
		reply.setPost(post.getId());
		
		replyService.write(reply);
		
		return "성공";
	}
	
	/*
	// 이미지 업로드
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String handleImageUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
		
	}
	*/
}
