package khs.employee.service;




import javax.annotation.PostConstruct;

import khs.employee.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MockService {
	
	
	@Autowired
	private EmployeeService employeeService;

	
	@PostConstruct
	public void init() {
		
		System.out.println("Initializing Employee Mocks");
		
		Employee emp = new Employee();
		emp.setEmail("test@test.com");
		emp.setFirstName("Clifford");
		emp.setLastName("Squidlow");
		
		employeeService.persist(emp);
		
	    emp = new Employee();
		emp.setEmail("joe@test.com");
		emp.setFirstName("Jane");
		emp.setLastName("Doe");
		
		employeeService.persist(emp);
			
		
	}
	
	

}
