package com.poliscrypts.security;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRefreshRequest {

	@NotBlank(message = "Vous devez fournir le nom d'utilisateur !")
	private String username;

	@NotBlank(message = "Vous devez fournir le refresh token !")
	private String refreshToken;

}
