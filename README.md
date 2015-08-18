# khs-employee-service
Employe Micro Service implementation

Description
-----------
Provides CRUD API for Employee 

Installation
------------
1. Clone Repo  (Project is dependent on khs-service-base project)
2. Import into STS Eclipse and configure as a Maven Project 
3. Run `khs.project.boot.Application` as a Java Application 

Service Registry Configuration
-------------------------------
By default, service registers with Keyhole Cloud registry, to register with a local registry add this VM argument when you run the application 

     -Deureka.environment=DEV
  
API
---
     api/employee/all  | GET  |  All Employees
     api/employee/find/{id} | GET | Find Employee by ID 
     api/employee/save   | POST | Add an Employee 
     api/employee/save   | PUT  | Update a Employee 
     api/employee/save   | DELETE | Remove a Employee
