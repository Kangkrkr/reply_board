package com.pilot.controller;


import javax.servlet.http.HttpSession;

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
import com.pilot.valid.WriteForm;

@RestController
public class AjaxController {
	
	@Autowired 
	private PostService postService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public String logout(){
		
		try{
			if(null != session.getAttribute("userInfo")){
				session.invalidate();
				session = null;
				return "로그아웃에 성공하였습니다.";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "로그아웃에 실패하였습니다.";
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(@RequestParam("id") Integer id){
		
		try{
			postService.delete(id);
			return "글 삭제에 성공하였습니다.";
		}catch(Exception e){
			e.printStackTrace();
			return "글 삭제에 실패하였습니다.";
		}
	}
	
	
	// 글 업로드
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(MultipartRequest mr, @Validated WriteForm writeForm, BindingResult result){
		if(result.hasErrors()){
			if(result.hasFieldErrors("content")){
				return "글 작성 실패 - 글자수는 2000자 이하여야합니다.";
			}else{
				// type 에러
				return "글 게시 중 에러가 발생하였습니다.";
			}
		}
		
		uploadService.upload(mr, writeForm);
		
		return "글 게시에 성공하였습니다.";
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeDTO getListSize(){
		return new ListSizeDTO(postService.count(), PostService.MAX_SIZE);
	}
}
