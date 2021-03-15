package com.revature.ers.servlets;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.revature.ers.models.Reimbursement;
import com.revature.ers.models.Status;
import com.revature.ers.models.Type;
import com.revature.ers.models.User;
import com.revature.ers.services.ReimbursementService;
import com.revature.ers.services.ReimbursementServiceImpl;
import com.revature.ers.utils.Utils;


/*
 * This servlet will take you to the homepage for the Reimbursement module
 */
public class ReimbursementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static  Logger log = Logger.getLogger(ReimbursementServlet.class);
	
	private ReimbursementService reimbursementService;
	
	public ReimbursementServlet() {
		super();
		reimbursementService = new ReimbursementServiceImpl();
	}	

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("<>--------- Inside doGet(HttpServletRequest request, HttpServletResponse response)");
		//response.setContentType("application/json");
		
		reimbursementService = new ReimbursementServiceImpl(); 

		try {	
			
			String option = request.getParameter("option");

			// get connected user
			User connectedUser = (User) request.getSession().getAttribute("connectedUser");
			//response.setContentType("application/json");
			
			if(Utils.isSame(option, "view")) {
				response.setContentType("application/json");
				JSONArray reimbursements = reimbursementService.getReimbursementsAsJson(connectedUser);				
				response.getWriter().write(reimbursements.toString());				
			} else if(Utils.isSame(option, "preimbursement")) {
				response.setContentType("application/json");
				JSONArray pendingReimbursements = reimbursementService.getPendingReimbursementsAsJson();
				response.getWriter().write(pendingReimbursements.toString());				
			} else if(Utils.isSame(option, "allreimbursements")) {
				response.setContentType("application/json");
				JSONArray allReimbursements = reimbursementService.getAllReimbursementsAsJson();
				response.getWriter().write(allReimbursements.toString());				
			} else if(Utils.isSame(option, "display")) {
				int reimbursementId = Integer.parseInt(request.getParameter("reimbursementId"));				
				// redirect to an html page
				populateReimbursement(reimbursementId, request, response);	
				request.getRequestDispatcher("layouts/reimbursement.html").forward(request, response);
			}
			
			//will alway dispay statuses and types
			String statusesAndTypes = reimbursementService.getStatusesAsJS()+"|"
									 +reimbursementService.getTypesAsJS();
			
			add2ClientSession("statuses_and_types", statusesAndTypes, response);			
		} catch(Exception ex) {
			log.warn("???????????????? Error while retrieving statuses and types:: "+ex);
		}
		
		log.info("<>--------- Exit doGet(HttpServletRequest request, HttpServletResponse response)");
	}	

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("<>--------- Inside doPost(HttpServletRequest request, HttpServletResponse response) --------<>");

		Map<String, Object> reimbursementMap = getDataModel(request);
		
		if(Utils.isNotNull(reimbursementMap)) {
			Reimbursement reimbursement = new Reimbursement();

			reimbursement.setReimbursementId(Integer.parseInt((String) reimbursementMap.get("reimbursementId")));
			reimbursement.setDescription(reimbursementMap.get("description").toString());
			reimbursement.setAmount(Double.parseDouble(reimbursementMap.get("amount").toString()));
			
			if(reimbursement.getAmount() <=0 ) {
				request.getRequestDispatcher("layouts/reimbursement.html").forward(request, response);
			}
			
			int typeId = Integer.parseInt((String) reimbursementMap.get("type"));
			Type type = reimbursementService.findTypeById(typeId);
			reimbursement.setType(type);
			
			int statusId = Integer.parseInt((String) reimbursementMap.get("status"));
			Status status = reimbursementService.findStatusById(statusId);
			reimbursement.setStatus(status);

			// set receipt into reimbursement
			if (Utils.isNotNull(reimbursementMap.get("receipt"))) {
				byte[] receipt = Utils.toByteArray((File) reimbursementMap.get("receipt"));
				reimbursement.setReceipt(receipt);
			}

			// get connected user
			User connectedUser = (User) request.getSession().getAttribute("connectedUser");
						
			int reimbursementId = reimbursementService.submit(reimbursement, connectedUser);
			
			if (reimbursementId > 0) {
				//log.info("<>--------- Reimbursement is submitted:: " + submitted);
				// redirect to an html page
				populateReimbursement(reimbursementId, request, response);
				add2ClientSession("ers_message", "success:Reimbursemente has been submitted successfully.", response);
			}

		} else {	
			add2ClientSession("ers_message", "fail:Fail to submit the reimbursement.", response);
		}
		
		request.getRequestDispatcher("layouts/reimbursement.html").forward(request, response);
		log.info("<>--------- Exit doPost(...) --------<>");
	}
	
	//Get the data from the UI. Since the form is multipart, do not use getParameter. Proceed the way in the method instead.
	private Map<String, Object> getDataModel(HttpServletRequest request) {
		log.info("<>--------- Inside getReimbursementDataModel(HttpServletRequest request) --------<>");
		Map<String, Object> reimbursementMap = null;
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			return reimbursementMap;
		}
		
		// Constructs the directory path to store upload file. This path is relative to
		// application's directory
		String uploadPath = getServletContext().getRealPath("") + File.separator + "uploaded.files";
		// log.info("<>--------- Upload path: "+uploadPath);

		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}		
				
		try {
			// parses the request's content to extract file data
			List<FileItem> formItems = Utils.fileUploadConfig().parseRequest(request);
			
			if (formItems != null && formItems.size() > 0) {
				reimbursementMap = new HashMap<String, Object>();
				
				// iterates over form's fields
				for (FileItem item : formItems) {
					// processes only fields that are not form fields
					if (item.isFormField()) {
                        reimbursementMap.put(item.getFieldName(), item.getString());                       
					} else if(Utils.isNotBlank(item.getName())) {
						File uploadedReceipt = uploadReceipt(item, uploadDir);							
						reimbursementMap.put("receipt", uploadedReceipt);						
					}
				}
			}
		} catch (Exception ex) {
			log.warn("???????????? Error while getting reimbursement's info: " + ex.getMessage());
		}		
		
		log.info("<>--------- Exit getReimbursementDataModel(HttpServletRequest request) --------<>");
		return reimbursementMap;
	}	
	
	//method to upload reimbursement picture
	private File uploadReceipt(FileItem item, File uploadDirectory) {
		log.info("<>--------- Inside uploadReceipt(FileItem item)");
		File uploadFile = null;
		try {
			if (!item.isFormField()) {
				String fileName = new File(item.getName()).getName();
				String filePath = uploadDirectory + File.separator + fileName;
				uploadFile = new File(filePath);
				//log.info("<>--------- Store file: "+uploadFile);

				// saves the file on disk
				item.write(uploadFile);
			}
		} catch (Exception ex) {
			log.warn("???????????? Error while uploading reimbursement's receipt: " + ex.getMessage());
		}
		
		return uploadFile;
	}
	
	//Method to populate reimbursement after saving
	private void populateReimbursement(int reimbursementId, HttpServletRequest request, HttpServletResponse response) {
		try {
			Reimbursement reimbursement = reimbursementService.findById(reimbursementId);

			String reimbursementAsJS = reimbursement.toJSObject(); // parse to javascript object

			if (Utils.isNotNull(reimbursement.getReceipt())) {
				String receiptFile = getServletContext().getRealPath("") + File.separator + "uploaded.files"
						+ File.separator; // actually it's the path to download the receipt

				// creates the directory if it does not exist
				File downloadDir = new File(receiptFile);
				if (!downloadDir.exists()) {
					downloadDir.mkdir();
				}

				receiptFile = receiptFile + reimbursement.getReimbursementId() + ".jpg";

				Utils.writeToFile(reimbursement.getReceipt(), receiptFile);
				reimbursementAsJS = reimbursementAsJS + "^receiptFile:" + receiptFile;
			}

			add2ClientSession("reimbursement_request", reimbursementAsJS, response);						
		} catch (Exception ex) {
			log.warn("???????????? Error while populating reimbursement." + ex.getMessage());
		}
	}
	
	//Method to create cookie
	private void add2ClientSession(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		response.addCookie(cookie);		
	}	
 }

