/**
 * 
 */
package com.revature.ers.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.revature.ers.models.Employee;
import com.revature.ers.models.User;
import com.revature.ers.services.EmployeeServiceImpl;
import com.revature.ers.services.UserServiceImpl;
import com.revature.ers.utils.Utils;

/**
 * @author hectorv
 *
 */
public class WebappServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static  Logger log = Logger.getLogger(WebappServlet.class);		
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("<>--------- Inside doGet(HttpServletRequest request, HttpServletResponse response)");
		add2ClientSession("ers_message", "", response); //clear the cookie
		
		String option = request.getParameter("option");

		if(Utils.isBlank(option)) {
			option = "profile";
		}
		
		switch(option) {
			case "home": 
				request.getRequestDispatcher("layouts/home.html").forward( request, response );
				break;
			
			case "reimbursement": 					
				request.getRequestDispatcher("layouts/reimbursement.html").forward( request, response );
				break;
				
			case "vreimbursements": 				
				request.getRequestDispatcher("layouts/view_reimbursements.html").forward( request, response );				
				break;				
	
			case "preimbursements": 
					request.getRequestDispatcher("layouts/pending_reimbursements.html").forward( request, response );
					break;											
			
			case "allreimbursements": 
					request.getRequestDispatcher("layouts/all_reimbursements.html").forward( request, response );
					break;
				
			case "addEmployee": 
				request.getRequestDispatcher("layouts/employee.html").forward( request, response );
				break;

			case "browseEmployee": 
					request.getRequestDispatcher("layouts/browse_employee.html").forward( request, response );
					break;	
										
			case "profile": 					
					//send data back via cookie instead of json or object mapper. Otherwise, you'll be facing big challenge especially when using hibernate
					Employee employee = populateEmployee(request);

					String employeeAsJS = employee.toJSObject(); //parse to javascript object
					
					if(Utils.isNotNull(employee.getPicture())) {
						String pictureFile = getServletContext().getRealPath("") 
											+ File.separator 
											+ "uploaded.files"
											+ File.separator; //actually it's the path to download the picture
						
						// creates the directory if it does not exist
						File downloadDir = new File(pictureFile);
						if (!downloadDir.exists()) {
							downloadDir.mkdir();
						}
						
						pictureFile = pictureFile + employee.getEmployeeId()+".jpg";
						
						Utils.writeToFile(employee.getPicture(), pictureFile);
						employeeAsJS = employeeAsJS + "^pictureFile:" + pictureFile;
					}

					add2ClientSession("employee_profile", employeeAsJS, response);
						
					request.getRequestDispatcher("layouts/employee.html").forward( request, response );					
					break;	
	
			case "logout": 
					add2ClientSession("connected_user", "", response);
					add2ClientSession("user_role", "", response);				
					request.getSession().invalidate();
					request.getRequestDispatcher("index.html").forward( request, response );
					break;	
					
			default:
				request.getRequestDispatcher("index.html").forward( request, response );
				break;					
		}
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("<>--------- Inside doPost(HttpServletRequest request, HttpServletResponse response)");
		//get the credentials
		User user = new UserServiceImpl()
						.signIn(new User(request.getParameter("userName"), 
										 Utils.encriptString(request.getParameter("password"))));
		
		if(Utils.isNotNull(user)) {
			add2ClientSession("connected_user", user.getUsername(), response);
			add2ClientSession("user_role", user.getRole().getName(), response);
			request.getSession().setAttribute("connectedUser", user);
			request.getRequestDispatcher("layouts/home.html").forward( request, response );
		} else {
			request.getRequestDispatcher("index.html").forward( request, response ); 
		}
	}	
	
	private Employee populateEmployee(HttpServletRequest request) {
		log.info("<>--------- Inside populateEmployee(HttpServletRequest request)");
		Employee employee = null;		
		String employeeId = request.getParameter("employeeId"); 

		if(Utils.isBlank(employeeId)) {
			//get connected user
			User user = (User) request.getSession().getAttribute("connectedUser");
			employee = new EmployeeServiceImpl().findByUserId(user.getUserId());
		} else {			
			employee = new EmployeeServiceImpl().findById(Integer.parseInt(employeeId));
		}

		return employee;
	}
	
	//Method to create cookie
	private void add2ClientSession(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		response.addCookie(cookie);		
	}	
}
