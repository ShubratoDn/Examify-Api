package com.api.examify.payloads;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.api.examify.DTO.UserDto;

import jakarta.validation.ConstraintViolation;

public class ValidationResponse {

	public Map<String, String> getValidationErrors(Set<ConstraintViolation<UserDto>> violations) {

		Map<String, String> response = new HashMap<>();

		for (ConstraintViolation<?> violation : violations) {
			String fieldName = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			response.put(fieldName, message);
		}

		return response;
	}

}
