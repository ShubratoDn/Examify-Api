package com.api.examify.controllers;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.examify.DTO.UserDto;
import com.api.examify.configs.Constants;
import com.api.examify.payloads.ApiResponse;
import com.api.examify.payloads.ValidationResponse;
import com.api.examify.services.FileServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {	

	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private FileServices fileServices;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(
			@RequestParam(value = "image", required = false) MultipartFile file,
			@RequestParam(value = "data", required = false) String data
			){
		
		//step 1: converting the Data into UserDto object
		UserDto userDto =null;
		try {
			userDto = mapper.readValue(data, UserDto.class);
		} catch (Exception e) {			
			log.error("Converting Data failed" + e);
			return ResponseEntity.badRequest().body(new ApiResponse("error", "Invalid data or data structure"));
		} 		
		
		
		// step 2: input validation process
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);		
		
		if (!violations.isEmpty()) {
			ValidationResponse validResp = new ValidationResponse();
			Map<String, String> validationErrors = validResp.getValidationErrors(violations);
			return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
		}
		
		
		
		//image validation
		Map<String, String> fileValidation = fileServices.fileValidation(file, Constants.FILE_USER_IMAGE);
		if(fileValidation != null) {
			return new ResponseEntity<>(fileValidation, HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(userDto);		
	}
	
}
