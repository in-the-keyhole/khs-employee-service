package com.keyholesoftware.employees.security;

import org.springframework.security.core.Authentication;

public interface JWTVerificationStrategy {
	
	static final String BEARER = "Bearer";

	public Authentication verify(String jwt);
}
