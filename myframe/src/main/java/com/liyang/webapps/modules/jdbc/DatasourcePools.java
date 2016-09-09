package com.liyang.webapps.modules.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.liyang.webapps.modules.config.Global;



public class DatasourcePools {
	
	private static Map<String, BasicDataSource> datasourcePools = new HashMap<String, BasicDataSource>();;
	
	private static Logger logger = Logger.getLogger(DatasourcePools.class);
	
	
	
	
	public static void initDataSource(String datasource){  
		
		  BasicDataSource bds =datasourcePools.get(datasource);
		   
		   if(bds==null) {
			   bds = new BasicDataSource(); 
			   String url = Global.getConfig(datasource+".jdbc.url");
			   String driver = Global.getConfig(datasource+".jdbc.driver");
			   String username = Global.getConfig(datasource+".jdbc.username");
			   String password = Global.getConfig(datasource+".jdbc.password");
			   
			   if(url==null || driver==null || username==null || password==null) {
				   logger.error("数据源配置出错，请关注: " + datasource);
			   }
	          
		        bds.setUrl(url);  
		        bds.setDriverClassName(driver);  
		        bds.setUsername(username);  
		        bds.setPassword(password);  
		        datasourcePools.put(datasource, bds);
		   }	
    }  
	
	
	 public static Connection  getConnection(String datasource) throws  SQLException {  
		    DataSource dataSource = datasourcePools.get(datasource);
		    
		    if(dataSource==null) {
		    	initDataSource(datasource);
		    }
	       
	        Connection conn = null;     
	        if (dataSource != null) {     
	            conn = dataSource.getConnection();     
	        }     
	        return conn;     
	    }  

}
