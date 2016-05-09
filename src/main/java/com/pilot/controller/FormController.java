package com.pilot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.service.UserService;
import com.pilot.valid.JoinForm;
import com.pilot.valid.LoginForm;

@Controller
@RequestMapping("/form")
public class FormController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {
		return (session.getAttribute("useInfo") != null) ? "redirect:/list" : "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginProcess(@Validated LoginForm loginForm, Model model) {

		try{
			// 로그인 처리..
			session.setAttribute("userInfo", userService.login(loginForm));
			
			return "redirect:/list?page=1";
		}catch(Exception e){
			// printStackTrace를 하지말고 로그에 남겨두기..
			e.printStackTrace();
			return "error/login_error";
		}
	}

	@RequestMapping(value = "join", method = RequestMethod.GET)
	public String join() {
		return "join";
	}
	
	@RequestMapping(value = "join", method = RequestMethod.POST)
	public String joinProcess(@Validated JoinForm joinForm) {
		
		try{
			userService.join(joinForm);
			return "redirect:login";
		}catch(Exception e){
			return "error/join_error";
		}
	}
}
