package com.keyholesoftware.employees.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class HttpServletDisableSessionRequestWrapper extends HttpServletRequestWrapper {

	public HttpServletDisableSessionRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public HttpSession getSession() {
		throw new UnsupportedOperationException("Using HttpSession is not allowed.");
	}

	@Override
	public HttpSession getSession(boolean create) {
		throw new UnsupportedOperationException("Using HttpSession is not allowed");
	}
}
