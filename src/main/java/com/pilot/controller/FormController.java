package com.pilot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.domain.User;
import com.pilot.service.UserService;
import com.pilot.validator.JoinForm;
import com.pilot.validator.LoginForm;

@Controller
@RequestMapping("/form")
public class FormController {

	@Autowired
	private UserService userService;

	@ModelAttribute("loginForm")
	public LoginForm setLoginForm() {
		return new LoginForm();
	}
	
	@ModelAttribute("joinForm")
	public JoinForm setJoinForm() {
		return new JoinForm();
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpSession session, HttpServletRequest req) {
		User userInfo = (User)session.getAttribute("userInfo");
		return (userInfo != null) ? "redirect:/list" : "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginProcess(@Validated LoginForm loginForm, BindingResult result, HttpSession session) {

		if (result.hasErrors()) {
			System.out.println("올바른 로그인 정보가 들어오지 않았음.");
			return "redirect:login";
		}

		User user = userService.findByEmail(loginForm.getEmail());
		if (userService.login(user) < 1) {
			return "redirect:login";
		}
		
		// 보안 관련 기능이 추가되지 않은 관계로, 임시로 세션을 이용함.
		session.setAttribute("userInfo", user);

		return "redirect:/list";
	}

	@RequestMapping(value = "join", method = RequestMethod.GET)
	public String join() {
		return "join";
	}
	
	@RequestMapping(value = "join", method = RequestMethod.POST)
	public String joinProcess(@Validated JoinForm joinForm, BindingResult result) {
		
		if(result.hasErrors()){
			System.out.println("회원가입시 올바르지 못한 정보를 입력함.");
			return join();
		}
		
		User user = new User();
		BeanUtils.copyProperties(joinForm, user);
		
		userService.join(user);
		
		return "redirect:login";
	}
}
