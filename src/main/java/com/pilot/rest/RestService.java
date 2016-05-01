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

import com.pilot.dao.WriterImpl;
import com.pilot.service.PostService;
import com.pilot.service.UserService;
import com.pilot.util.ExtraInfo;
import com.pilot.util.ImageUploader;
import com.pilot.validator.WriteForm;

@RestController
public class RestService {

	@Autowired
	UserService userService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	WriterImpl postWriter;
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public String logout(HttpSession session){
		
		try{
			if(null != session.getAttribute("userInfo")){
				session.invalidate();
				session = null;
				return "로그인에 성공하였습니다.";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "로그인에 실패하였습니다.";
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(@RequestParam("type") String type, @RequestParam("postId") Integer postId){
		try{
			if(type.equals("post")){
				// 먼저 하위 댓글들을 모두 지운뒤, 게시글을 삭제한다.
				postService.delete(postService.findOne(postId));
			}else if(type.equals("reply")){
				// 먼저 하위 댓글들을 모두 지운뒤, 자신(댓글)을 삭제한다.
			}
			
			return "글 삭제에 성공하였습니다.";
		}catch(Exception e){
			e.printStackTrace();
			return "글 삭제에 실패하였습니다.";
		}
	}
	
	// 글 업로드
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String uploadPost(MultipartRequest mr, @Validated WriteForm writeForm, BindingResult result, HttpSession session){

		if(result.hasErrors()){
			if(result.hasFieldErrors("content")){
				return "글 작성 실패 - 글자수는 2000자 이하여야합니다.";
			}else if(result.hasFieldErrors("password")){
				return "글 작성 실패 - 비밀번호는 8자 이상 20자 이하여야합니다.";
			}else{
				// type 에러
				return "글 게시 중 에러가 발생하였습니다.";
			}
		}
		
		String fixedPath = ImageUploader.uploadAndSavePath(mr, writeForm);
		postWriter.setWriteForm(writeForm);
		postWriter.setExtraInfo(new ExtraInfo(null, session, fixedPath));
		postWriter.write();
		
		return "글 게시에 성공하였습니다.";
	}
	
	private int toInteger(String num){
		return Integer.parseInt(num);
	}
}
