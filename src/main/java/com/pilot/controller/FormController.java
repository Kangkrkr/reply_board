package com.pilot.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	public String login() {
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginProcess(@Validated LoginForm loginForm, BindingResult result) {

		if (result.hasErrors()) {
			System.out.println("올바른 로그인 정보가 들어오지 않았음.");
			return login();
		}

		User user = new User();
		BeanUtils.copyProperties(loginForm, user);

		int ret = userService.login(user);
		System.out.println("로그인 결과 : " + ret);
		if (ret < 1) {
			return login();
		}

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
