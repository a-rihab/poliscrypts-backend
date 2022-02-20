package com.poliscrypts.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRefreshRequest {
	private String username;
	private String refreshToken;
}
