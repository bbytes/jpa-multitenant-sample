//package com.bbytes.config.jpa.multitenant.hibernate;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class TenantContextHolder {
//
//	private static final Logger logger = LoggerFactory.getLogger(TenantContextHolder.class);
//
//	/**
//	 * This is used to store the tenant name of the account during log in
//	 */
//	private static ThreadLocal<String> tenantName = new ThreadLocal<String>();
//
//	/**
//	 * @return the tenantName
//	 */
//	public static String getTenantName() {
//		return tenantName.get();
//	}
//
//	public static void setTenantName(final String tenant) {
//		tenantName.set(tenant);
//	}
//
//	/**
//	 * Clears the context. This should be called after a user has logged out.
//	 */
//	public static void clearContext() {
//		logger.debug("Clearing tenant context.");
//		tenantName.remove();
//	}
//}