package com.revature.ers.dao;

import java.util.List;

import com.revature.ers.models.Reimbursement;
import com.revature.ers.models.Status;
import com.revature.ers.models.Type;

/**
* Interface for our Data Access Object to handle database queries related to Reimbursements.
*/
public interface ReimbursementDAO {
	public int submitReimbursement(Reimbursement reimbursement);
	public List<Status> getStatuses();
	public List<Type> getTypes();
	public void checkStatusAndTypes();
	public List<Reimbursement> getReimbursements(int author);

}
