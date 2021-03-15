/**
 * EmployeeDAOImpl.java
 * Created on 2021-01-29 @ 06:29 pm
 * Author: Hector Vertus
 */
package com.revature.ers.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.ers.models.Employee;
import com.revature.ers.models.Reimbursement;
import com.revature.ers.models.Role;

public class EmployeeDAOImpl extends DAOUtilities implements EmployeeDAO {
	private static  Logger log = Logger.getLogger(EmployeeDAOImpl.class);

	/*
	 * method to persist employee into the database.
	 */
	public int save(Employee employee) {
		log.info("<>-------------- Inside saveEmployee(Employee employee)");	
		int saved = 0;

		try {
			
			Role role = new UserDAOImpl().findRoleByName("employee");

			//Save the user first	
			employee.getUser().setRole(role);
			//save user and employee
			
			if(employee.getEmployeeId()==0) {
				saved = super.save(employee.getUser(), employee); //this objects are in an acidic transaction, so that's we set them up to persist as a pair object. If one is faild, rollback.
			} else {
				super.update(employee.getUser(), true); //update the user
				super.update(employee, true); //update the employee		
				saved = employee.getEmployeeId();
			}
		} catch (Exception sqle) {
			log.warn("??????????? Error on saveEmployee:: ", sqle);
		} 
		
		log.info("<>-------------- Exit saveEmployee(Employee employee)");
		return saved;
	}
	
	//Method to find an employee by id
	public Employee findById(int employeeId) {
		log.info("<>-------------- Inside findById(int employeeId)");
		Employee employee = null;
		//user id is a unique constrainst, so this query will return just one employee.
		List<?> employees = super.selectAll("Employee.selectById", employeeId);

		if(!employees.isEmpty()) {
			employee = (Employee) employees.get(0); //just one role, so get the first one.
		}
					
		log.info("<>-------------- Exit findById(int employeeId)");
		return employee;
	}	
	
	//Method to find an employee by user id
	public Employee findByUserId(int userId) {
		log.info("<>-------------- Inside findByUserId(int userId)");
		Employee employee = null;
		//user id is a unique constrainst, so this query will return just one employee.
		List<?> employees = super.selectAll("Employee.selectByUserId", userId);

		if(!employees.isEmpty()) {
			employee = (Employee) employees.get(0); //just one role, so get the first one.
		}
					
		log.info("<>-------------- Exit findByUserId(int userId)");
		return employee;
	}

	/* Method to get all employees */
	public List<Employee> getAllEmployees() {
		log.info("<>-------------- Inside getAllEmployees()");
		List<Employee> employees = null;

		try {
			List<?> employeesList = super.selectAll("FROM Employee", Employee.class);
			
			if(!employeesList.isEmpty()) {
				employees = new ArrayList<Employee>();
				for(Object object: employeesList) {
					if(object instanceof Employee) {
						employees.add((Employee)object);
					}
				}
			}
		} catch (Exception e) {

		} 
		
		log.info("<>-------------- Exit getAllEmployees()");
		return employees;
	}

	/* Method to get all reimbursement for an employee */
	public List<Reimbursement> getAllReimbursementsForAnEmployee(Long emplID) {
		List<Reimbursement> reimbursements = null;
		try {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return reimbursements;
	}

	/*------------------------------------------------------------------------------------------------*/
}
