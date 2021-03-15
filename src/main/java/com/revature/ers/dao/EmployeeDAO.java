/**
 * EmployeeDAO.java
 * Created on 2021-01-29 @ 06:28 pm
 * Author: Hector Vertus
 */
package com.revature.ers.dao;

import java.util.List;

import com.revature.ers.models.Employee;

public interface EmployeeDAO {
	public int save(Employee employee);	
	public List<Employee> getAllEmployees();
	public Employee findById(int employeeId);
	public Employee findByUserId(int userId);

}