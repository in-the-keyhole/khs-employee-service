package com.keyholesoftware.employees.model;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Jaime Niswonger
 */
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {
}
