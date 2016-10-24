package com.keyholesoftware.employees;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import khs.trouble.servlet.TroubleServlet;


/**
 * @author Jaime Niswonger
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EmployeesApp implements ServletContextInitializer {

	public static void main(String[] args) {

		SpringApplication.run(EmployeesApp.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println( "Employee App #onStartup");
		ServletRegistration servletRegistration = servletContext.addServlet("trouble", TroubleServlet.class);
		servletRegistration.setInitParameter("token", "abc123");
		servletRegistration.addMapping("/trouble");
		servletRegistration.addMapping("/trouble/*");
		
		
	}

}

