package com.api.examify.DTO;

import java.sql.Timestamp;

import com.api.examify.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	
	private Long id;
	
	@NotBlank(message = "Insert Email")
	@Email(message = "Insert a valid email")
	private String email;
	
	@Size(min = 4, message = "Password need minimum 4 character")
	private String password;
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@NotBlank(message = "Insert your name")
	private String name;
	
	private UserRole role;
	
	private Timestamp date_join;	
	
	private String image;	
	
}
