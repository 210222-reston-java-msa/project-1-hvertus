package com.revature.ers.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.revature.ers.dao.EmployeeDAOImpl;
import com.revature.ers.models.Employee;
import com.revature.ers.utils.Utils;

public class EmployeeServiceImpl extends EmployeeDAOImpl implements EmployeeService {
	private static  Logger log = Logger.getLogger(EmployeeServiceImpl.class);

	@Override
	public int save(Employee employee) {
		log.info("<>--------- Inside save(Employee employee)");
		if(employee.getEmployeeId() > 0) {
			Employee emp = (Employee) super.findById(employee.getEmployeeId());
			employee.setCreatedBy(emp.getCreatedBy());
			employee.setCreatedDate(emp.getCreatedDate());
			employee.setCreatedTime(emp.getCreatedTime());			
			
			//check the picture
			if(Utils.isNull(employee.getPicture()) &&
			   Utils.isNotNull(emp.getPicture())) {
				employee.setPicture(emp.getPicture());	
			}
		}
		
		//encrypt the user password
		employee.getUser().setPassword(Utils.encriptString(employee.getUser().getPassword()));
		log.info("<>--------- Exit save(Employee employee)");
		return super.save(employee);
	}

	// Method to get all employees
	public List<Employee> getEmployees() {		
		return super.getAllEmployees();
	}
	
	// Method to get all employees as json array
	public JSONArray getEmployeesAsJson() {	
		//parse employees' list into json array
		JSONArray employeesArray = new JSONArray();
		//loop through employees' list
		getEmployees().stream().forEach(employee -> {
			employeesArray.put(employee.toJson());				
		});
		
		return employeesArray;
	}	
	
	// Method to get Employee information by id
	public Employee findById(int employeeId) {
		return super.findById(employeeId);
	}
	
	// Method to get Employee information by user id
	public Employee findByUserId(int userId) {
		return super.findByUserId(userId);
	}	
	
	/*public List<Reimbursement> getAllReimbursementsForAnEmployee(Long emplID){
		return super.getAllReimbursementsForAnEmployee(emplID);
	}*/
	
}