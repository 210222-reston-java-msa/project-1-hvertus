package com.revature.ers.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONObject;

/**
 * Status.java
 * Created on 2021-03-01 @ 10:15 pm
 * Author: Hector Vertus
 */

@Entity // entity means a class that will be a table 
@Table(name="reimbursement_status") // here we specify the table name
@NamedQuery(name="Status.selectById", query="SELECT BEAN FROM Status BEAN WHERE BEAN.statusId = ?1")
public class Status {

	@Id // this means that this column will be the Primary Key
	@Column(name="status_id")
	@GeneratedValue(strategy=GenerationType.AUTO) // this is how we generate a SERIAL value	
	private int statusId;
	
	@Column(unique=true, nullable=false)
	private String status;

	// We will signal to hibernate that this is a OneToMany relationship....
	@OneToMany(mappedBy="status", fetch = FetchType.LAZY)
	private List<Reimbursement> reimbursements;
	
	
	/**
	 * default constructor
	 */
	public Status() {
		super();
	}

	/**
	 * @return the statusId
	 */
	public int getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the reimbursements
	 */
	public List<Reimbursement> getReimbursements() {
		return reimbursements;
	}

	/**
	 * @param reimbursements the reimbursements to set
	 */
	public void setReimbursements(List<Reimbursement> reimbursements) {
		this.reimbursements = reimbursements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + statusId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Status)) {
			return false;
		}
		Status other = (Status) obj;
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (statusId != other.statusId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Status [statusId=" + statusId + ", status=" + status + "]";
	}

	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		jObject.put("statusId", statusId);
		jObject.put("status", status);
		return jObject;
	}	
	
	public String toJSObject() { //to javascript object
		return "statusId:" + statusId +"^status:" + status ;		
	}	
}
