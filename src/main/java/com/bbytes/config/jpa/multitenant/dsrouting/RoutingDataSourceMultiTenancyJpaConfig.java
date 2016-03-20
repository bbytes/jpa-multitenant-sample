package com.bbytes.config.jpa.multitenant.dsrouting;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.bbytes.utils.DbUtils;

/**
 * This multi tenant is using spring AbstractRoutingDataSource strategy and not
 * hibernate inbuilt multi tenant feature. Here database and connection pooling
 * per tenant is created. The db is init for the first time . We have to create
 * a default db to maitian tenant info ..it should be called tenant management
 * db. That db should be directly connected using jdbc template and not thru
 * hibernate as hibernate repostory has all the tables and is multi tenant by
 * default
 * 
 * @author admin
 *
 */
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@Profile("saas")
public class RoutingDataSourceMultiTenancyJpaConfig {

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean locationEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(tenantRoutingDataSource());
		em.setPackagesToScan("com.bbytes.domain");
		em.setPersistenceUnitName("multi_tenant_pu");

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(DbUtils.additionalHibernateProperties());

		return em;
	}

	@Bean
	public DataSource tenantRoutingDataSource() {
		TenantRoutingDataSource tenantRoutingDataSource = new TenantRoutingDataSource();
		return tenantRoutingDataSource;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor locationExceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	
}
