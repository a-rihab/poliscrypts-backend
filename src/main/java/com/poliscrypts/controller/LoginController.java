package com.poliscrypts.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poliscrypts.security.JwtUtils;
import com.poliscrypts.security.LoginRefreshRequest;
import com.poliscrypts.security.LoginRequest;
import com.poliscrypts.security.LoginResponse;
import com.poliscrypts.service.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Operation(summary = "login page")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateAccessToken(authentication);
		String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);
		return ResponseEntity.ok(new LoginResponse(jwt, jwtRefreshToken));
	}

	@Operation(summary = "login page")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAuthentification(@RequestBody LoginRefreshRequest refreshRequest,
			HttpServletRequest request) {

		if (jwtUtils.validateJwtRefreshToken(refreshRequest.getRefreshToken())) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(refreshRequest.getUsername());
			if (userDetails != null) {

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateAccessToken(authentication);
				String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);
				return ResponseEntity.ok(new LoginResponse(jwt, jwtRefreshToken));
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} else
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}
}