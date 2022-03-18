package com.poliscrypts.security;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
	private String username;

	@NotBlank(message = "Le mot de passe est obligatoire")
	private String password;

}