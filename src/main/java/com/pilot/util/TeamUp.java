package com.pilot.util;

public class TeamUp {

	// client_id & client_secret
	public static final String CLIENT_ID = "teamup-testbot";
	public static final String CLIENT_SECRET = "kf97cu5iqqia4i";
	
	// redirect uri
	// .tmup.com 에서의 쿠키를 받아오기 위해 다음의 주소를 사용한다.
	public static final String REDIRECT_URI = "http://local.tmup.com/oauth2_callback";
	
	// authorize
	public static final String AUTHORIZE = "https://auth.tmup.com/oauth2/authorize";
	public static final String TOKEN = "https://auth.tmup.com/oauth2/token";
	
	// user
	public static final String USER = "https://auth.tmup.com/v1/user";
	
	// edge
	public static final String EDGE = "https://edge.tmup.com/v1";
	
	// room
	public static final String ROOM = EDGE + "/room/";
	
	// message
	public static final String MESSAGE = EDGE + "/message/";
}
