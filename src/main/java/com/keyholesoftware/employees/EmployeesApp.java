package com.keyholesoftware.employees;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import khs.trouble.servlet.TroubleServlet;

/**
 * @author Jaime Niswonger
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EmployeesApp implements ServletContextInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeesApp.class);

	public static void main(String[] args) {

		SpringApplication.run(EmployeesApp.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		LOG.debug("Employee App #onStartup");
		ServletRegistration servletRegistration = servletContext.addServlet("trouble", TroubleServlet.class);
		servletRegistration.setInitParameter("token", "abc123");
		servletRegistration.addMapping("/trouble");
		servletRegistration.addMapping("/trouble/*");
	}
}