package com.bbytes.utils;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbUtils {
	private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);

	public static String databaseNameFromJdbcUrl(String url, String newDbName) {
		try {
			String cleanURI = url.substring(5);

			URI uri = URI.create(cleanURI);
			String newUrl = "jdbc:" + uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + "/" + newDbName
					+ "?createDatabaseIfNotExist=true";
			System.out.println(newUrl);
			return newUrl;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Properties additionalHibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.setProperty("hibernate.ddl-auto", "none");
		properties.setProperty("show-sql", "true");

		return properties;
	}

	public static void initDb(DataSource dataSource) throws SQLException {
		Connection connection = dataSource.getConnection();
		try {
			HibernateExporter exporter = new HibernateExporter("org.hibernate.dialect.MySQL5Dialect",
					"com.bbytes.domain");
			String dbInitDDLScript[] = exporter.exportUpdateScript(connection);

			if (dbInitDDLScript != null && dbInitDDLScript.length > 0) {
				try {

					for (String query : dbInitDDLScript) {
						JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
						jdbcTemplate.execute(query);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (connection != null)
				connection.close();
		}

	}

}