package com.keyholesoftware.employees.model;
//Jaime was here
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Jaime Niswonger
 */
@Entity
@Data
public class Employee {

	@Id
	@GeneratedValue
	Long id;

	@Column(name = "lastname")
	private String firstname;
	
	@Column(name = "firstname")
	private String lastname;
	
	private String email;
}
