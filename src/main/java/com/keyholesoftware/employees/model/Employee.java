package com.keyholesoftware.employees.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Jaime Niswonger
 * 
 */
@Entity
@EntityListeners({ EmployeeEntityListener.class })
public class Employee {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Long oId;

	
	
	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	private String email;

	public Long getOId() {
		return oId;
	}

	public void setOId(Long id) {
		this.oId = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
