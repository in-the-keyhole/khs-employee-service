package com.keyholesoftware.employees.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keyholesoftware.employees.model.Employee;
import com.keyholesoftware.employees.model.EmployeeRepository;

@RestController
public class EmployeesApi {

	@Autowired
	EmployeeRepository repository;

	@RequestMapping(method = RequestMethod.GET, value = "/employees")
	ResponseEntity<Iterable<Employee>> all() {
		Iterable<Employee> employees = repository.findAll();
		return new ResponseEntity<Iterable<Employee>>(employees, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/employees/{id}")
	ResponseEntity<Employee> one(@PathVariable("id") Long id) {
		Employee employee = repository.findOne(id);
		return employee != null ? new ResponseEntity<Employee>(employee, HttpStatus.OK) : new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/employees")
	ResponseEntity<Employee> create(@RequestBody Employee Employee) {
		Employee newEmployee = repository.save(Employee);
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/employees/{id}")
	ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
		Employee originalEmployee = repository.findOne(id);
		originalEmployee.setFirstname(employee.getFirstname());
		originalEmployee.setLastname(employee.getLastname());
		originalEmployee.setEmail(employee.getEmail());

		Employee savedEmployee = repository.save(originalEmployee);

		return new ResponseEntity<Employee>(savedEmployee, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/employees/{id}")
	void update(@PathVariable Long id) {
		repository.delete(id);
	}
}
