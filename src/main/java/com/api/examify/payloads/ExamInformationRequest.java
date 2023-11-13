package com.api.examify.payloads;

import java.util.List;

import com.api.examify.DTO.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamInformationRequest {

	private String title;
    private String category;
    private String description;
    private String type;
    private List<UserDto> selectedStudents; // Assuming student IDs are Long
	@Override
	public String toString() {
		return "ExamInformationRequest [title=" + title + ", category=" + category + ", description=" + description
				+ ", type=" + type + ", selectedStudents=" + selectedStudents + "]";
	}
	
    
}
