package com.keyholesoftware.employees.security;

import org.springframework.security.core.Authentication;

public interface JWTVerificationStrategy {
	
	public Authentication verify(String jwt);
}
