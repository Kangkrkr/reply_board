package com.pilot.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.ReplyBoardApplication;
import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.domain.User;
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
	
	// 이미지 업로드
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String uploadPost(MultipartRequest mr, @RequestParam("type") String type, 
			@RequestParam("content") String content, @RequestParam("password") String password, HttpSession session){

		System.out.println("type : " + type);
		
		MultipartFile photo = (MultipartFile)mr.getFile("photo");
		
		BufferedOutputStream stream = null;
		String fixedPath = null;
		
		if (null != photo && !photo.isEmpty()) {
			try {
				File imageFile = new File(ReplyBoardApplication.ROOT + "/" + photo.getOriginalFilename());
				String filePath = imageFile.getPath();
				
				// 순수 파일이름만 DB에 저장하는 방식을 취함.(뭔가 좀 이상한데...)
				fixedPath = imageFile.getPath().substring(filePath.lastIndexOf('\\') + 1, filePath.length());
				
				stream = new BufferedOutputStream(
						new FileOutputStream(imageFile));
                FileCopyUtils.copy(photo.getInputStream(), stream);
                
                // 왜인지 images 폴더를 새로고침 해줘야 이미지가 정상출력 되는 경우가 간혹 발생함.
                if(stream != null){
        			try {
        				stream.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(type.equals("post")){
			wirtePost(content, password, session, fixedPath);
		}else{
			String typeDistiction = type.split("#")[0];
			String idDistiction = type.split("#")[1];
			
			int id = toInteger(idDistiction);
			
			if(typeDistiction.equals("reply")){
				wirteReply(id, content, password, session, fixedPath);
			}else if(typeDistiction.equals("reply_on_reply")){
				wirteReplyOnReply(id, content, password, session, fixedPath);
			}
		}
		/*
		else {
			wirtePost(content, password, session, null, stream);
		}*/
		
		return "와우";
	}
	
	private void wirtePost(String content, String password, HttpSession session, String fixedPath) {
		User user = (User)session.getAttribute("userInfo");
		
		Post post = new Post();
		post.setImage(fixedPath);
		post.setContent(content);
		post.setPassword(password);
		post.setRegdate(new Date());
		post.setUser(user.getId());
		
		postService.write(post);
	}
	
	private void wirteReply(int postId, String content, String password, HttpSession session, String fixedPath) {
		User user = (User)session.getAttribute("userInfo");
		
		Post post = postService.findOne(postId);
		
		Reply reply = new Reply();
		reply.setDepth(1);
		reply.setImage(fixedPath);
		reply.setContent(content);
		reply.setPassword(password);
		reply.setRegdate(new Date());
		reply.setPost(post.getId());
		reply.setUser(user);
		
		replyService.write(reply);
	}
	
	private void wirteReplyOnReply(int replyId, String content, String password, HttpSession session, String fixedPath) {
		User user = (User)session.getAttribute("userInfo");
		
		Reply reply = replyService.findOne(replyId);
		
		Reply replyOnReply = new Reply();
		replyOnReply.setDepth(reply.getDepth() + 1);
		replyOnReply.setImage(fixedPath);
		replyOnReply.setContent(content);
		replyOnReply.setPassword(password);
		replyOnReply.setRegdate(new Date());
		replyOnReply.setUser(user);
		replyOnReply.setPost(reply.getPost());
//		replyOnReply.setReply(reply);
		
		replyService.write(replyOnReply);
	}
	
	private int toInteger(String num){
		return Integer.parseInt(num);
	}
}
