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
 * Type.java
 * Created on 2021-03-01 @ 10:15 pm
 * Author: Hector Vertus
 */

@Entity // entity means a class that will be a table 
@Table(name="reimbursement_type") // here we specify the table name
@NamedQuery(name="Type.selectById", query="SELECT BEAN FROM Type BEAN WHERE BEAN.typeId = ?1")
public class Type {

	@Id // this means that this column will be the Primary Key
	@Column(name="type_id")
	@GeneratedValue(strategy=GenerationType.AUTO) // this is how we generate a SERIAL value	
	private int typeId;
	
	@Column(unique=true, nullable=false)
	private String type;
	
	//bi-directional one-to-one association to Reimbursement
	// We will signal to hibernate that this is a OneToMany relationship....
	@OneToMany(mappedBy="type", fetch = FetchType.LAZY)
	private List<Reimbursement> reimbursements;	
	
	/**
	 * default constructor
	 */
	public Type() {
		super();
	}

	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + typeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Type)) {
			return false;
		}
		Type other = (Type) obj;
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (typeId != other.typeId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Type [typeId=" + typeId + ", type=" + type + "]";
	}

	public JSONObject toJson() {
		JSONObject jObject = new JSONObject();
		jObject.put("typeId", typeId);
		jObject.put("type", type);
		return jObject;
	}	
	
	public String toJSObject() { //to javascript object
		return "typeId:" + typeId +"^type:" + type ;		
	}
}
