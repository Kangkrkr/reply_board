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
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.domain.Post;
import com.pilot.domain.User;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;
import com.pilot.service.UserService;
import com.pilot.util.ExtraInfo;
import com.pilot.util.ImageUploader;
import com.pilot.util.WriterImpl;
import com.pilot.validator.WriteForm;

@RestController
public class RestService {

	@Autowired
	UserService userService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	ReplyService replyService;
	
	@Autowired
	WriterImpl postWriter;

	@Autowired
	WriterImpl replyWriter;
	
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
			session = null;
			return "성공하였습니다.";
		}else{
			return "실패하였습니다.";
		}
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(@RequestParam("type") String type, @RequestParam("postId") Integer postId){
		try{
			if(type.equals("post")){
				// 먼저 하위 댓글들을 모두 지운뒤, 게시글을 삭제한다.
				replyService.deleteByPost(postId);
				postService.delete(postId);
			}else if(type.equals("reply")){
				// 먼저 하위 댓글들을 모두 지운뒤, 자신(댓글)을 삭제한다.
				replyService.delete(postId);
			}
			
			return "성공하였습니다.";
		}catch(Exception e){
			return "실패하였습니다.";
		}
	}
	
	// 글 업로드
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String uploadPost(MultipartRequest mr, @Validated WriteForm writeForm, HttpSession session){

		String fixedPath = ImageUploader.uploadAndSavePath(mr, writeForm);
		
		String distictions[] = writeForm.getType().split("#");
		
		for(String s : distictions){
			System.out.println(s);
		}
		
		String typeDistinction = distictions[0];
		
		if(typeDistinction.equals("post")){
			postWriter.setWriteForm(writeForm);
			postWriter.setExtraInfo(new ExtraInfo(null, session, fixedPath));
			postWriter.write();
		}else{
			if(typeDistinction.contains("reply")){
				replyWriter.setWriteForm(writeForm);
				replyWriter.setExtraInfo(new ExtraInfo(toInteger(distictions[1]), session, fixedPath));
				replyWriter.write();
			}
		}
		
		return "성공";
	}
	
	private int toInteger(String num){
		return Integer.parseInt(num);
	}
}
