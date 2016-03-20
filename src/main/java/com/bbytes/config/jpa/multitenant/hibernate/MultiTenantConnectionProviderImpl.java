//package com.bbytes.config.jpa.multitenant.hibernate;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//
//import org.flywaydb.core.Flyway;
//import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.bbytes.utils.DbUtils;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//@Component
//public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
//
//	private static final long serialVersionUID = -4080256318650811147L;
//
//	private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);
//
//	private Map<String, DataSource> tenantNameToDataSource;
//
//	@Value("${spring.datasource.url}")
//	private String url;
//
//	@Value("${spring.datasource.dataSourceClassName}")
//	private String dataSourceClassName;
//
//	@Value("${spring.datasource.username}")
//	private String user;
//
//	@Value("${spring.datasource.password}")
//	private String password;
//
//	@Value("${flyway.migration.file.location.tenant}")
//	private String flywayScriptTenantDb;
//	
//	@Value("${tenant.list.init}")
//	private String initTenantList;
//
//	private DataSource defaultDataSource;
//
//	private List<String> tenants;
//
//	@PostConstruct
//	public void load() {
//		tenantNameToDataSource = new HashMap<>();
//		// testing purpose
//		tenants = Arrays.asList(initTenantList.split("\\s*,\\s*"));
//		
//		try {
//			logger.debug("Configuring datasource {} {} {}", dataSourceClassName, url, user);
//			HikariConfig defaultConfig = new HikariConfig();
//			defaultConfig.setDataSourceClassName(dataSourceClassName);
//			defaultConfig.addDataSourceProperty("url", url);
//			defaultConfig.addDataSourceProperty("user", user);
//			defaultConfig.addDataSourceProperty("password", password);
//			defaultDataSource = new HikariDataSource(defaultConfig);
//
//			for (String tenantName : tenants) {
//				String tenantDbUrl = DbUtils.databaseNameFromJdbcUrl(url,tenantName);
//				logger.debug("Configuring datasource {} {} {}", dataSourceClassName, tenantDbUrl, user);
//				HikariConfig config = new HikariConfig();
//				config.setDataSourceClassName(dataSourceClassName);
//				config.addDataSourceProperty("url", tenantDbUrl);
//				config.addDataSourceProperty("user", user);
//				config.addDataSourceProperty("password", password);
//				HikariDataSource tenantDataSource = new HikariDataSource(config);
//				tenantNameToDataSource.put(tenantName, tenantDataSource);
//				// init tenant dbs
//				initDbWithFlyway(defaultDataSource);
//			}
//
//		} catch (Exception e) {
//			logger.error("Error in database URL {}", url, e);
//		}
//	}
//
//	private void initDbWithFlyway(DataSource dataSource) {
//		Flyway fly = new Flyway();
//		fly.setLocations(flywayScriptTenantDb);
//		fly.setDataSource(dataSource);
//		fly.baseline();
//		fly.migrate();
//
//	}
//
//	@Override
//	protected DataSource selectAnyDataSource() {
//		return defaultDataSource;
//	}
//
//	@Override
//	protected DataSource selectDataSource(String tenantIdentifier) {
//		logger.debug("+++++++++++ Selecting data source for {}", tenantIdentifier);
//		return tenantNameToDataSource.containsKey(tenantIdentifier) ? tenantNameToDataSource.get(tenantIdentifier)
//				: defaultDataSource;
//
//	}
//
//}