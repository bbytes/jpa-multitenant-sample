package com.bbytes.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserRole Domain Object
 * 
 * @author akshay
 */
@Data
@Entity(name="user_role")
@NoArgsConstructor
public class UserRole {

	@Id
	@Column(name="role_name")
	private String roleName;

	@ElementCollection
	@CollectionTable(name="permission", joinColumns=@JoinColumn(name="role_name"))
	@Column(name="permission_name")
	private List<String> permissions = new ArrayList<>();

	public UserRole(String roleName) {
		this.roleName = roleName;
	}

	public static UserRole NORMAL_USER_ROLE = new UserRole("NORMAL");
	public static UserRole ADMIN_USER_ROLE = new UserRole("ADMIN");
}
