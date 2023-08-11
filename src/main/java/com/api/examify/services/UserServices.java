package com.api.examify.services;

import com.api.examify.DTO.UserDto;
public interface UserServices {

	public UserDto registerUser(UserDto user);
	
	public UserDto getUserByEmail(UserDto userDto);
	
	
	public boolean deleteUser(UserDto userDto);
	
}
