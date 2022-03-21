package com.poliscrypts.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private String username;
	private List<String> roles;

}