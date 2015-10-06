package khs.employee.repository;

import org.springframework.stereotype.Component;

import khs.employee.model.Employee;
import khs.service.base.JPABaseRepository;

@Component
public class EmployeeRepository extends JPABaseRepository<Employee, Long> {


}
