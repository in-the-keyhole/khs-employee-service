package com.keyholesoftware.employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Jaime Niswonger
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EmployeesApp extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApp.class, args);
	}
}
