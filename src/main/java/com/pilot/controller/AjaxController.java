package com.pilot.controller;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.entity.User;
import com.pilot.form.WriteForm;
import com.pilot.model.ListSizeModel;
import com.pilot.service.PostService;
import com.pilot.service.UploadService;
import com.pilot.service.UserService;
import com.pilot.util.Message;

@RestController
public class AjaxController {
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private PostService postService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	HttpSession session;
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(@RequestParam("id") Integer id){
		
		try{
			postService.delete(id);
			return Message.DELETE_SUCCESS;
		}catch(Exception e){
			logger.error("delete error", e.toString());
			return Message.DELETE_FAILED;
		}
	}
	
	// 닉네임 설정
	@RequestMapping(value = "/nickname", method = RequestMethod.POST)
	public String nickname(@RequestParam("email") String email, @RequestParam("nickname") String nickname){
		User user = userService.findByEmail(email);
		return userService.setNickname(nickname, user);
	}
	
	// 글 업로드
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(MultipartRequest mr, @Validated WriteForm writeForm, BindingResult result, @CookieValue("tmid") String tmid){
		if(result.hasErrors()){
			return (result.hasFieldErrors("content")) ? Message.NOTIFY_WRITE : Message.ALERT_ERROR;
		}
		
		return uploadService.upload(mr, writeForm, tmid);
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeModel getListSize(){
		return new ListSizeModel(postService.count(), PostService.MAX_SIZE);
	}
}
