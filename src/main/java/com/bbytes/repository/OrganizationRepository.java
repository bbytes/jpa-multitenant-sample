package com.bbytes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bbytes.domain.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    List<Organization> findByOrgName(String orgName);
}