package com.api.examify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.examify.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
	
//	User findByEmailAndRole(String email, UserRole role);
	
	User findByEmail(String email);
	
}
