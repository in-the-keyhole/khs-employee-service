package com.keyholesoftware.employees.model;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class is necessary for the JPA Entity listener to access Spring Beans
 * 
 * @author dpitt
 *
 */
@Component
public class JpaApplicationContext implements ApplicationContextAware {

	private static ApplicationContext appContext;

	public void setApplicationContext(final ApplicationContext context) {
		appContext = context;
	}

	public static <T> T getBean(Class<T> clazz) {
		return appContext.getBean(clazz);
	}
}
