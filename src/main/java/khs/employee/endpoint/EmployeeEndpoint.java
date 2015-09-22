package khs.employee.endpoint;

import java.util.Collection;

import khs.employee.model.Employee;
import khs.employee.service.EmployeeService;
import khs.service.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import com.khs.sherpa.annotation.Action;
import com.khs.sherpa.annotation.Endpoint;
import com.khs.sherpa.annotation.MethodRequest;
import com.khs.sherpa.annotation.Param;

@Endpoint(authenticated=true)
@Configurable
public class EmployeeEndpoint {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	MessagingService messagingService;
	
	
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
	
	
	@Action(mapping = "/employee/save", method = MethodRequest.POST)
	public Employee persist(Employee employee) {
		return employeeService.persist(employee);
	}
	
	@Action(mapping = "/employee/save", method = MethodRequest.PUT)
	public Employee merge(Employee employee) {
		
		Employee emp = employeeService.merge(employee);
	
		return emp;
	}
	
	@Action(mapping = "/employee/save", method = MethodRequest.DELETE)
	public boolean remove(Employee employee) {
		employeeService.remove(employee);
		messagingService.produce("employee.delete", "id:"+employee.getId());
		return true;		
	}
	
	@Action(mapping = "/employee/delete/{id}", method = MethodRequest.GET)
	public Status deleteResource(@Param("id") Long id) {
		 boolean deleted = false;
		 if (employeeService.delete(Employee.class,id)) {
		   messagingService.produce("employee.delete", "id:"+id);
		   deleted = true;
		 } 
		  Status status = new Status();
		  status.msg = deleted ? "DELETED" : "DELETE FAILED";
		  
		return status;
				
	}
	
}


class Status {
	
	public String msg;
}



