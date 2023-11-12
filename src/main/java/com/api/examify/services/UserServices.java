package com.api.examify.services;

import java.util.List;

import com.api.examify.DTO.UserDto;
public interface UserServices {

	public UserDto registerUser(UserDto user);
	
	public UserDto getUserByEmail(UserDto userDto);
	
	public UserDto getUserById(Long id);
	
	public boolean deleteUser(UserDto userDto);
	
	public List<UserDto> getStudentsByName(String studentName);
	
}
