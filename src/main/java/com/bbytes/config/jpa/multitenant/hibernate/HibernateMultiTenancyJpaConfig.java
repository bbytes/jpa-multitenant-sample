//package com.bbytes.config.jpa.multitenant.hibernate;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import org.flywaydb.core.Flyway;
//import org.hibernate.MultiTenancyStrategy;
//import org.hibernate.cfg.Environment;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//
///**
// * In this approach we use hibernate multi tenant db approach . Database and
// * connection pooling per tenant
// * 
// * @author Thanneer
// *
// */
//@Configuration
//@EnableConfigurationProperties(JpaProperties.class)
//public class HibernateMultiTenancyJpaConfig {
//
//	@Autowired
//	private DataSource dataSource;
//
//	@Autowired
//	private JpaProperties jpaProperties;
//
//	@Autowired
//	private MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl;
//
//	@Autowired
//	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolverImpl;
//
//	@Value("${flyway.migration.file.location.default}")
//	private String flywayScriptDefaultDb;
//
//	@Bean(initMethod = "migrate")
//	public Flyway flyway() {
//		Flyway fly = new Flyway();
//		fly.setLocations(flywayScriptDefaultDb);
//		fly.setDataSource(dataSource);
//		fly.baseline();
//		fly.migrate();
//		return fly;
//	}
//
//	@Bean(name = "entityManagerFactory")
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
//		Map<String, Object> hibernateProps = new LinkedHashMap<>();
//		hibernateProps.putAll(jpaProperties.getHibernateProperties(dataSource));
//
//		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
//		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
//		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);
//		hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
//
//		return builder.dataSource(multiTenantConnectionProviderImpl.selectAnyDataSource()).packages("com.bbytes.domain")
//				.properties(hibernateProps).jta(false).build();
//	}
//}