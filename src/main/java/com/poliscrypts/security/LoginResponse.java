package com.poliscrypts.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private String username;
	private List<String> roles;

	public LoginResponse(String accessToken, String refreshToken, String username, List<String> roles) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.username = username;
		this.roles = roles;
	}

}