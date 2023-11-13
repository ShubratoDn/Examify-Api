package com.api.examify.DTO;

import java.util.Date;
import java.util.List;

import com.api.examify.enums.ExamState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamDto {

    private Long id;
    private UserDto author; // To store the ID of the author
    private String title;
    private String category;
    private String description;
    private String type;
    private List<UserDto> selectedStudents; // To store the IDs of selected students
    private List<QuestionDto> questions;
    private int duration;
    private Date startingTime;
    private ExamState examState;

    private String token;
    // Constructors, getters, and setters
}