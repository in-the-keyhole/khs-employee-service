package khs.employee.endpoint;

import java.util.Collection;

import khs.employee.model.Employee;
import khs.employee.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.khs.sherpa.annotation.Action;
import com.khs.sherpa.annotation.Endpoint;
import com.khs.sherpa.annotation.MethodRequest;
import com.khs.sherpa.annotation.Param;

@Endpoint(authenticated=false)
@Configurable
public class EmployeeEndpoint {
	
	@Autowired
	EmployeeService employeeService;
	
	@Action(mapping = "/employee/find/{id}", method = MethodRequest.GET)
	public Employee findForId(@Param("id") Long id) {
		return employeeService.find(Employee.class, id);
	}
	
	
	@Action(mapping = "/employee/all", method = MethodRequest.GET)
	public Collection<Employee> all() {
		return employeeService.findAll(Employee.class, null);
	}
	
	@Action(mapping = "/employee/list/{idarray}", method = MethodRequest.GET)
	public Collection<Employee> list(@Param("idarray") Long[] ids ) {
		return employeeService.findAll(Employee.class, null);
	}
	
	
	

}
