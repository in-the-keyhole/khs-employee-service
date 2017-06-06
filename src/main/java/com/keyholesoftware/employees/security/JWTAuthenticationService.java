package com.keyholesoftware.employees.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
class JWTAuthenticationService {

	static final String AUTH_HEADER = "Authorization";

	@Autowired
	private JWTVerificationStrategy jwtVerificationStrategy;

	public Authentication authenticate(HttpServletRequest request) {
		String jwt = request.getHeader(AUTH_HEADER);
		return jwtVerificationStrategy.verify(jwt);
	}
}