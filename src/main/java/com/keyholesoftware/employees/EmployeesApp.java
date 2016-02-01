package com.keyholesoftware.employees;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Jaime Niswonger
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EmployeesApp {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApp.class, args);
	}
}
