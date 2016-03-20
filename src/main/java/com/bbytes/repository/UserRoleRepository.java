package com.bbytes.repository;

import org.springframework.data.repository.CrudRepository;

import com.bbytes.domain.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

}