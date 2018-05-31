package com.keyholesoftware.employees.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean {

	private static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private JWTAuthenticationService authenticationService;

	public JWTAuthenticationFilter(JWTAuthenticationService jwtAuthenticationService) {
		this.authenticationService = jwtAuthenticationService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication = authenticationService.authenticate((HttpServletRequest) request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		if (authentication == null || !authentication.isAuthenticated()) {
			log.error("5 ALARM FIRE: AN INVALID JWT WAS PASSED FROM THE EDGE");
			((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value());
		} else {
			filterChain.doFilter(request, response);
		}
	}
}
