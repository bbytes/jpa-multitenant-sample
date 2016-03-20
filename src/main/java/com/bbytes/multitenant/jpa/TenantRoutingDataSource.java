package com.bbytes.multitenant.jpa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import com.bbytes.utils.DbUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Profile("saas")
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory.getLogger(TenantRoutingDataSource.class);

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.dataSourceClassName}")
	private String dataSourceClassName;

	@Value("${spring.datasource.username}")
	private String user;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${flyway.migration.file.location.tenant}")
	private String flywayScriptTenantDb;

	// testing purpose the tenant name list hardcoded, needs to come from db tenant management 
	@Value("${tenant.list.init}")
	private String initTenantList;

	private List<String> tenants;

	private Map<Object, DataSource> resolvedDataSources;

	private DataSource resolvedDefaultDataSource;

	private boolean lenientFallback = true;

	@Override
	protected Object determineCurrentLookupKey() {
		return TenantContextHolder.getTenantName();
	}

	/**
	 * Init all the tenant database , conn pool and run init sql 
	 */
	@Override
	public void afterPropertiesSet() {

		this.resolvedDataSources = new HashMap<Object, DataSource>();
		tenants = Arrays.asList(initTenantList.split("\\s*,\\s*"));

		try {
			logger.debug("Configuring datasource {} {} {}", dataSourceClassName, url, user);
			HikariConfig defaultConfig = new HikariConfig();
			defaultConfig.setDataSourceClassName(dataSourceClassName);
			defaultConfig.addDataSourceProperty("url", url);
			defaultConfig.addDataSourceProperty("user", user);
			defaultConfig.addDataSourceProperty("password", password);
			resolvedDefaultDataSource = new HikariDataSource(defaultConfig);
			DbUtils.initDb(resolvedDefaultDataSource);

			for (String tenantName : tenants) {
				String tenantDbUrl = DbUtils.databaseNameFromJdbcUrl(url, tenantName);
				logger.debug("Configuring datasource {} {} {}", dataSourceClassName, tenantDbUrl, user);
				HikariConfig config = new HikariConfig();
				config.setDataSourceClassName(dataSourceClassName);
				config.addDataSourceProperty("url", tenantDbUrl);
				config.addDataSourceProperty("user", user);
				config.addDataSourceProperty("password", password);
				config.setInitializationFailFast(true);
				HikariDataSource tenantDataSource = new HikariDataSource(config);
				resolvedDataSources.put(tenantName, tenantDataSource);
				// init tenant dbs
				DbUtils.initDb(tenantDataSource);
			}

		} catch (Exception e) {
			logger.error("Error in database URL {}", url, e);
		}

	}

	
	/**
	 * Retrieve the current target DataSource. Determines the
	 * {@link #determineCurrentLookupKey() current lookup key}, performs a
	 * lookup in the {@link #setTargetDataSources targetDataSources} map, falls
	 * back to the specified {@link #setDefaultTargetDataSource default target
	 * DataSource} if necessary.
	 * 
	 * @see #determineCurrentLookupKey()
	 */
	protected DataSource determineTargetDataSource() {
		Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
		Object lookupKey = determineCurrentLookupKey();
		DataSource dataSource = this.resolvedDataSources.get(lookupKey);
		if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
			dataSource = this.resolvedDefaultDataSource;
		}
		if (dataSource == null) {
			throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
		}
		return dataSource;
	}

}