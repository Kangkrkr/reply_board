package com.pilot.util;

public class Message {
	
	// upload
	public static final String POST_UPLOAD_SUCCESS = "글 게시에 성공하였습니다.";
	public static final String REPLY_UPLOAD_SUCCESS = "답글 게시에 성공하였습니다.";
	public static final String EDIT_SUCCESS = "글 수정에 성공하였습니다.";
	public static final String ALERT_ERROR = "오류가 발생하였습니다.";
	public static final String NOTIFY_WRITE = "글 작성 실패 - 글자수는 2000자 이하여야합니다.";

	// common
	public static final String INVALID_ACCESS = "잘못된 접근입니다.";
	
	// login
	public static final String LOGIN_SUCCESS = "로그인에 성공하였습니다.";
	public static final String LOGIN_FAILED = "아이디 혹은 패스워드가 틀렸습니다. ";
	
	// join
	public static final String JOIN_SUCCESS = "회원가입에 성공하였습니다.";
	public static final String JOIN_INVALID_INFO = "이름은 2글자, 암호는 8자 이상이어야 합니다.";
	public static final String JOIN_FAILED = "회원가입에 실패하였습니다.";
	
	// logout
	public static final String LOGOUT_SUCCESS = "로그아웃에 성공하였습니다.";
	public static final String LOGOUT_FAILED = "로그아웃에 실패하였습니다.";
	
	// delete
	public static final String DELETE_SUCCESS = "글 삭제에 성공하였습니다.";
	public static final String DELETE_FAILED = "글 삭제에 실패하였습니다.";
}
