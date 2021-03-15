package com.revature.ers.models;

/**
 * Employee.java
 * Created on 2021-03-01 @ 10:15 pm
 * Author: Hector Vertus
 */

import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.json.JSONObject;

import com.revature.ers.utils.Utils;

//All these annotations are from the javax.persistence API

@Entity // entity means a class that will be a table 
@Table(name="employee") // here we specify the table name
@NamedQuery(name="Employee.selectById", query="SELECT BEAN FROM Employee BEAN WHERE BEAN.employeeId = ?1")
@NamedQuery(name="Employee.selectByUserId", query="SELECT BEAN FROM Employee BEAN WHERE BEAN.user.userId = ?1")
public class Employee {
	
	@Id // this means that this column will be the Primary Key
	@Column(name="employee_id")
	@GeneratedValue(strategy=GenerationType.AUTO) // this is how we generate a SERIAL value	
	private int employeeId;
	
	@Column(name="first_name", nullable=false)
	private String firstName;
	
	@Column(name="last_name", nullable=false)
	private String lastName;
	
	private String gender;
	
	@Column(name="national_id", unique=true, nullable=false)
	private String nationalID;
	
	@Column(unique=true, nullable=false)
	private String email;
	
	@Column(unique=true, nullable=false)
	private String phone;
	
	@Column(nullable=false)
	private String address;
	
	private byte[] picture;
	
	@Column(name="created_date", nullable=false)
	private Date createdDate;
	
	@Column(name="date_stamp", nullable=false)
	private Date dateStamp;
	
	@Column(name="created_time", nullable=false)
	private Time createdTime;
	
	@Column(name="time_stamp", nullable=false)
	private Time timeStamp;
	
	@Column(name="created_by", nullable=false)
	private String createdBy;
	
	@Column(name="user_stamp", nullable=false)
	private String userStamp;
	
	//bi-directional one-to-one association to User
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")	
	private User user;
	
	// We will signal to hibernate that this is a OneToMany relationship....
	@OneToMany(mappedBy="author", fetch = FetchType.LAZY)
	private List<Reimbursement> reimbursementAuthors;
	
	// We will signal to hibernate that this is a OneToMany relationship....
	@OneToMany(mappedBy="resolver", fetch = FetchType.LAZY)
	private List<Reimbursement> reimbursementResolvers;

	/**
	 * The default constructor
	 */
	public Employee() {
		super();
		user = new User();
	}
	
	public Employee(boolean createID) {
		user = new User();
	}	
	
	/**
	 * @return the employeeId
	 */
	public int getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the nationalID
	 */
	public String getNationalID() {
		return nationalID;
	}

	/**
	 * @param nationalID the nationalID to set
	 */
	public void setNationalID(String nationalID) {
		this.nationalID = nationalID;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the picture
	 */
	public byte[] getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the dateStamp
	 */
	public Date getDateStamp() {
		return dateStamp;
	}

	/**
	 * @param dateStamp the dateStamp to set
	 */
	public void setDateStamp(Date dateStamp) {
		this.dateStamp = dateStamp;
	}

	/**
	 * @return the timeStamp
	 */
	public Time getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Time timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the userStamp
	 */
	public String getUserStamp() {
		return userStamp;
	}

	/**
	 * @param userStamp the userStamp to set
	 */
	public void setUserStamp(String userStamp) {
		this.userStamp = userStamp;
	}

	/**
	 * @return the createdTime
	 */
	public Time getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Time createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the reimbursementAuthors
	 */
	public List<Reimbursement> getReimbursementAuthors() {
		return reimbursementAuthors;
	}

	/**
	 * @param reimbursementAuthors the reimbursementAuthors to set
	 */
	public void setReimbursementAuthors(List<Reimbursement> reimbursementAuthors) {
		this.reimbursementAuthors = reimbursementAuthors;
	}

	/**
	 * @return the reimbursementResolvers
	 */
	public List<Reimbursement> getReimbursementResolvers() {
		return reimbursementResolvers;
	}

	/**
	 * @param reimbursementResolvers the reimbursementResolvers to set
	 */
	public void setReimbursementResolvers(List<Reimbursement> reimbursementResolvers) {
		this.reimbursementResolvers = reimbursementResolvers;
	}
	
	//populate the stamps but make sure all set first
	public void populateStamp(String connectedUser) {									
		if(this.getEmployeeId() == 0) {
			this.setCreatedDate(Utils.dateNow());
			this.setCreatedTime(Utils.currentTime());
			this.setCreatedBy(connectedUser);
		} 
		
		this.setDateStamp(Utils.dateNow());
		this.setTimeStamp(Utils.currentTime());
		this.setUserStamp(connectedUser);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + employeeId;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((nationalID == null) ? 0 : nationalID.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee other = (Employee) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (employeeId != other.employeeId) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (gender == null) {
			if (other.gender != null) {
				return false;
			}
		} else if (!gender.equals(other.gender)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if (nationalID == null) {
			if (other.nationalID != null) {
				return false;
			}
		} else if (!nationalID.equals(other.nationalID)) {
			return false;
		}
		if (phone == null) {
			if (other.phone != null) {
				return false;
			}
		} else if (!phone.equals(other.phone)) {
			return false;
		}
		return true;
	}	

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", gender=" + gender + ", nationalID=" + nationalID + ", email=" + email + ", phone=" + phone
				+ ", address=" + address + "]";
	}
	
	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		jObject.put("employeeId", employeeId);
		jObject.put("firstName", firstName);
		jObject.put("lastName", lastName);
		jObject.put("gender", gender);
		jObject.put("nationalID", nationalID);
		jObject.put("email", email);
		jObject.put("phone", phone);
		jObject.put("address", address);
		return jObject;
	}
	
	public String toJSObject() { //to javascript object
		return "employeeId:" + employeeId + "^firstName:" + firstName
				+"^lastName:" + lastName + "^gender:" + gender
				+"^nationalID:" + nationalID + "^address:" + address
				+"^email:" + email + "^phone:" + phone 
				+"^userId:" + user.getUserId()+"^userName:" + user.getUsername() 
				+"^password:" + user.getPassword();		
	}

}
