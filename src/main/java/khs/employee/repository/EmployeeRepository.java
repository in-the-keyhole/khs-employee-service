package khs.employee.repository;

import org.springframework.stereotype.Component;

import khs.employee.model.Employee;
import khs.service.base.JPABaseDao;

@Component
public class EmployeeRepository extends JPABaseDao<Employee, Long> {

	public EmployeeRepository() {
		super("EmployeeRepository");
	}

}
