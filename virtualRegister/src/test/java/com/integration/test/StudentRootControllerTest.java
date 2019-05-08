package com.integration.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.student.Student;
import com.student.StudentRepository;
import com.student.subject.Degree;
import com.student.subject.Subject;
import com.student.subject.SubjectRepository;

public abstract class StudentRootControllerTest extends AbstractControllerTest{
	
	@Autowired
	protected StudentRepository studentRepository;
	
	@Autowired
	protected SubjectRepository subjectRepository;
	
	protected long firstStudentId;
	protected long secondStudentId;
	protected long notExistingStudentId = -1;
	
	public void setUp() {
		super.setUp();
		cleanUpDatabase();
		insertSampleData();
		
		List<Student> students = studentRepository.findAll();
		firstStudentId = students.get(0).getId();
		secondStudentId = students.get(1).getId();
	}
	
	// localhost:8080/students
	protected String getStudentCollectionLink() {
		return getHost() + "/students";
	}

	// localhost:8080/students/{studentId}
	protected String getConcreteStudentLink(long studentId) {
		return getStudentCollectionLink() + "/" + studentId;
	}

	// localhost:8080/students/{studentId}/subjects
	protected String getSubjectCollectionLinkForChosenStudent(long studentId) {
		return getConcreteStudentLink(studentId) + "/subjects";
	}
	
	// localhost:8080/students/{studentId}/subjects/{subjectName}
	protected String getConcreteSubjectLinkForChosenStudent(Long studentId, String subjectName) {
		return getSubjectCollectionLinkForChosenStudent(studentId) + "/" + subjectName;
	}
	
	private void cleanUpDatabase() {
		studentRepository.deleteAll();
		subjectRepository.deleteAll();
	}
	
	private void insertSampleData() {
		Subject subject = new Subject("Math",new ArrayList<>(Arrays.asList(Degree.THREE, Degree.THREE_AND_HALF, Degree.THREE_AND_HALF)));
		Subject subject2 = new Subject("English",new ArrayList<>(Arrays.asList(Degree.FOUR, Degree.FOUR_AND_HALF, Degree.FIVE)));
		Subject subject3 = new Subject("English",new ArrayList<>(Arrays.asList(Degree.TWO, Degree.THREE, Degree.THREE)));
		
		Student student = new Student("Jan", "Nowak");
		Student student2 = new Student("Marcin", "Kowalski");
		
		student.addSubjects(subject);
		student.addSubjects(subject2);
		student2.addSubjects(subject3);
		
		studentRepository.save(student);
		studentRepository.save(student2);
	}
}
