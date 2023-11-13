package com.api.examify.entities;

import java.util.Date;
import java.util.List;

import com.api.examify.enums.ExamState;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Exam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;

	private String title;

	private String category;

	private String description;

	private String type;

	@ManyToMany
	@JoinTable(name = "exam_selected_students", joinColumns = @JoinColumn(name = "exam_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
	private List<User> selectedStudents;

	@OneToMany(mappedBy = "exam", cascade = CascadeType.MERGE, orphanRemoval = true)
	private List<Question> questions;

	private int duration;

	private Date startingTime;

	@Enumerated(EnumType.STRING)
	private ExamState examState; // disable, enable, draft
	
	private String token;

}
