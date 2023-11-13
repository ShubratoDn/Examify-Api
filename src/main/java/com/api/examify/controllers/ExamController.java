package com.api.examify.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.examify.entities.Exam;
import com.api.examify.payloads.ApiResponse;
import com.api.examify.payloads.ExamInformationRequest;
import com.api.examify.services.ExamServices;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {
	
	
	@Autowired
	private ExamServices examServices;
	

	@PostMapping("/add-exam-information")
	public ResponseEntity<?> addExamInformation(@RequestBody ExamInformationRequest examInformationRequest) {
		
		if(examInformationRequest.getType().equalsIgnoreCase("specified")) {
			if(examInformationRequest.getSelectedStudents().size() < 1) {
				return ResponseEntity.badRequest().body(new ApiResponse("type","Please, select minimum 1 student."));
			}
		}else {
			examInformationRequest.setSelectedStudents(null);
		}
		
		
		Exam saveExamInformation = examServices.saveExamInformation(examInformationRequest);		
		
        System.out.println("REQUEST RECEIVED" + SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(examInformationRequest);
		
        return ResponseEntity.ok(saveExamInformation);
    }
	
	
}
