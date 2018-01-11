package com.keyholesoftware.employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.stream.binding.OutputBindingLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jaime Niswonger
 */
//@PublishSwagger
@SpringBootApplication
@EnableCircuitBreaker
//@EnableApiStatistics
public class EmployeesApp {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApp.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		return restTemplate;
	}

	/**
	 * This is only here to allow running the application locally without
	 * Kafka/Zookeeper running
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.cloud.stream.auto-startup", havingValue = "false")
	OutputBindingLifecycle outputBindingLifecycle() {
		OutputBindingLifecycle lc = new OutputBindingLifecycle() {
			public boolean isAutoStartup() {
				return false;
			};
		};
		return lc;
	}
}