/**
 * User.java
 * Created on 2021-01-28 @ 06:30 pm
 * Author: Hector Vertus
 */
package com.revature.ers.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity // entity means a class that will be a table 
@Table(name="role") // here we specify the table name
public class Role {
	@Id // this means that this column will be the Primary Key
	@Column(name="role_id")
	@GeneratedValue(strategy=GenerationType.AUTO) // this is how we generate a SERIAL value	
	private int roleId;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	//bi-directional one-to-many association to User
	@OneToMany(mappedBy="role", fetch = FetchType.LAZY)
	private List<User> users;	
	
	/**
	 * default constructor
	 */
	public Role() {
		super();
	}	

	/**
	 * @return the roleId
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param user the users to set
	 */
	public void setUser(List<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + roleId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (roleId != other.roleId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", name=" + name + "]";
	}
	
}
