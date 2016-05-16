package com.pilot.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

import com.pilot.form.WriteForm;
import com.pilot.model.ListSizeModel;
import com.pilot.service.AuthorizeService;
import com.pilot.service.PostService;
import com.pilot.service.UploadService;
import com.pilot.util.Message;

@RestController
public class AjaxController {
	
	@Autowired 
	private PostService postService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private AuthorizeService authorizeService; 
	
	@Autowired
	private HttpSession session;
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public String logout(@CookieValue(value = "token", required = false) String token, HttpServletRequest request){
		
		// https://tmup.com/signout?token=토큰 접속시 로그아웃 가능.
		System.err.println("로그아웃 하려는 사용자의 토큰 : " + token);
		System.err.println("로그아웃 하려는 사용자 : " + authorizeService.getMyInform(token));
		
		try{
			if(null != token){
				System.err.println("로그아웃합니다 ?");
				//session.invalidate();
				Cookie[] cookies = request.getCookies();
				if(cookies != null){
					for(Cookie cookie : cookies){
						if(token.equals(cookie.getValue())){
							cookie.setMaxAge(0);
						}
					}
				}
				
				return Message.LOGOUT_SUCCESS;
			}
		}catch(Exception e){
			logger.error("logout errror", e.toString());
			e.printStackTrace();
		}
		
		return Message.LOGOUT_FAILED;
	}
	
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
	
	
	// 글 업로드
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(MultipartRequest mr, @Validated WriteForm writeForm, BindingResult result, @CookieValue("token") String token){
		if(result.hasErrors()){
			return (result.hasFieldErrors("content")) ? Message.NOTIFY_WRITE : Message.ALERT_ERROR;
		}
		
		return uploadService.upload(mr, writeForm, token);
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeModel getListSize(){
		return new ListSizeModel(postService.count(), PostService.MAX_SIZE);
	}
}
