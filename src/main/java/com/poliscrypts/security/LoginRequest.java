package com.poliscrypts.security;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {

	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
	private String username;

	@NotBlank(message = "Le mot de passe est obligatoire")
	private String password;
	
	

	public LoginRequest() {
		// TODO Auto-generated constructor stub
	}

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}