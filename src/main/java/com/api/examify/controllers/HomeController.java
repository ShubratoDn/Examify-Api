package com.api.examify.controllers;

import org.springframework.http.ResponseEntity;
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
	
}