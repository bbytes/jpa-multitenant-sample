//package com.bbytes.config.jpa.multitenant.hibernate;
//
//import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
//
//	private static final String DEFAULT_TENANT_ID = "tenant_management";
//
//	@Override
//	public String resolveCurrentTenantIdentifier() {
//
//		String identifier = TenantContextHolder.getTenantName();
//		if (identifier != null) {
//			return identifier;
//		}
//
//		return DEFAULT_TENANT_ID;
//	}
//
//	@Override
//	public boolean validateExistingCurrentSessions() {
//		return true;
//	}
//}