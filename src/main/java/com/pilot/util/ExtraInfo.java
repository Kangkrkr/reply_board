package com.pilot.util;

import javax.servlet.http.HttpSession;

import com.pilot.entity.User;

import lombok.Data;

@Data
public class ExtraInfo {
	private Integer targetId;
	private User uploader;
	private String fixedPath;
	
	public ExtraInfo(Integer targetId, HttpSession session, String fixedPath) {
		this.targetId = targetId;
		this.uploader = (User)session.getAttribute("userInfo");
		this.fixedPath = fixedPath;
	}
}
