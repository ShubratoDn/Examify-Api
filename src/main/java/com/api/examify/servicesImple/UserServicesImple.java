package com.api.examify.servicesImple;

import java.security.SecureRandom;
import java.util.Optional;

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
	
	
	
	private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-";
    private final int TOKEN_LENGTH = 50; // Adjust the length as needed

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            token.append(randomChar);
        }

        return token.toString();
    }


	//registering user
	@Override
	public UserDto registerUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);		
		
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setToken(generateToken());
		
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


	@Override
	public UserDto getUserById(Long id) {
		Optional<User> findById = userRepo.findById(id);
		User user = (User) findById.get();
		UserDto map = modelMapper.map(user, UserDto.class);
		return map;
	}
	

}
