package com.api.examify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.examify.entities.Exam;

public interface ExamRepo extends JpaRepository<Exam, Long> {

}
