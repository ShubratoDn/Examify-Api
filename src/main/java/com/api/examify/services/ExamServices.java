package com.api.examify.services;

import com.api.examify.entities.Exam;
import com.api.examify.payloads.ExamInformationRequest;

public interface ExamServices {


	public Exam saveExamInformation(ExamInformationRequest examInformation);
	
}
