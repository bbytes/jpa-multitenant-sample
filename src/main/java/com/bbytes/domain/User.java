package com.bbytes.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Domain Object
 * 
 * @author akshay
 */
@Data
@Entity(name="user")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id")
	private Long userId;

	private String name;

	@Column(unique=true)
	private String email;

	private String status;

	private String password;

	@Column(name="account_initialise")
	private boolean accountInitialise;

	@ManyToOne(optional = false)
	@JoinColumn(name = "org_id")
	private Organization organization;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_role")
	private UserRole userRole = UserRole.NORMAL_USER_ROLE;

	@CreatedDate
	private DateTime creationDate;

	@LastModifiedDate
	private DateTime lastModified;

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public static String PENDING = "Pending";
	public static String JOINED = "Joined";
}
