/**
 * UserDAOImpl.java
 * Created on 2021-01-28 @ 07:48 pm
 * Author: Hector Vertus
 */

package com.revature.ers.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.revature.ers.models.Role;
import com.revature.ers.models.User;

/**
 * @author hectorv
 *
 */
public class UserDAOImpl extends DAOUtilities implements UserDAO {
	private static  Logger log = Logger.getLogger(UserDAOImpl.class);			

	public User authenticateUser(User user) {
		log.info("<>-------------- Inside authenticateUser(User user)");
		User usr = null; 
		try {
			List<?> users = super.selectAll("FROM User WHERE username='"
											+user.getUsername()
											+"' AND password='"+user.getPassword()
											+"'", User.class); //will return just one user

			if(!users.isEmpty()) {
				usr = (User)users.get(0);
			}
		} catch (Exception e) {

		} 	
		
		log.info("<>-------------- Exit authenticateUser(User user)");
		return usr;
	}
	
	//Method to get role by name
	public Role findRoleByName(String roleName) {
		log.info("<>-------------- Inside findRoleByName(String roleName)");
		Role role = null;
		
		//Role name is a unique constrainst, so this query will return just one role if found.
		List<?> roles = super.selectAll("FROM Role WHERE name='"+roleName+"'", Role.class);
		if(!roles.isEmpty()) {
			role = (Role) roles.get(0); //just one role, so get the first one.
		}
			
		log.info("<>-------------- Exit findRoleByName(String roleName)");
		return role;
	}
	
	//check if table roles is empty, if so create them 
	public void checkRoles() {
		Role role = null;
		List<?> roles = super.selectAll("From Role", Role.class);

		if(roles.isEmpty()) { //this will execute just once
			role = new Role();
			role.setName("employee");
			super.save(role);
			role = new Role();
			role.setName("manager");
			super.save(role);	
			
			role = findRoleByName("manager");
		}	
	}
	
	/*------------------------------------------------------------------------------------------------*/

}
