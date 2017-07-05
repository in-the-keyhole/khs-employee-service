# khs-employee-service 
------------------- 

Description
-----------
Provides bounded context API for Employees 

Installation
------------

1. Clone this repository
2. Import the project into an IDE and configure it as a Maven project
3. Start server by running the `com.keyolesoftware.employees.EmployeesApp` as a Java Application

Discovery Server Configuration
-------------------------------
By default, the service registers with a `localhost` Eureka-based registry

API
---
     /api/employees           | GET    | returns all Employees
     /api/employees/{id}      | GET    | returns a specific Employee by 'id'
     /api/employees           | POST   | add a new Employee 
     /api/employees/{id}      | PUT    | update an Employee
     /api/employees/{id}      | DELETE | deletes an Employee
