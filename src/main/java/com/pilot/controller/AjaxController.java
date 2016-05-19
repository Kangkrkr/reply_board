package com.pilot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.entity.User;
import com.pilot.model.ListSizeModel;
import com.pilot.model.WriteModel;
import com.pilot.service.AuthorizeService;
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
	private AuthorizeService authorizeService;
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Integer id){
		
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
	public String upload(MultipartRequest mr, @Validated WriteModel writeForm, BindingResult result, @CookieValue("tk") String tk){
		if(result.hasErrors()){
			return (result.hasFieldErrors("content")) ? Message.NOTIFY_WRITE : Message.ALERT_ERROR;
		}
		
		return uploadService.upload(mr, writeForm, tk);
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeModel getListSize(){
		return new ListSizeModel(postService.count(), PostService.MAX_SIZE);
	}
}
