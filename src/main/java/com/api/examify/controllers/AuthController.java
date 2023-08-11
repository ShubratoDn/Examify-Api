package com.api.examify.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.examify.DTO.UserDto;
import com.api.examify.configs.Constants;
import com.api.examify.configs.jwt.JWTTokenUtil;
import com.api.examify.configs.security.CustomUserDetailsServiceImpl;
import com.api.examify.entities.UserRole;
import com.api.examify.payloads.ApiResponse;
import com.api.examify.payloads.LoginRequest;
import com.api.examify.payloads.LoginResponse;
import com.api.examify.payloads.ValidationResponse;
import com.api.examify.repositories.UserRoleRepo;
import com.api.examify.services.FileServices;
import com.api.examify.services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private FileServices fileServices;

	@Autowired
	private UserRoleRepo userRoleRepo;

	@Autowired
	private UserServices userServices;
	
	@Autowired
	private AuthenticationManager authenticationManager;
		
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestParam(value = "image", required = false) MultipartFile file,
			@RequestParam(value = "data", required = false) String data) {

		// step 1: converting the Data into UserDto object
		UserDto userDto = null;
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

		
				
		
		// user role validation
		Optional<UserRole> findUserRole = userRoleRepo.findById(userDto.getRoles().get(0).getId());
		if (!findUserRole.isPresent()) {
			return ResponseEntity.badRequest().body(new ApiResponse("role", "Invalid user role"));
		} else {
			UserRole userRole = findUserRole.get();
			userDto.setRoles(List.of(userRole));
		}

		
		
		//setting about
		if (userDto.getAbout() == null || userDto.getAbout().isBlank()) {
			userDto.setAbout("I'm " + userDto.getName() + ". And I'm a " + userDto.getRoles().get(0).getRole());
		}

		
		//checking if the user is already exist in the database or not
		if(userServices.getUserByEmail(userDto) != null)  {
			Map<String, String> map = new HashMap<>();
			map.put("email", "Email already exist for this role");
			return ResponseEntity.badRequest().body(map);
		}
		
		
		
		// image validation
		Map<String, String> fileValidation = fileServices.fileValidation(file, Constants.FILE_USER_IMAGE);
		if (fileValidation != null) {
			return new ResponseEntity<>(fileValidation, HttpStatus.BAD_REQUEST);
		}

		// upload file
		String fileName = fileServices.uploadFile(file, Constants.FILE_USER_IMAGE);
		if (fileName == null) {
			return ResponseEntity.badRequest().body(new ApiResponse("error", "File Upload failed"));
		} else {
			userDto.setImage(fileName);
		}


		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a");
		String formattedDate = sdf.format(Timestamp.from(Instant.now()));
		System.out.println("User join at : "+formattedDate); // Print the formatted date

		
		//setting current time 
		userDto.setDateJoin(Timestamp.from(Instant.now()));
		
		
		// register user
		UserDto registerUser = userServices.registerUser(userDto);

		return ResponseEntity.ok(registerUser);
	}
	
	
	
	
	
	
	//deleting user
	@GetMapping("/delete/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id){
		UserDto user = new UserDto();
		user.setId(id);
		
		boolean deleteUser = userServices.deleteUser(user);
		if(deleteUser) {
			return ResponseEntity.ok(new ApiResponse("success", "User deleted"));
		}else {
			return ResponseEntity.ok(new ApiResponse("error", "User Can not be deleted"));
		}
	}
	
	
	
	
	
	//login user
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		//authentication kortesi j User er password thik ache ki na
		 try {
	        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	    } catch (DisabledException e) {
	        return ResponseEntity.badRequest().body(new ApiResponse("error", "User account is disabled."));
	    } catch (BadCredentialsException e) {
	        // Return a response for incorrect password
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("error", "Incorrect username or password."));
	    } catch (InternalAuthenticationServiceException e) {
	        return ResponseEntity.badRequest().body(new ApiResponse("error", "User not found."));
	    }
		
		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
		String token = jwtTokenUtil.generateToken(userDetails);
		
		
		
		UserDto userDto = new UserDto();
		userDto.setEmail(username);
		
		UserDto user = userServices.getUserByEmail(userDto);
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setUser(user);
		
		return ResponseEntity.ok(loginResponse);
	}
	
	

}
