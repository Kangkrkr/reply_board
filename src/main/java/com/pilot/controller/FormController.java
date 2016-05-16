package com.pilot.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pilot.form.JoinForm;
import com.pilot.form.LoginForm;
import com.pilot.service.AuthorizeService;
import com.pilot.service.UserService;
import com.pilot.util.Message;

@Controller
@RequestMapping("/form")
public class FormController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private HttpSession session;
	
	private static final Logger logger = LoggerFactory.getLogger(FormController.class);
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {
		return "redirect:" + authorizeService.getAuthorizationPageUri().toString();
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginProcess(@Validated LoginForm loginForm, Model model, RedirectAttributes reAttr) {

		try{
			/*
			List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
			converters.add(new FormHttpMessageConverter());
			converters.add(new StringHttpMessageConverter());
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setMessageConverters(converters);
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("response_type", "code");
			map.add("client_id", client_id);
			map.add("redirect_uri", redirect_uri);
			
			String result = restTemplate.postForObject("https://auth.tmup.com/oauth2/authorize", map, String.class);
			System.err.println(result);
			*/
			
			// 로그인 처리..
			session.setAttribute("userInfo", userService.login(loginForm));
			reAttr.addFlashAttribute("message", Message.LOGIN_SUCCESS);
			return "redirect:/list?page=1";
		}catch(Exception e){
			logger.error(e.toString());
			model.addAttribute("message", Message.LOGIN_FAILED);
			return "login";
		}
	}

	@RequestMapping(value = "join", method = RequestMethod.GET)
	public String join() {
		return "join";
	}
	
	@RequestMapping(value = "join", method = RequestMethod.POST)
	public String joinProcess(@Validated JoinForm joinForm, BindingResult result, Model model, RedirectAttributes reAttr) {
		
		try{
			if(result.hasErrors()){
				model.addAttribute("message", Message.JOIN_INVALID_INFO);
				return "join";
			}
			
			userService.join(joinForm);
			reAttr.addFlashAttribute("message", Message.JOIN_SUCCESS);
			return "redirect:login";
		}catch(Exception e){
			logger.error(e.toString());
			model.addAttribute("message", Message.JOIN_FAILED);
			return "join";
		}
	}
}
