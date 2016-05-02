package com.pilot.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.dao.WriterImpl;
import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.dto.ListSizeDTO;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;
import com.pilot.service.UserService;
import com.pilot.util.CustomUtil;
import com.pilot.util.ExtraInfo;
import com.pilot.util.ImageUploader;
import com.pilot.validator.WriteForm;

@RestController
public class RestService {

	@Autowired
	CustomUtil util;
	
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
	
	/*
	@Transactional
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public List test(@PathParam("page") Integer page){
		
		List<Post> posts = postService.findAll();
		
		Criteria containerCriteria = util.getSession().createCriteria(Reply.class, "reply");
		// containerCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		containerCriteria.createCriteria("post", "post");
		
		containerCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		List resultList = containerCriteria.setFirstResult(page).setMaxResults(2).list();
		
		for(int i=0; i<resultList.size(); i++){
			Map<String, Object> map = resultList.get(i);
			Post post = (Post)map.get("post");
			
			Reply reply = (Reply)map.get("reply");	
		}
		
		return resultList;
	}
	*/
	
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
	public String delete(@RequestParam("type") String type, @RequestParam("postId") Integer postId){
		try{
			if(type.equals("post")){
				// 먼저 하위 댓글들을 모두 지운뒤, 게시글을 삭제한다.
				Post post = postService.findOne(postId);
				for(Reply reply : replyService.findRepliesByPost(post)){
					replyService.delete(reply);
				}
				postService.delete(post);
			}else if(type.contains("reply")){
				// 먼저 하위 댓글들을 모두 지운뒤, 자신(댓글)을 삭제한다.
				// Reply 테이블을 새로 설계해야 될듯 ...
				replyService.delete(replyService.findOne(postId));
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
			}else if(result.hasFieldErrors("password")){
				return "글 작성 실패 - 비밀번호는 8자 이상 20자 이하여야합니다.";
			}else{
				// type 에러
				return "글 게시 중 에러가 발생하였습니다.";
			}
		}
		
		String type = writeForm.getType();
		String fixedPath = ImageUploader.uploadAndSavePath(mr, writeForm);
		
		if(type.equals("post")){
			postWriter.setWriteForm(writeForm);
			postWriter.setExtraInfo(new ExtraInfo(null, session, fixedPath));
			postWriter.write();
		}else{
			Integer targetId = toInteger(writeForm.getType().split("#")[1]);
			
			writeForm.setType(writeForm.getType().split("#")[0]);
			replyWriter.setWriteForm(writeForm);
			replyWriter.setExtraInfo(new ExtraInfo(targetId, session, fixedPath));
			replyWriter.write();
		}
		
		
		return "글 게시에 성공하였습니다.";
	}
	
	@RequestMapping(value = "list_size", method = RequestMethod.GET)
	public ListSizeDTO getListSize(){
		return new ListSizeDTO(postService.findAll().size(), PostService.MAX_SIZE);
	}
	
	private int toInteger(String num){
		return Integer.parseInt(num);
	}
}
