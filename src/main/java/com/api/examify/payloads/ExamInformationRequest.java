package com.api.examify.payloads;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamInformationRequest {

	private String title;
    private String category;
    private String description;
    private String type;
    private List<Long> selectedStudents; // Assuming student IDs are Long
	@Override
	public String toString() {
		return "ExamInformationRequest [title=" + title + ", category=" + category + ", description=" + description
				+ ", type=" + type + ", selectedStudents=" + selectedStudents + "]";
	}
	
    
}
