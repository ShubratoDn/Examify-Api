package com.api.examify.DTO;

import com.api.examify.enums.QuestionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {

    private Long id;
    private ExamDto exam; // To store the ID of the associated exam
    private String question;
    private QuestionType questionType;
    private String answer;
    private int mark;
}