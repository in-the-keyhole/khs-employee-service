package com.keyholesoftware.employees.model;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Source.class)
public class EmployeeEntityListener {

	private static final Logger log = LogManager.getLogger(EmployeeEntityListener.class);

	void onPrePersist(Object o) {
	}

	@PostPersist
	void onPostPersist(Object o) {
	}

	@PostLoad
	void onPostLoad(Object o) {
	}

	@PreUpdate
	void onPreUpdate(Object o) {
	}

	@PostUpdate
	void onPostUpdate(Object o) {
	}

	@PreRemove
	void onPreRemove(Object o) {
	}

	@PostRemove
	void onPostRemove(Object o) {
		Employee employee = (Employee) o;
		log.info("Deletion recognized for employee id: " + employee.getOId());
		MessageChannel outputChannel = JpaApplicationContext.getBean(Source.class).output();
		outputChannel.send(MessageBuilder.withPayload("" + employee.getOId()).build());
		log.info("raised delete message for employee id: " + employee.getOId());
	}
}
