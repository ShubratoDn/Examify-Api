package com.api.examify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.examify.entities.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
	
}
