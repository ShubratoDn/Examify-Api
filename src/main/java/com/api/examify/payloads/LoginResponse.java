package com.api.examify.payloads;

import com.api.examify.DTO.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	private UserDto user;
	private String token;
	
}
