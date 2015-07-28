package khs.employee.repository;

import khs.employee.model.Employee;
import khs.service.base.JPABaseDao;

import org.springframework.stereotype.Component;

@Component
public class EmployeeRepository extends JPABaseDao<Employee,Long> {

	
}
