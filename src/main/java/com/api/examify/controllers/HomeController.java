package com.api.examify.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/")
public class HomeController {

	@GetMapping("/test")
	public ResponseEntity<?> test(){
		return ResponseEntity.ok("Working smoothly");
	}
	
	
	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/test2")
	public ResponseEntity<?> test2(){		 
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		
		
		
		
		return ResponseEntity.ok("This is TEST 2 : " + name);
	}
	
}
