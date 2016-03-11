package com.keyholesoftware.employees.model;

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

	private String firstname;
	private String middlename = "X";
	private String lastname;
	private String email;
}
