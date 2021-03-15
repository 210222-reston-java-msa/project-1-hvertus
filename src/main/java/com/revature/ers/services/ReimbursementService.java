package com.revature.ers.services;

import java.util.List;

import org.json.JSONArray;

import com.revature.ers.models.Reimbursement;
import com.revature.ers.models.Status;
import com.revature.ers.models.Type;
import com.revature.ers.models.User;

public interface ReimbursementService {
	public int submit(Reimbursement reimbursement, User connectedUser);
	public List<Status> getStatuses();
	public List<Type> getTypes();
	public JSONArray getTypesAsJson();
	public JSONArray getStatusesAsJson();
	public String getTypesAsJS();
	public String getStatusesAsJS();
	public Reimbursement findById(int reimbursementId);
	public Type findTypeById(int typeId);
	public Status findStatusById(int statusId);
	
	public JSONArray getReimbursementsAsJson(User connectedUser);
	public JSONArray getPendingReimbursementsAsJson();
	public JSONArray getAllReimbursementsAsJson();
}
