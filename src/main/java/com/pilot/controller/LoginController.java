package com.pilot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pilot.service.AuthorizeService;

@Controller
public class LoginController {

	@Autowired
	private AuthorizeService authorizeService;
	
	// 쿠키에 해당 키값이 없을 수도 있으므로 required를 false로 줌.
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "redirect:" + authorizeService.getAuthorizationPageUri().toString();
	}
}
