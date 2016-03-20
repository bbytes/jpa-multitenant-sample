package com.bbytes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bbytes.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByEmail(String email);
}