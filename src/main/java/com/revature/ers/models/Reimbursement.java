package com.revature.ers.models;

/**
 * Reimbursement.java
 * Created on 2021-03-01 @ 10:15 pm
 * Author: Hector Vertus
 */

import java.sql.Time;
import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity // entity means a class that will be a table 
@Table(name="reimbursement") // here we specify the table name
@NamedQuery(name="Reimbursement.selectById", query="SELECT BEAN FROM Reimbursement BEAN WHERE BEAN.reimbursementId = ?1")
@NamedQuery(name="Reimbursement.selectByAuthor", query="SELECT BEAN FROM Reimbursement BEAN WHERE BEAN.author.employeeId = ?1")
@NamedQuery(name="Reimbursement.selectPending", query="SELECT BEAN FROM Reimbursement BEAN WHERE BEAN.status.status='pending'")
@NamedQuery(name="Reimbursement.selectAll", query="SELECT BEAN FROM Reimbursement BEAN WHERE BEAN.status.status<>'pending'") 
public class Reimbursement {

	@Id // this means that this column will be the Primary Key
	@Column(name="reimbursement_id")
	@GeneratedValue(strategy=GenerationType.AUTO) // this is how we generate a SERIAL value		
	private int reimbursementId;
	
	private Double amount;	
	private Time submitted;
	private Time resolved;
	private String description;
	private byte[] receipt;

	@ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "author")
	private Employee author;
	
	@ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "resolver")	
	private Employee resolver;
	
	//bi-directional one-to-one association to reimbursement Status
	@ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "status")		
	private Status status;
	
	//bi-directional one-to-one association to reimbursement Type
	@ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")		
	private Type type;

	/**
	 * default constructor
	 */
	public Reimbursement() {
		super();
	}

	/**
	 * @return the reimbursementId
	 */
	public int getReimbursementId() {
		return reimbursementId;
	}

	/**
	 * @param reimbursementId the reimbursementId to set
	 */
	public void setReimbursementId(int reimbursementId) {
		this.reimbursementId = reimbursementId;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the submitted
	 */
	public Time getSubmitted() {
		return submitted;
	}

	/**
	 * @param submitted the submitted to set
	 */
	public void setSubmitted(Time submitted) {
		this.submitted = submitted;
	}

	/**
	 * @return the resolved
	 */
	public Time getResolved() {
		return resolved;
	}

	/**
	 * @param resolved the resolved to set
	 */
	public void setResolved(Time resolved) {
		this.resolved = resolved;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the receipt
	 */
	public byte[] getReceipt() {
		return receipt;
	}

	/**
	 * @param receipt the receipt to set
	 */
	public void setReceipt(byte[] receipt) {
		this.receipt = receipt;
	}

	/**
	 * @return the author
	 */
	public Employee getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Employee author) {
		this.author = author;
	}

	/**
	 * @return the resolver
	 */
	public Employee getResolver() {
		return resolver;
	}

	/**
	 * @param resolver the resolver to set
	 */
	public void setResolver(Employee resolver) {
		this.resolver = resolver;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(receipt);
		result = prime * result + reimbursementId;
		result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Reimbursement)) {
			return false;
		}
		Reimbursement other = (Reimbursement) obj;
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!author.equals(other.author)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (!Arrays.equals(receipt, other.receipt)) {
			return false;
		}
		if (reimbursementId != other.reimbursementId) {
			return false;
		}
		if (resolved == null) {
			if (other.resolved != null) {
				return false;
			}
		} else if (!resolved.equals(other.resolved)) {
			return false;
		}
		if (resolver == null) {
			if (other.resolver != null) {
				return false;
			}
		} else if (!resolver.equals(other.resolver)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (submitted == null) {
			if (other.submitted != null) {
				return false;
			}
		} else if (!submitted.equals(other.submitted)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Reimbursement [reimbursementId=" + reimbursementId + ", amount=" + amount + ", submitted=" + submitted
				+ ", resolved=" + resolved + ", description=" + description + ", status=" + status + ", type=" + type
				+ "]";
	}
	
	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		jObject.put("reimbursementId", reimbursementId);
		jObject.put("description", description);
		jObject.put("amount", amount);
		jObject.put("submitted", submitted);
		jObject.put("resolved", resolved);
		jObject.put("typeId", type.getTypeId());
		jObject.put("type", type.getType());
		jObject.put("statusId", status.getStatusId());
		jObject.put("status", status.getStatus());
		return jObject;
	}
	
	public String toJSObject() { //to javascript object
		return "reimbursementId:" + reimbursementId + "^description:" + description
				+"^amount:" + amount + "^submitted:" + submitted
				+"^resolved:" + resolved + "^typeId:" + type.getTypeId() 
				+"^type:" + type.getType() + "^statusId:" + status.getStatusId()
				+"^status:" + status.getStatus();		
	}
}