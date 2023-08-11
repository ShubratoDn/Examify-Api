package com.api.examify;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.api.examify.configs.Constants;
import com.api.examify.entities.UserRole;
import com.api.examify.repositories.UserRoleRepo;

@SpringBootApplication
public class ExamifyApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ExamifyApiApplication.class, args);
	}
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	
	@Autowired
	private UserRoleRepo userRoleRepo;
	
	@Override
	public void run(String... args) throws Exception {
	
		UserRole role1 = new UserRole();
		role1.setId(Constants.ROLE_ADMIN);
		role1.setRole("ADMIN");
		
		UserRole role2 = new UserRole();
		role2.setId(Constants.ROLE_TEACHER);
		role2.setRole("TEACHER");
		
		UserRole role3 = new UserRole();
		role3.setId(Constants.ROLE_STUDENT);
		role3.setRole("STUDENT");
		
		
		  // Create a list of UserRole instances
	    List<UserRole> userRoles = List.of(role1, role2, role3);
	    
	    // Save all UserRole instances to the database
	    userRoleRepo.saveAll(userRoles);
		
		
		
	}

}
