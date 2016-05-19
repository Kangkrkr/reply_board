package com.pilot.service;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.pilot.entity.User;
import com.pilot.model.TokenModel;
import com.pilot.model.UserModel;
import com.pilot.util.TeamUp;

@Service
public class AuthorizeService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisService redisService;
	
	public URI getAuthorizationPageUri() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("response_type", "code");
		map.add("client_id", TeamUp.CLIENT_ID);
		map.add("redirect_uri", TeamUp.REDIRECT_URI);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(TeamUp.AUTHORIZE);
		uriBuilder.queryParams(map);

		URI redirectUri = uriBuilder.build().encode().toUri();

		// 해당 uri로 접속하면 로그인 페이지가 뜨고, 로그인을 하게되면 지정한redirect_uri로 code가 붙여지고
		// 지정한 redirect_uri로 redirecting 된다.
		return redirectUri;
	}

	public String getToken(String code) {
		// 얻은 code를 이용해 token을 발급 받는 작업.
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "authorization_code");
		map.add("client_id", TeamUp.CLIENT_ID);
		map.add("client_secret", TeamUp.CLIENT_SECRET);
		map.add("code", code);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(TeamUp.TOKEN);
		uriBuilder.queryParams(map);

		URI tokenUri = uriBuilder.build().encode().toUri();

		// 얻은 json string을 model 객체에 담는다.
		RestTemplate restTemplate = new RestTemplate();
		TokenModel token = restTemplate.getForObject(tokenUri, TokenModel.class);
		
		return token.getAccessToken();
	}

	public UserModel getMyInform(String token){
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(TeamUp.USER);
		uriBuilder.queryParam("token", token);
		
		URI userUri = uriBuilder.build().encode().toUri();
		
		RestTemplate restTemplate = new RestTemplate();
		UserModel user = restTemplate.getForObject(userUri, UserModel.class);
		
		return user;
	}
	
	public void checkUser(String token, HttpServletRequest request){
		
		// 팀업서버에서 토큰으로 사용자 정보를 얻어온다.
		UserModel myInfo = getMyInform(token);
		myInfo.setToken(token);
		
		User user = userService.findByEmail(myInfo.getEmail());
		
		// local DB에 해당 사용자가 없다면,
		if(null == user){
			User newer = new User();
			newer.setEmail(myInfo.getEmail());
			newer.setName(myInfo.getName());
			newer.setProfileImage(myInfo.getProfileImage());
			
			// local DB에 저장후 redis에 저장.
			userService.join(newer);
		}
		
		redisService.addInfo(myInfo.getEmail(), myInfo, request);
	}
}
