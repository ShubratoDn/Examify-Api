package com.api.examify.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.examify.payloads.ExamInformationRequest;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {

	@PostMapping("/add-exam-information")
	public ResponseEntity<String> addExamInformation() {
        System.out.println("REQUEST RECEIVED");
        return ResponseEntity.ok("Exam information created successfully.");
    }
	
	
}
