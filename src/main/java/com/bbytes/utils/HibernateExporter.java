package com.bbytes.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.tool.hbm2ddl.SchemaUpdateScript;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will create an hibernate {@link Configuration} with the given
 * dialect and will scan provided package for {@link MappedSuperclass} and
 * {@link Entity}. You can then use the export methods to generate your schema
 * DDL. Taken from @author Geoffroy Warin (https://github.com/geowarin)
 *
 */
public class HibernateExporter {

	private static Logger logger = LoggerFactory.getLogger(HibernateExporter.class);

	private String dialect;
	private String entityPackage;

	private Configuration hibernateConfiguration;

	public HibernateExporter(String dialect, String entityPackage) {
		this.dialect = dialect;
		this.entityPackage = entityPackage;

		hibernateConfiguration = createHibernateConfig();
	}

	public String[] exportUpdateScript(Connection con) {
		Dialect hibDialect = Dialect.getDialect(hibernateConfiguration.getProperties());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintWriter writer = new PrintWriter(byteArrayOutputStream)) {

			DatabaseMetadata metadata = new DatabaseMetadata(con, hibDialect, hibernateConfiguration);
			List<SchemaUpdateScript> updateSQL = hibernateConfiguration.generateSchemaUpdateScriptList(hibDialect,
					metadata);

			return SchemaUpdateScript.toStringArray(updateSQL);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String[] exportCreateScript() {
		Dialect hibDialect = Dialect.getDialect(hibernateConfiguration.getProperties());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintWriter writer = new PrintWriter(byteArrayOutputStream)) {

			String[] createSQL = hibernateConfiguration.generateSchemaCreationScript(hibDialect);
			return createSQL;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String[] exportDropScript() {
		Dialect hibDialect = Dialect.getDialect(hibernateConfiguration.getProperties());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintWriter writer = new PrintWriter(byteArrayOutputStream)) {

			String[] dropSQL = hibernateConfiguration.generateDropSchemaScript(hibDialect);

			return dropSQL;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private Configuration createHibernateConfig() {
		hibernateConfiguration = new Configuration();

		final Reflections reflections = new Reflections(entityPackage);
		for (Class<?> cl : reflections.getTypesAnnotatedWith(MappedSuperclass.class)) {
			hibernateConfiguration.addAnnotatedClass(cl);
			logger.debug("Mapped = " + cl.getName());
		}
		for (Class<?> cl : reflections.getTypesAnnotatedWith(Entity.class)) {
			hibernateConfiguration.addAnnotatedClass(cl);
			logger.debug("Mapped = " + cl.getName());
		}
		hibernateConfiguration.setProperty(AvailableSettings.DIALECT, dialect);
		hibernateConfiguration.setProperties(DbUtils.additionalHibernateProperties());
		return hibernateConfiguration;
	}

	public Configuration getHibernateConfiguration() {
		return hibernateConfiguration;
	}

	public void setHibernateConfiguration(Configuration hibernateConfiguration) {
		this.hibernateConfiguration = hibernateConfiguration;
	}
}