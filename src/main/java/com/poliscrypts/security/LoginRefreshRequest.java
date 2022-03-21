package com.poliscrypts.security;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRefreshRequest {

	@NotBlank(message = "Vous devez fournir le nom d'utilisateur !")
	private String username;

	@NotBlank(message = "Vous devez fournir le refresh token !")
	private String refreshToken;

}
