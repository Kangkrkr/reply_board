package com.pilot.valid;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.codec.digest.DigestUtils;

import com.nhncorp.lucy.security.xss.XssPreventer;

public class LoginForm {

	@NotNull
	@Size(min = 10, max = 125)
	private String email;

	@NotNull
	@Size(min = 8, max = 1024)
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		// XSS 공격방지. -> https://github.com/naver/lucy-xss-filter
		this.email = XssPreventer.escape(email);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		// 폼으로부터 들어오는 사용자의 비밀번호를 암호화 한다.
		this.password = DigestUtils.sha512Hex(password);
	}
}
