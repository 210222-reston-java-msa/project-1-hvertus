package com.revature.ers.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.ers.models.Reimbursement;
import com.revature.ers.models.Status;
import com.revature.ers.models.Type;
import com.revature.ers.utils.Utils;


/**
 * Implementation for the ReimbursementDAO, responsible for querying the database for Reimbursement objects.
 */
public class ReimbursementDAOImpl extends DAOUtilities implements ReimbursementDAO {
	private static  Logger log = Logger.getLogger(ReimbursementDAOImpl.class);
	
	/*
	 * method to persist reimbursement into the database.
	 */
	public int submitReimbursement(Reimbursement reimbursement) {
		log.info("<>-------------- Inside submitReimbursement(Reimbursement reimbursement)");	
		int submittedId = 0;

		try {
			if(reimbursement.getReimbursementId()==0) {
				submittedId = super.save(reimbursement); //this objects are in an acidic transaction, so that's we set them up to persist as a pair object. If one is faild, rollback.
			} else {				
				super.update(reimbursement, true); //update the reimbursement
				submittedId = reimbursement.getReimbursementId();
			}						
		} catch (Exception ex) {
			log.warn("??????????? Error on submitReimbursement:: ", ex);
		} 
		
		log.info("<>-------------- Exit submitReimbursement(Reimbursement reimbursement)");
		return submittedId;
	}	

	/*------------------------------------------------------------------------------------------------*/
	public List<Status> getStatuses() {
		log.info("<>-------------- Inside getStatuses()");
		
		List<Status> statuses = null;

		try {
			List<?> statusesList = super.selectAll("FROM Status", Status.class);
			
			if(!statusesList.isEmpty()) {
				statuses = new ArrayList<Status>();
				for(Object object: statusesList) {
					if(object instanceof Status) {
						statuses.add((Status)object);
					}
				}
			}
		} catch (Exception e) {

		} 
		
		log.info("<>-------------- Exit getStatuses()");
		return statuses;
	}
	
	public List<Type> getTypes() {
		log.info("<>-------------- Inside getTypes()");
		List<Type> types = null;

		try {
			List<?> typesList = super.selectAll("FROM Type", Type.class);
			
			if(!typesList.isEmpty()) {
				types = new ArrayList<Type>();
				for(Object object: typesList) {
					if(object instanceof Type) {
						types.add((Type)object);
					}
				}
			}
		} catch (Exception e) {

		} 
		
		log.info("<>-------------- Exit getTypes()");
		return types;				
	}
	
	// Method to get reimbursement information by id
	public Reimbursement findById(int reimbursementId) {
		log.info("<>-------------- Inside findById(int reimbursementId)");
		Reimbursement reimbursement = null;
		//user id is a unique constrainst, so this query will return just one reimbursement.
		List<?> reimbursements = super.selectAll("Reimbursement.selectById", reimbursementId);

		if(!reimbursements.isEmpty()) {
			reimbursement = (Reimbursement) reimbursements.get(0); //just one role, so get the first one.
		}
					
		log.info("<>-------------- Exit findById(int reimbursementId)");
		return reimbursement;
	}
	
	// Method to get reimbursement type by id
	public Type findTypeById(int typeId) {
		log.info("<>-------------- Inside findById(int typeId)");
		Type type = null;
		//user id is a unique constrainst, so this query will return just one type.
		List<?> types = super.selectAll("Type.selectById", typeId);

		if(!types.isEmpty()) {
			type = (Type) types.get(0); //just one role, so get the first one.
		}
					
		log.info("<>-------------- Exit findTypeById(int typeId)");
		return type;
	}	
	
	// Method to get reimbursement status by id
	public Status findStatusById(int statusId) {
		log.info("<>-------------- Inside findById(int statusId)");
		Status status = null;
		//user id is a unique constrainst, so this query will return just one status.
		List<?> statuses = super.selectAll("Status.selectById", statusId);

		if(!statuses.isEmpty()) {
			status = (Status) statuses.get(0); //just one role, so get the first one.
		}
					
		log.info("<>-------------- Exit findStatusById(int statusId)");
		return status;
	}
	
	// Method to get reimbursement status by status
	public Status findStatusByStatus(String stat) {
		log.info("<>-------------- Inside findStatusByStatus(String status)");
		Status status = null;
		//user id is a unique constrainst, so this query will return just one status.
		List<?> statuses = super.selectAll("FROM Status WHERE status='"+stat+"'", Status.class);

		if(!statuses.isEmpty()) {
			status = (Status) statuses.get(0); //just one role, so get the first one.
		}
					
		log.info("<>-------------- Exit findStatusById(int statusId)");
		return status;
	}		
	
	/*------------------------------------------------------------------------------------------------*/
	// This method is to get reimbursements of an author.
	public List<Reimbursement> getReimbursements(int author) {
		log.info("<>------------- Inside getReimbursements(int author)");
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();

		List<?> reimburses = super.selectAll("Reimbursement.selectByAuthor", author);

		if(Utils.isNotNull(reimburses)) {			
			reimburses.stream().forEach(object -> {
				if(object instanceof Reimbursement) {
					reimbursements.add((Reimbursement) object);
				}
			});
		}
		
		log.info("<>------------- Exit getReimbursements(int author)");
		return !reimbursements.isEmpty() ? reimbursements:null;
	}
	
	// This method is to deny a reimbursement.
	public List<Reimbursement> getPendingReimbursements() {
		log.info("<>------------- Inside getPendingReimbursements()");
		List<Reimbursement> pendingReimbursements = new ArrayList<Reimbursement>();
		
		List<?> preimbursements = super.selectAll("Reimbursement.selectPending");
		
		if(Utils.isNotNull(preimbursements)) {			
			preimbursements.stream().forEach(object -> {
				if(object instanceof Reimbursement) {					
					pendingReimbursements.add((Reimbursement) object);
				}
			});
		}
		
		log.info("<>------------- Exit getPendingReimbursements()");
		return !pendingReimbursements.isEmpty() ? pendingReimbursements:null;
	}
	
	// This method is to get all reimbursements.
	public List<Reimbursement> getAllReimbursements() {
		log.info("<>------------- Inside getAllReimbursements()");
		List<Reimbursement> allReimbursements = new ArrayList<Reimbursement>();
		
		List<?> reimbursements = super.selectAll("Reimbursement.selectAll"); 
		
		if(Utils.isNotNull(reimbursements)) {			
			reimbursements.stream().forEach(object -> {
				if(object instanceof Reimbursement) {
					allReimbursements.add((Reimbursement) object);
				}
			});
		}
		
		log.info("<>------------- Exit getAllReimbursements()");
		return !allReimbursements.isEmpty() ? allReimbursements:null;
	}	

	/*------------------------------------------------------------------------------------------------*/
	//check if table reimbursement_status is empty and reimbursement_type, if so create them 
	public void checkStatusAndTypes() {
		List<Status> statuses = getStatuses();

		//save the statuses
		if(Utils.isNull(statuses)) { //this will execute just once			
			Status status = new Status();
			status.setStatus("pending");
			super.save(status);
			status = new Status();
			status.setStatus("approved");
			super.save(status);
			status = new Status();
			status.setStatus("denied");
			super.save(status);
		}
		
		List<Type> types = getTypes();
		//save the types
		if(Utils.isNull(types)) { //this will execute just once	
			Type type = new Type();
			type.setType("lodging");
			super.save(type);
			type = new Type();
			type.setType("travel");
			super.save(type);
			type = new Type();
			type.setType("food");
			super.save(type);
			type = new Type();
			type.setType("other");
			super.save(type);			
		}	
	}	
}	