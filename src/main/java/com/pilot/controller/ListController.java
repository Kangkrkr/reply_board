package com.pilot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("list")
public class ListController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String showList(){
		return "list";
	}
	
}
