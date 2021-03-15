/**
 * UserServiceImpl.java
 * Created on 2021-01-26 @ 04:30 pm
 * Author: Hector Vertus
 */
package com.revature.ers.services;

import com.revature.ers.dao.UserDAOImpl;
import com.revature.ers.models.User;

public class UserServiceImpl extends UserDAOImpl implements UserService {

	public UserServiceImpl() {
		super();
		super.checkRoles();
	}

	// method to authenticate a user
	public User signIn(User user) {
		return super.authenticateUser(user);
	}

}
