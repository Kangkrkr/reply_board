package com.pilot.controller;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.dto.ListSizeDTO;
import com.pilot.service.PostService;
import com.pilot.service.UploadService;
import com.pilot.util.Message;
import com.pilot.valid.WriteForm;

@RestController
public class AjaxController {
	
	@Autowired 
	private PostService postService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private HttpSession session;
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public String logout(){
		
		try{
			if(null != session.getAttribute("userInfo")){
				session.invalidate();
				session = null;
				return Message.LOGOUT_SUCCESS;
			}
		}catch(Exception e){
			logger.error("logout errror", e.toString());
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
	public String upload(MultipartRequest mr, @Validated WriteForm writeForm, BindingResult result){
		if(result.hasErrors()){
			return (result.hasFieldErrors("content")) ? Message.NOTIFY_WRITE : Message.ALERT_ERROR;
		}
		
		return uploadService.upload(mr, writeForm);
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeDTO getListSize(){
		return new ListSizeDTO(postService.count(), PostService.MAX_SIZE);
	}
}
