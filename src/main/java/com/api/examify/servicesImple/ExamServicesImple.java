package com.api.examify.servicesImple;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.examify.DTO.UserDto;
import com.api.examify.entities.Exam;
import com.api.examify.entities.User;
import com.api.examify.enums.ExamState;
import com.api.examify.payloads.ExamInformationRequest;
import com.api.examify.repositories.ExamRepo;
import com.api.examify.services.ExamServices;
import com.api.examify.services.UserServices;

@Service
public class ExamServicesImple implements ExamServices {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserServices userServices;
	
	
	@Autowired
	private ExamRepo examRepo;
	

	@Override
	public Exam saveExamInformation(ExamInformationRequest examInformation) {

		//setting author
		UserDto userByEmail = userServices.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		User author = modelMapper.map(userByEmail, User.class);
		
		//setting selected students
		List<UserDto> selectedStudentsDto = examInformation.getSelectedStudents();
		
		List<User> selectedStudents = null;
		
		if(selectedStudentsDto != null && selectedStudentsDto.size() > 0) {
			selectedStudents = new ArrayList<>();
			for(UserDto userDto: selectedStudentsDto) {
				selectedStudents.add(modelMapper.map(userDto, User.class));
			}
		}
		
		
		
		Exam exam = new Exam();
		exam.setAuthor(author);
		exam.setTitle(examInformation.getTitle());
		exam.setCategory(examInformation.getCategory());
		exam.setDescription(examInformation.getDescription());
		exam.setType(examInformation.getType());
		exam.setSelectedStudents(selectedStudents);
		exam.setToken(userServices.generateToken());
		exam.setExamState(ExamState.DRAFT);
		
		Exam savedExam = examRepo.save(exam);		
		
		return savedExam;
	}
	
}
