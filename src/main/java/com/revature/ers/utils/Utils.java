/**
 * Utils.java
 */
package com.revature.ers.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author hectorv
 *
 */
public class Utils {
	public static final String TEMP_PATH = "/opt/web/wildfly-20.0.0.Final/standalone/data/webapp.temp.files/";

    //check if an object is null, return true if the given object is null false otherwise
    public static boolean isNull(Object obj) {
        if (obj == null) return true;
        else return false;
    }

    //check if an object is not null, return true if the given object is null false otherwise
    public static boolean isNotNull(Object obj) {
        if (obj != null) return true;
        else return false;
    }
    
    /**
     * Returns true if null or trims to an empty string.
     *
     * @param s The string
     * @return Whether the given string is blank.
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().length()==0 || "null".equals(s.trim());
    }

    /**
     * Returns true if given string is not null or all blanks..
     *
     * @param s The string
     * @return Whether the given string is not blank.
     */
    public static boolean isNotBlank(String s) {
        return s != null && s.trim().length() > 0;
    } 

    /**
     * Returns true if both strings are same
     */
    public static boolean isSame(String aString, String other) {
        return isBlank(aString) && isBlank(other)
                || !isBlank(aString) && !isBlank(other) && aString.equals(other);
    } 

    /**
     * Encode a string under MD5 format
     *
     * @string : string to encode
     * @return encoded string
     * @throws NoSuchAlgorithmException
     */
    public static String encriptString(String string) { 
        
      StringBuffer hexString = new StringBuffer();
      
      try {  
          if (isNotBlank(string)) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(string.getBytes());
    
                byte[] hash = md.digest();
    
                for (int i = 0; i < hash.length; i++) {
                    if((0xff & hash[i]) < 0x10)
                        hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                    else
                        hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
          }
          else System.out.println(">----------- Unable to encript a blank string ....."); //log.info(">----------- Unable to encript a blink string .....");
        
      } catch (NoSuchAlgorithmException nosae) {
    	  System.out.println(">-------------------- Encript String Error "+nosae);
      }
      
      return (!string.equals(null) || string.length()>0) ? hexString.toString() : "";    
    }  
    
    //Method to check whether @email is valid or not
    public static boolean isEmailValid(String email) {
    	return email.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}");
    }
        
    //Method to check if a string is under format like 325 630 2005 or 202-555-0125
    public static boolean isPhoneValid(String string) {
    	String regex = "^(\\(\\d{3}\\)|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"; 
    	return Pattern.compile(regex).matcher(string).matches();  	    	
    }    
    
    /*indexed a string and get the first index
     *@string : the string to be indexed
     *@separator : char witch separe the strings
     *@example : firstIndexOfString("Jhon, Doe",',') will return Jhon
     */    
    public static String firstIndexOfString(String string,char separator) {
    	if(isBlank(string)) {
    		return null;
    	}
    	
    	String indexedString = null;
    	if(isNotBlank(string)) {
	        int index = string.indexOf(separator);
	        if (index == -1)
	        	indexedString = string;
	        else
	        	indexedString = string.substring(0, index);
    	}
    	return indexedString;
    }

    /*indexed a string and get the first index
     *@string : the string to be indexed
     *@separator : char witch separe the strings
     *@example : lastIndexOfString("Jhon, Doe",',') will return Doe
     */
    public static String lastIndexOfString(String string,char separator) {
    	if(isBlank(string)) {
    		return null;
    	}
    	
    	return string.substring(string.lastIndexOf(separator)+1).trim();
    }  
    
    //Method to capitalize a string
    //@allWords: false for the first letter only, true for all words separate by @separator    
    public static String capitalizeString(String string, boolean allWords, String separator) {
    	String capitalize = null;
    	
    	if(isBlank(string)) {
    		return capitalize; 
    	}
    	
    	if(!allWords) {
    		capitalize = string.substring(0, 1).toUpperCase() + string.substring(1);
    	} else { //so now we need to split the string, then capitalize each word.
    		String[] words = string.split(separator);
    		StringBuffer buff = new StringBuffer();
    		    		
    		for(int i=0; i < words.length; i++) {
    			capitalize = words[i].trim().substring(0, 1).toUpperCase() + words[i].trim().substring(1);    			
    			buff.append(i!=words.length-1 ? capitalize.concat(separator):"");  
    		}
    		
    		capitalize = words[words.length-1].trim().substring(0, 1).toUpperCase() + words[words.length-1].trim().substring(1);
    		capitalize = buff.toString().concat(capitalize.trim());
    	}
    	
    	return capitalize;
    }
    
    //method to get current date
    public static Date dateNow() {
        return new Date();
    }    
    
    //jdbc time type
    public static Time currentTime() {
        Calendar cal = Calendar.getInstance();
        return new Time(cal.getTime().getTime());
    }    

    //Method to parse a string into java.util.Date
    public static Date toDate(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch(Exception ex) { ex.printStackTrace();}
        return date;
    } 
    
    public static Date parse2Date(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        } catch(Exception ex) { ex.printStackTrace();}
        return date;
    }     
    
    //convert java.util.Date to string
    public static String dateToString(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String dat = null;
		try{
            dat = sdf.format(date);
		} catch(Exception ex) { ex.printStackTrace();}
		return dat;
    } 
    
    //method to convert a given sql time to string
    public static String timeToString(java.sql.Time time) {
        return Utils.isNotNull(time) ? new SimpleDateFormat("hh:mm a").format(time) : null;
    }    
  
    //return true if is an integer or a numeric value otherwise false
    public static boolean isNumeric(String stringValue) {
        if (stringValue.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {  
            return true;  
        } else {  
            return false;  
        }    	
    }
    
    /*method to format decimal of number
     *@number : number to format the decimal
     *@decimal : number of decimal to add to the number
     *Example : formatDecimal(20.1,2) ; will return 20.10 or formatDecimal(0.2,3) will return 0.200
     */    
	public static String formatDecimal(double number, int decimal) {
		
		int value = Integer.parseInt(firstIndexOfString(String.valueOf(number), '.'));
		
		int decima = Integer.parseInt(lastIndexOfString(String.valueOf(number), '.'));
		
		String fDecimal = "";
		
		if(decima>=0 && decima <=9) fDecimal = decima+addZeros(decimal-1);
		else if(decima>=10) fDecimal = String.valueOf(decima);
		
		return value+"."+fDecimal;
	}    
	
	private static String addZeros(int number) {
		StringBuffer buff = new StringBuffer();
		
		if(number>0) {
			for(int i=0; i<number; i++) {
				buff.append("0");
			}
		} else return null;
		
		return buff.toString();
	}	
    
    //@len is the length of the of the key to generate.
    public static int genKey(int len) {
        if (len > 18)
            throw new IllegalStateException("Too many digits");
        
        long tLen = (long) Math.pow(10, len - 1) * 9;

        long number = (long) (Math.random() * tLen) + (long) Math.pow(10, len - 1) * 1;

        String tVal = number + "";
        if (tVal.length() != len) {
            throw new IllegalStateException("The random number '" + tVal + "' is not '" + len + "' digits");
        }
        
        return Integer.valueOf(tVal);
    }
    
    //Method to check if a string is under format like MM/dd/yyyy
    public static boolean isADateFormat(String string) {
    	String regex = "^[0-3][0-9]/[0-3][0-9]/(?:[0-9][0-9])?[0-9][0-9]$";
    	return Pattern.compile(regex).matcher(string).matches();  	
    }
    
    //For servlet 3.0 FileUpload----------------------------------------------------------------------------------------------------    
    public static ServletFileUpload fileUploadConfig() {
    	// upload settings
    	final int MEMORY_THRESHOLD 	= 1024 * 1024 * 3; 	// 3MB
    	final int MAX_FILE_SIZE 		= 1024 * 1024 * 50; // 50MB
    	final int MAX_REQUEST_SIZE	= 1024 * 1024 * 55; // 55MB  
    	
		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// sets memory threshold - beyond which files are stored in disk 
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// sets temporary location to store files
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload sfUpload = new ServletFileUpload(factory);
		
		// sets maximum size of upload file
		sfUpload.setFileSizeMax(MAX_FILE_SIZE);
		
		// sets maximum size of request (include file + form data)
		sfUpload.setSizeMax(MAX_REQUEST_SIZE);    	
    	
    	return sfUpload;
    }
    
    //----------------------------------------------------------------------------
    //method to convert a file into a byte array
    public static byte[] toByteArray(File file) {
    	//return new byte[(int) file.length()];
    	try {
			BufferedImage bImage = ImageIO.read(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			ImageIO.write(bImage, "jpg", bos);
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
    	return null;
    }
    
    //method to write a byte array into a file
    public static void writeToFile(byte[] bytearray, String imageFile) {        
        try{
        	FileOutputStream file = new FileOutputStream(imageFile);
        	if(bytearray.length > 0) {
        		//write the bytes to the file (fileName)
        		file.write(bytearray,0,bytearray.length);
        		file.flush();
        		file.close();
        	} else System.out.println(">>>>>>>>>>>>>>> File does not create ...");
        } catch(Exception ex) {  
        	ex.printStackTrace();
        }
    }    
    

}

