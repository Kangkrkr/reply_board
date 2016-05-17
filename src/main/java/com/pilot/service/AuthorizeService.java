package com.pilot.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.pilot.entity.User;
import com.pilot.model.TokenJsonModel;
import com.pilot.model.UserModel;
import com.pilot.util.TeamUp;

@Service
public class AuthorizeService {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	public URI getAuthorizationPageUri() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("response_type", "code");
		map.add("client_id", TeamUp.CLIENT_ID);
		map.add("redirect_uri", TeamUp.REDIRECT_URI);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(TeamUp.AUTHORIZE);
		uriBuilder.queryParams(map);

		// 다음의 형태로 변환된다.
		// https://auth.tmup.com/oauth2/authorize?response_type=code&client_id=teamup-testbot&redirect_uri=http://localhost:8010/oauth2_callback
		// 위 uri로 접속하면 팀업 로그인 화면으로 접속된다. 이미 세션에 로그인정보가 담겨있다면 아래의 uri를 넘긴다.
		URI redirectUri = uriBuilder.build().encode().toUri();

		// 해당 uri로 접속하면 로그인 페이지가 뜨고, 로그인을 하게되면 다음의 redirect_uri로 아래 code가 붙여지며
		// 지정한 redirect_uri로 redirecting 된다.
		// http://localhost:8010/oauth2_callback?code=487e85d36b45d6d9129dcab89c294a04fa515dd44f7607670af021746d291f2e
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

		// 다음과 같은 token 결과를 얻을 수 있다.
		// {"access_token":"22b7e7b13dfebe7508622af81808b3a65c841ba040a289d62e1c872f6e0bbe4b","expires_in":86399,"token_type":"bearer","refresh_token":"1efc73193c6b901ec3a14453dcb2f99fbbf5c91b338b11b6d69982649f89ff7f"}
		// 얻은 json string을 model 객체에 담는다.
		RestTemplate restTemplate = new RestTemplate();
		TokenJsonModel token = restTemplate.getForObject(tokenUri, TokenJsonModel.class);
		
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
	
	public void checkUser(String tmId, String token){
		
		// 팀업서버에서 토큰으로 사용자 정보를 얻어온다.
		UserModel myInfo = getMyInform(token);
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
		
		redisService.addInfo(tmId, token);
	}
	
	public String logout(){
		
		return "";
	}
}
