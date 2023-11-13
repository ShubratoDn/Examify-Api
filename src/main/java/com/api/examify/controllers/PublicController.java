package com.api.examify.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.examify.DTO.UserDto;
import com.api.examify.services.UserServices;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

	@Autowired
	private UserServices userServices;
	
	@GetMapping("/student/")
    public ResponseEntity<?> redirectToDefaultStudent() {
		List<UserDto> studentsByName = userServices.getStudentsByName("SHOULD NOT RECEIVE ANY RESP");
		return ResponseEntity.ok(studentsByName);  
    }
	
	@GetMapping("/student/{username}")
	public ResponseEntity<?> getStudentByName(@PathVariable String username){		
		List<UserDto> studentsByName = userServices.getStudentsByName(username);
		return ResponseEntity.ok(studentsByName);
	}
	
}
