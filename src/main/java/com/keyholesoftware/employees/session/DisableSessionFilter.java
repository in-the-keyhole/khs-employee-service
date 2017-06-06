package com.keyholesoftware.employees.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

public class DisableSessionFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletDisableSessionRequestWrapper wrapper = new HttpServletDisableSessionRequestWrapper((HttpServletRequest) request);
		chain.doFilter(wrapper, response);
	}
}
