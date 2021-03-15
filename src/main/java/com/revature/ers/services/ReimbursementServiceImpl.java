package com.revature.ers.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.revature.ers.dao.ReimbursementDAOImpl;
import com.revature.ers.models.Employee;
import com.revature.ers.models.Reimbursement;
import com.revature.ers.models.Status;
import com.revature.ers.models.Type;
import com.revature.ers.models.User;
import com.revature.ers.utils.Utils;

public class ReimbursementServiceImpl extends ReimbursementDAOImpl implements ReimbursementService {
	private static  Logger log = Logger.getLogger(ReimbursementServiceImpl.class);
	
	public ReimbursementServiceImpl() {
		super();
		super.checkStatusAndTypes();
	}
		
	public int submit(Reimbursement reimbursement, User connectedUser) {
		log.info("<>--------- Inside submit(Reimbursement reimbursement)");
		EmployeeService employeeService = new EmployeeServiceImpl();
		Employee employee = employeeService.findByUserId(connectedUser.getUserId());
		
		if(reimbursement.getReimbursementId()==0) {
			reimbursement.setResolved(Utils.currentTime());	
			
			//set pending status into reimbursement
			reimbursement.setStatus(this.findStatusByStatus("pending"));
			
			//set author into reimbursement
			reimbursement.setAuthor(employee);			
		} else {
			reimbursement.setResolver(employee);
			reimbursement.setResolved(Utils.currentTime());	
			
			Reimbursement reimburse = findById(reimbursement.getReimbursementId());
			
			//check the receipt
			if(Utils.isNull(reimbursement.getReceipt()) &&
			   Utils.isNotNull(reimburse.getReceipt())) {
				reimbursement.setReceipt(reimburse.getReceipt());	
			}	
			
			//set author into reimbursement
			reimbursement.setAuthor(reimburse.getAuthor());			
		}
		
		//set the current into submitted. Valid for update as well
		reimbursement.setSubmitted(Utils.currentTime());
										
		log.info("<>--------- Exit submit(Reimbursement reimbursement)");
		return super.submitReimbursement(reimbursement);
	}	

	public List<Status> getStatuses() {
		return super.getStatuses();
	}

	// Method to get all statuses as json array
	public JSONArray getStatusesAsJson() {	
		//parse statuses' list into json array
		JSONArray statusesArray = new JSONArray();
		//loop through statuses' list
		getStatuses().stream().forEach(status -> {
			statusesArray.put(status.toJson());				
		});
		
		return statusesArray;
	}
	
	// Method to get all statuses as javascript
	public String getStatusesAsJS() {	
		StringBuffer buff = new StringBuffer();
		List<Status> statuses = getStatuses();
		int iteration = 1;
		for(Status status: statuses) {
			buff.append(status.toJSObject() + (iteration < statuses.size() ? ",":""));
			iteration++;
		}
		
		return buff.toString();
	}	
	
	public List<Type> getTypes() {
		return super.getTypes();
	}
	
	// Method to get all types as json array
	public JSONArray getTypesAsJson() {	
		//parse types' list into json array
		JSONArray typesArray = new JSONArray();
		//loop through types' list
		getTypes().stream().forEach(type -> {
			typesArray.put(type.toJson());				
		});
		
		return typesArray;
	}
	
	// Method to get all types as javascript
	public String getTypesAsJS() {	
		StringBuffer buff = new StringBuffer();
		List<Type> types = getTypes();
		int iteration = 1;
		for(Type type: types) {
			buff.append(type.toJSObject() + (iteration < types.size() ? ",":""));
			iteration++;
		}
		
		return buff.toString();
	}	
	
	// Method to get reimbursement information by id
	public Reimbursement findById(int reimbursementId) {
		return super.findById(reimbursementId);
	}
	
	// Method to get reimbursement type by id
	public Type findTypeById(int typeId) {
		return super.findTypeById(typeId);
	}	
	
	// Method to get reimbursement status by id
	public Status findStatusById(int statusId) {
		return super.findStatusById(statusId);
	}
	
	// Method to get reimbursement status by status
	public Status findStatusByStatus(String status) {
		return super.findStatusByStatus(status);
	}	
	
	// Method to get all reimbursements as json array
	public JSONArray getReimbursementsAsJson(User connectedUser) {	
		//parse reimbursements' list into json array
		JSONArray reimbursementsArray = new JSONArray();

		EmployeeService employeeService = new EmployeeServiceImpl();
		Employee author = employeeService.findByUserId(connectedUser.getUserId());
		
		List<Reimbursement> reimbursements = super.getReimbursements(author.getEmployeeId());
			
		//loop through reimbursements' list
		reimbursements.stream().forEach(reimbursement -> {
			reimbursementsArray.put(reimbursement.toJson());				
		});
		
		return reimbursementsArray;
	}
	
	// Method to get all pending reimbursements as json array
	public JSONArray getPendingReimbursementsAsJson() {	
		//parse reimbursements' list into json array
		JSONArray reimbursementsArray = new JSONArray();

		List<Reimbursement> pendingReimbursements = super.getPendingReimbursements();
		//loop through pendingReimbursements' list
		pendingReimbursements.stream().forEach(preimbursement -> {
			reimbursementsArray.put(preimbursement.toJson());				
		});
		
		return reimbursementsArray;
	}	
	
	// function to get all reimbursements except pending
	public JSONArray getAllReimbursementsAsJson() {	
		//parse reimbursements' list into json array
		JSONArray reimbursementsArray = new JSONArray();

		List<Reimbursement> allReimbursements = super.getAllReimbursements();
		
		//loop through allReimbursements' list
		allReimbursements.stream().forEach(allreimbursement -> {
			reimbursementsArray.put(allreimbursement.toJson());				
		});
		
		return reimbursementsArray;
	}

}
