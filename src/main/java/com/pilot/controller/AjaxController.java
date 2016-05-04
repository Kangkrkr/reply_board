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
import com.pilot.service.ReplyService;
import com.pilot.util.ExtraInfo;
import com.pilot.util.ImageUploader;
import com.pilot.validator.WriteForm;

@RestController
public class AjaxController {
	
	@Autowired 
	private PostService postService;
	
	@Autowired 
	private ReplyService replyService;
	
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public String logout(HttpSession session){
		
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
	public String delete(@RequestParam("type") String type, @RequestParam("postId") Integer id){
		try{
			if(type.equals("post")){
				postService.delete(id);
			}else if(type.contains("reply")){
				replyService.delete(id);
			}else{
				
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
			}else{
				// type 에러
				return "글 게시 중 에러가 발생하였습니다.";
			}
		}
		
		String type = writeForm.getType();
		String fixedPath = ImageUploader.uploadAndSavePath(mr, writeForm);
		
		if(type.equals("post")){
			postService.setWriteForm(writeForm);
			postService.setExtraInfo(new ExtraInfo(null, session, fixedPath));
			postService.write();
		}else if(type.startsWith("reply")){
			Integer targetId = toInteger(writeForm.getType().split("#")[1]);
			
			writeForm.setType(writeForm.getType().split("#")[0]);
			replyService.setWriteForm(writeForm);
			replyService.setExtraInfo(new ExtraInfo(targetId, session, fixedPath));
			replyService.write();
		}else{
			if(type.contains("edit_post")){
				// 게시물 수정처리
				postService.update(writeForm, session, fixedPath);
			}else{
				// 댓글 수정처리
				replyService.update(writeForm, session, fixedPath);
			}
		}
		
		
		return "글 게시에 성공하였습니다.";
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeDTO getListSize(){
		return new ListSizeDTO(postService.count(), PostService.MAX_SIZE);
	}
	
	private int toInteger(String num){
		return Integer.parseInt(num);
	}
}
