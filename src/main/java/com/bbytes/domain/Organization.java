package com.bbytes.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.joda.time.DateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Organization Domain Object
 * 
 * @author akshay
 */

@Data
@Entity(name="organization")
@NoArgsConstructor
public class Organization {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String orgId;

	@Column(name="org_name",unique=true)
	private String orgName;

	@Column(name="time_preference")
	private String timePreference;

	private DateTime creationDate;

	private DateTime lastModified;

	public Organization(String orgId, String orgName) {
		this.orgId = orgId;
		this.orgName = orgName;
	}

}
