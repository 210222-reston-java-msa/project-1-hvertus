/**
 * ImageServlet.java
 * 
 * Created on 2018-09-14 @ 09:17am 
 */
package com.revature.ers.servlets;
import java.io.File;
/**
 * @author hectorv
 *
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.revature.ers.utils.Utils;


//servlet class to display image
@SuppressWarnings("serial")
@WebServlet("/images")
public class ImageServlet extends HttpServlet  {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	 //@Override
	 @SuppressWarnings("resource")
	public void doGet(HttpServletRequest request, HttpServletResponse  response) throws ServletException, IOException {
		 log.info("<>----------- Entering insite method :: doGet(...)");

		 String imagefile= request.getParameter("image");

		 //log.info("<>----------- Image file to display :: "+imagefile);
		 
		 try {
			 
			 if(!Utils.isBlank(imagefile)) {
				 File file = new File(imagefile);
				 if(file.exists()) {
					 //check if file image file name exist				 
					 //log.info("<>----------- fileExist ---------- Image path to print :: "+imagefile);

					response.setContentType("image/jpg");			
				 
					//log.info("<>----------- Image to print :: "+imagefile);

					OutputStream os = response.getOutputStream();
					byte b[] = new byte[1024];
		                
					InputStream is = new FileInputStream(imagefile);
					//log.info("<>----------- Image stream to display :: "+is);
		        
					int numRead=0;
				
					while ((numRead=is.read(b)) > 0) {	    		
						os.write(b, 0, numRead);
					}
		    
					os.flush();	
					//log.info("<>--------------- The image should display properly.");
				  }
			 }
	 	} catch(Exception ex){
	 		//log.error("??????????? Unable to display the image : doGet(HttpServletRequest request, HttpServletResponse  response)", ex);						
	 	}	
		 
		 //log.info("<>----------- Exit from ImageServlet.doGet(...) "); 
	 }
	

}
