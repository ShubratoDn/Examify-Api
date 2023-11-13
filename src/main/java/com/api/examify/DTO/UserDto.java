package com.api.examify.DTO;

import java.sql.Timestamp;
import java.util.List;

import com.api.examify.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	
	private Long id;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	private String email;
	
	@NotBlank(message = "Password cannot be empty")
	@Size(min = 4, message = "Password must be at least 4 characters long")
	private String password;
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotNull(message = "Please select a role")
	private List<UserRole> roles;
	
	private Timestamp dateJoin;	
	
	private String image;	
	
	private String about;

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", roles="
				+ roles + ", dateJoin=" + dateJoin + ", image=" + image + ", about=" + about + "]";
	}

	
}
