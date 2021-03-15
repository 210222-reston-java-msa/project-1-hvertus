package com.revature.ers.services;

import java.util.List;

import org.json.JSONArray;

import com.revature.ers.models.Employee;

public interface EmployeeService {
	public int save(Employee employee);
	public List<Employee> getEmployees();
	public JSONArray getEmployeesAsJson();
	public Employee findById(int employeeId);
	public Employee findByUserId(int userId);
	//public Employee getEmployeeInfo(long userID);
	//public List<Reimbursement> getAllReimbursementsForAnEmployee(Long emplID);
}
