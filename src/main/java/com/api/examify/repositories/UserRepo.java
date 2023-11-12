package com.api.examify.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.examify.entities.User;
import com.api.examify.entities.UserRole;

public interface UserRepo extends JpaRepository<User, Long> {
	
//	User findByEmailAndRole(String email, UserRole role);
	
	User findByEmail(String email);
	
	List<User> findByNameContainsAndRoles(String name, UserRole role);
}
