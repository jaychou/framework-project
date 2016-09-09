package com.liyang.webapps.modules.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.liyang.webapps.modules.config.Global;


public class JdbcTemplateFactory {
	private static Map<String, BasicDataSource> datasourcePools = new HashMap<String, BasicDataSource>();
	private static Logger logger = Logger.getLogger(JdbcTemplateFactory.class);

	public static JdbcTemplate getJdbcTemplate(String datasource) {
		JdbcTemplate tempalte = null;
		try {
			DataSource dataSource = datasourcePools.get(datasource);

			if (dataSource == null) {
				dataSource = initDataSource(datasource);
			}
			tempalte = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return tempalte;
	}

	public static DataSource initDataSource(String datasource) {

		BasicDataSource bds = datasourcePools.get(datasource);

		if (bds == null) {
			bds = new BasicDataSource();
			String url = Global.getConfig(datasource + ".jdbc.url");
			String driver = Global.getConfig(datasource + ".jdbc.driver");
			String username = Global.getConfig(datasource + ".jdbc.username");
			String password = Global.getConfig(datasource + ".jdbc.password");

			if (url == null || driver == null || username == null
					|| password == null) {
				logger.error("数据源配置出错，请关注: " + datasource);
			}

			bds.setUrl(url);
			bds.setDriverClassName(driver);
			bds.setUsername(username);
			bds.setPassword(password);
			datasourcePools.put(datasource, bds);
		}
		
		return bds;
	}

}
