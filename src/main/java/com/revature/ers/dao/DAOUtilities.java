/**
 * DAOUtilities.java
 * Created on 2021-01-26 @ 04:14 pm
 * Author: Hector Vertus
 */
package com.revature.ers.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;

import com.revature.ers.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Class used to retrieve DAO Implementations. Serves as a factory.
 */
public abstract class DAOUtilities {
	private static Logger log = Logger.getLogger(DAOUtilities.class);

	// Hibernate
	// configuration---------------------------------------------------------------------------------
	/*
	 * Creating the HibernateUtil.java Helper File
	 * 
	 * To use Hibernate, you need to create a helper class that handles startup and
	 * access Hibernate's SessionFactory to obtain a Session object
	 * 
	 * 
	 * (interface) Session manages the connection to DB and provides CRUD operations
	 * (create, read, update, delete)
	 * 
	 * (class) Configuration's job is to gather info from the hiberate.cfg.xml file
	 * and then build the Session Factory.
	 * 
	 * (interface) Session Factory's job is to create sessions and store info on how
	 * to make connections to your DB
	 * 
	 * (interface) Transaction manages..well...your transactions and cache (must be
	 * ACID).
	 * 
	 * 
	 * Query is used to write complex CRUD operations using HQL (Hibernate Query
	 * Language)
	 * 
	 * Criteria is for programmatically writing Select queries
	 */
	private static Session ses;

	// We will use the SessionFactory interface to create a Configuration()
	// Object which will call the .configure method on on our "hibernate.cfg.xml"
	private static SessionFactory sf = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	// We create a getSession() method which is going to be called in our DAO layer
	// This method obtains a JDBC connection which we can use to perform a
	// transaction against our DB
	public static Session getSession() {
		if (ses == null) {
			ses = sf.openSession();
		}

		return ses;
	}

	// This closes an active session.
	public static void closeResources() {
		ses.close();
		sf.close();
		// By closing our session, you free up the connections from the conn pool
		// so that it can be used by a new session.
	}

	public static int save(Object... object) {
		log.info("<=============:: Inside save(Object... object)");
		int saved = 0;
		Transaction trx = null;
		try {
			log.info("<=============:: Reading the object to save :: " + object.getClass().getName());
			Session session = getSession(); // 1. capture the session

			// The Transaction Interface is used for managing ACID complient transactions
			// against the DB
			// Transaction is a mechanism for dispatching SQL statements against the DB
			trx = session.beginTransaction(); // 2. Perform an operation on the DB

			for (Object obj : object) {
				//Id will be last id of the last object saved. Be carefull with this.
				saved = (Integer) ses.save(obj); // 3. use the save() session method to perform an insert operation
			}

			log.info("<>--------- Commit the transaction");
			trx.commit(); // 4. commit the transaction by utilizing a method from the actual Transaction
							// interface;
			log.info("<=============:: The object is persisted or save properly");
		} catch (Exception exp) {
			if (Utils.isNotNull(trx)) {
				trx.rollback();
			}

			log.error(">---------:: Unable to persist the object(...)", exp);
		} finally {
			// closeResources();
		}

		log.info("<=============:: Exit save(Object object)");
		return saved;
	}

	public boolean update(Object object, boolean merged) {
		log.info("<=============:: Inside update(Object object, boolean merged) ");
		boolean updated = false;
		Transaction trx = null;

		try {
			Session session = getSession();
			trx = session.beginTransaction();
			log.info("<>--------- Update the object:: "+object.getClass().getName());
			if(!merged) {
				session.update(object);
			} else {
				session.merge(object);
			}
			log.info("<>--------- Commit the transaction");
			trx.commit();
			log.info("<=============:: The object is updated properly");
			updated = true;
		} catch (Exception exp) {
			if (Utils.isNotNull(trx)) {
				trx.rollback();
			}

			log.error(">---------:: Unable to persist the object(...)", exp);
		} finally {
			// closeResources();
		}

		log.info("<=============:: Exit update(Object object, boolean merged)");
		return updated;
	}	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectById(Class clazz, int id) {
		log.info("<=============:: Inside selectById(int id)");
		Session session = getSession();
		Object obj = session.get(clazz, id);
		log.info("<=============:: Exit selectById(int id)");
		return obj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> selectAll(String hql, Class clazz) {
		Session session = getSession();
		// HQL -- Hibernate Query Language
		// Anything marked with @Entity can be queried by HQL
		List<?> objects = session.createQuery(hql, clazz).list();

		return objects;
	}
	
    @SuppressWarnings("unchecked")
	public List<Object> selectAll(String namedQuery) {   
    	if(Utils.isNotBlank(namedQuery)) {
    		Session session = getSession();
    		return session.createNamedQuery(namedQuery)
	    				  .getResultList();
    	} 
    	    	
    	return null;    	    	
    }
    
    @SuppressWarnings("unchecked")
	public List<Object> selectAll(String namedQuery, int param) {   
    	if(Utils.isNotBlank(namedQuery)) {
    		Session session = getSession();
    		return session.createNamedQuery(namedQuery)
	    				  .setParameter(1, param)
	    				  .getResultList();
    	} 
    	    	
    	return null;    	    	
    }	
    
    @SuppressWarnings("unchecked")
	public List<Object> selectAll(String namedQuery, String param) {   
    	if(Utils.isNotBlank(namedQuery)) {
    		Session session = getSession();
    		return session.createNamedQuery(namedQuery)
	    				  .setParameter(1, param)
	    				  .getResultList();
    	} 
    	    	
    	return null;    	    	
    }          

	// ------------------------------------------------------------------------------------------------
	private static Connection connection;

	// Method to connect to the database
	public static synchronized Connection getConnection() throws SQLException {
		Properties properties = getProperties();

		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver"); // setup the driver
			} catch (ClassNotFoundException e) {
				System.out.println("Could not register driver!");
				e.printStackTrace();
			}

			// get the connection
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"),
					properties.getProperty("password"));
		}

		// If connection was closed then retrieve a new connection
		if (connection.isClosed()) {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"),
					properties.getProperty("password"));
		}

		return connection;
	}

	// Method to get properties to connect with the database
	private static Properties getProperties() {
		Properties properties = new Properties();
		properties.put("url", "jdbc:postgresql://localhost:5432/ers?currentSchema=ers_admin");
		properties.put("username", "postgres");
		properties.put("password", "postgres");
		return properties;
	}

}
