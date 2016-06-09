package com.keyholesoftware.employees.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyholesoftware.employees.model.Employee;
import com.keyholesoftware.employees.model.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository repository;

	public Iterable<Employee> all() {
		return repository.findAll();
	}

	public Employee one(Long id) {
		return repository.findOne(id);
	}

	public Employee save(Employee employee) {
		return repository.save(employee);
	}

	public void delete(Long id) {
		repository.delete(id);
	}
}
