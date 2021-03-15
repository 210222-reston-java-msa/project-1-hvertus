package com.revature.ers.servlets;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Import required java libraries
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.revature.ers.models.Employee;
import com.revature.ers.models.User;
import com.revature.ers.services.EmployeeService;
import com.revature.ers.services.EmployeeServiceImpl;
import com.revature.ers.utils.Utils;

public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static  Logger log = Logger.getLogger(EmployeeServlet.class);	
	
	private EmployeeService employeeService;

	public EmployeeServlet() {
		super();
		employeeService = new EmployeeServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("<>--------- Inside doGet(HttpServletRequest request, HttpServletResponse response)");
		response.setContentType("application/json");
		
		//to display employees
		try {			
			JSONArray employees = employeeService.getEmployeesAsJson();
			response.getWriter().write(employees.toString());					
		} catch(Exception ex) {
			log.warn("???????????????? Error while retrieving employees:: "+ex);
		}
		
		log.info("<>--------- Exit doGet(HttpServletRequest request, HttpServletResponse response)");
	}
	
	/**
	 * Upon receiving file upload submission, parses the request to read
	 * upload data and saves the file on disk.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("<>--------- Inside doPost(HttpServletRequest request, HttpServletResponse response) --------<>");

		Map<String, Object> employeeMap = getDataModel(request);
		
		if(Utils.isNotNull(employeeMap)) {
			
			if(Utils.isSame(employeeMap.get("password").toString(), 
							employeeMap.get("confirmPassword").toString())) {

				Employee employee = new Employee();

				employee.setEmployeeId(Integer.parseInt((String) employeeMap.get("employeeId")));
				employee.setFirstName(employeeMap.get("firstName").toString());
				employee.setLastName(employeeMap.get("lastName").toString());
				employee.setGender(employeeMap.get("gender").toString());
				employee.setNationalID(employeeMap.get("nationalId").toString());
				employee.setAddress(employeeMap.get("address").toString());
				employee.setPhone(employeeMap.get("telephone").toString());
				employee.setEmail(employeeMap.get("email").toString());

				// set picture into employee
				if (Utils.isNotNull(employeeMap.get("picture"))) {
					byte[] picture = Utils.toByteArray((File) employeeMap.get("picture"));
					employee.setPicture(picture);
				}

				// get connected user
				User connectedUser = (User) request.getSession().getAttribute("connectedUser");

				// populate the stamps
				employee.populateStamp(connectedUser.getUsername());

				User user = new User();
				user.setUserId(Integer.parseInt(employeeMap.get("userId").toString()));
				user.setUsername(employeeMap.get("userName").toString());
				user.setPassword(employeeMap.get("password").toString());
				employee.setUser(user);

				int employeeId = employeeService.save(employee);

				if (employeeId > 0) {
					//log.info("<>--------- Employee is saved:: " + saved);
					// redirect to an html page
					populateEmployee(employeeId, request, response);
					add2ClientSession("ers_message", "success:Employee has been saved successfully.", response);
				}
			} else {
				add2ClientSession("ers_message", "fail:Password and confirm are mismatched.", response);
			}
		} else {			
			add2ClientSession("ers_message", "fail:Fail to save the employee.", response);
		}
		
		request.getRequestDispatcher("layouts/employee.html").forward(request, response);
		log.info("<>--------- Exit doPost(...) --------<>");
	}
	
	//Get the data from the UI. Since the form is multipart, do not use getParameter. Proceed the way in the method instead.
	private Map<String, Object> getDataModel(HttpServletRequest request) {
		log.info("<>--------- Inside getEmployeeDataModel(HttpServletRequest request) --------<>");
		Map<String, Object> employeeMap = null;
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			return employeeMap;
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
				employeeMap = new HashMap<String, Object>();
				
				// iterates over form's fields
				for (FileItem item : formItems) {
					// processes only fields that are not form fields
					if (item.isFormField()) {
                        employeeMap.put(item.getFieldName(), item.getString());                       
					} else if(Utils.isNotBlank(item.getName())) {
						File uploadedPicture = uploadPicture(item, uploadDir);							
						employeeMap.put("picture", uploadedPicture);						
					}
				}
			}
		} catch (Exception ex) {
			log.warn("???????????? Error while getting employee's info: " + ex.getMessage());
		}		
		
		log.info("<>--------- Exit getEmployeeDataModel(HttpServletRequest request) --------<>");
		return employeeMap;
	}
	
	//method to upload employee picture
	private File uploadPicture(FileItem item, File uploadDirectory) {
		log.info("<>--------- Inside uploadPicture(FileItem item)");
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
			log.warn("???????????? Error while uploading employee's picture: " + ex.getMessage());
		}
		
		return uploadFile;
	}
	
	//Method to populate employee after saving
	private void populateEmployee(int employee_id, HttpServletRequest request, HttpServletResponse response) {
		try {
			Employee employee = employeeService.findById(employee_id);

			String employeeAsJS = employee.toJSObject(); // parse to javascript object

			if (Utils.isNotNull(employee.getPicture())) {
				String pictureFile = getServletContext().getRealPath("") + File.separator + "uploaded.files"
						+ File.separator; // actually it's the path to download the picture

				// creates the directory if it does not exist
				File downloadDir = new File(pictureFile);
				if (!downloadDir.exists()) {
					downloadDir.mkdir();
				}

				pictureFile = pictureFile + employee.getEmployeeId() + ".jpg";

				Utils.writeToFile(employee.getPicture(), pictureFile);
				employeeAsJS = employeeAsJS + "^pictureFile:" + pictureFile;
			}

			add2ClientSession("employee_profile", employeeAsJS, response);
			//response.getWriter().write("employeeId:"+employee.getEmployeeId());			
		} catch (Exception ex) {
			log.warn("???????????? Error while populating employee." + ex.getMessage());
		}
	}
	
	//Method to create cookie
	private void add2ClientSession(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		response.addCookie(cookie);		
	}
	
}