package com.bbytes.domain;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * Part of tenant management DB .It stores the link between tenant id and user
 * email. This is used to resolve tenant id given email
 * 
 * @author akshay
 *
 */
@Data
@Entity(name="tenant_resolver")
public class TenantResolver {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String email;
	
	@Column(name="user_id")
	private Long userId;

	@Column(name="org_id")
	private String orgId;

}
