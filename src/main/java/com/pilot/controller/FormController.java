package com.pilot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.entity.User;
import com.pilot.service.UserService;
import com.pilot.validator.JoinForm;
import com.pilot.validator.LoginForm;

@Controller
@RequestMapping("/form")
public class FormController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpSession session, HttpServletRequest req) {
		User userInfo = (User)session.getAttribute("userInfo");
		return (userInfo != null) ? "redirect:/list" : "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginProcess(@Validated LoginForm loginForm, BindingResult result, HttpSession session) {

		try{
			if (result.hasErrors()) {
				System.err.println("잘못된 데이터를 입력함.");
				return "redirect:login";
			}

			// 로그인 검증..
			User user = userService.login(loginForm);
			
			if (user == null) {
				System.err.println("해당 사용자 정보가 없음.");
				return "redirect:login";
			}
			
			session.setAttribute("userInfo", user);
			
			return "redirect:/list";
		}catch(Exception e){
			e.printStackTrace();
			return "error/login_error";
		}

		
	}

	@RequestMapping(value = "join", method = RequestMethod.GET)
	public String join() {
		return "join";
	}
	
	@RequestMapping(value = "join", method = RequestMethod.POST)
	public String joinProcess(@Validated JoinForm joinForm, BindingResult result) {
		
		try{
			if(result.hasErrors()){
				return join();
			}
			
			User user = new User();
			user.setEmail(joinForm.getEmail());
			user.setName(joinForm.getName());
			user.setPassword(joinForm.getPassword());
			
			userService.join(user);
			
			return "redirect:login";
		}catch(Exception e){
			return "error/join_error";
		}
	}
}
