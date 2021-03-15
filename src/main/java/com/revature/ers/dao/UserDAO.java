/**
 * UserDAO.java
 * Created on 2021-01-28 @ 07:44 pm
 * Author: Hector Vertus
 */
package com.revature.ers.dao;

import com.revature.ers.models.User;

public interface UserDAO {
	public User authenticateUser(User user);
}
