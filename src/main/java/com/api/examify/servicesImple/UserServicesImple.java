package com.api.examify.servicesImple;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.examify.DTO.UserDto;
import com.api.examify.entities.User;
import com.api.examify.repositories.UserRepo;
import com.api.examify.services.UserServices;

@Service
public class UserServicesImple implements UserServices {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder; 

	//registering user
	@Override
	public UserDto registerUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);		
		
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		User savedUser = userRepo.save(user);		
		UserDto addedUser = modelMapper.map(savedUser, UserDto.class);
		
		return addedUser;
	}
	
	
	//find user by email and role
	public UserDto getUserByEmail(UserDto userDto) {		
		User findByEmail = userRepo.findByEmail(userDto.getEmail());
		if(findByEmail == null) {
			return null;
		}
		UserDto map = modelMapper.map(findByEmail, UserDto.class);		
		return map;		
	}
	
	
	
	
	
	
	//deleting user
	public boolean deleteUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);		
		
		User userx = userRepo.findById(user.getId()).orElse(null);		
        if (userx != null) {
            // Remove roles from the user before deleting
            userx.getRoles().clear();
            userRepo.delete(userx);
            return true;
        }
		
		return false;
	}
	

}
