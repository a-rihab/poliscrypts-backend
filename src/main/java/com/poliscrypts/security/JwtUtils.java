package com.poliscrypts.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.poliscrypts.model.User;
import com.poliscrypts.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:application.properties")
@Slf4j
@Component
public class JwtUtils {

	@Value("${com.poliscrypts.jwtSecret}")
	private String jwtSecret;

	@Value("${com.poliscrypts.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${com.poliscrypts.jwtRefreshExpirationMs}")
	private int jwtRefreshExpirationMs;

	@Autowired
	private UserRepository userRepository;

	public String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

	public String generateJwtToken(Authentication authentication, int expiration) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		Map<String, Object> claims = new HashMap<>();

		Collection<? extends GrantedAuthority> roles = userPrincipal.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("ADMIN", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			claims.put("USER", true);
		}

		String token = Jwts.builder().setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

		return token;
	}

	public String generateRefreshToken(Authentication authentication) {
		return generateJwtToken(authentication, jwtRefreshExpirationMs);
	}

	public String generateAccessToken(Authentication authentication) {
		return generateJwtToken(authentication, jwtExpirationMs);
	}

	public Date getExpiration(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateSubject(String token) {

		String username = getUserNameFromJwtToken(token);
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		boolean isTokenExpired = claims.getExpiration().before(new Date());
		System.out.println(claims.getExpiration());
		Optional<User> optional = userRepository.findByUsername(username);
		if (optional.isPresent() && optional.get().getActive() == 1 && !isTokenExpired) {
			return true;
		}

		return false;
	}

	public boolean validateJwtRefreshToken(String refreshToken) {
		try {
			return validateSubject(refreshToken);
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public boolean validateJwtToken(String authToken) {
		try {
			return validateSubject(authToken);
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}