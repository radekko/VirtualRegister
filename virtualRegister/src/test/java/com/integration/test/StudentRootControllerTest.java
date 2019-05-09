package com.integration.test;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.student.Student;
import com.student.StudentRepository;
import com.student.subject.Mark;
import com.student.subject.Subject;
import com.student.subject.SubjectRepository;
import com.subjectDetails.SubjectDetails;
import com.subjectDetails.SubjectDetailsRepository;

public abstract class StudentRootControllerTest extends AbstractControllerTest{
	
	@Autowired
	protected StudentRepository studentRepository;
	
	@Autowired
	protected SubjectRepository subjectRepository;

	@Autowired
	protected SubjectDetailsRepository subjectDetailsRepository;
	
	protected long firstStudentId;
	protected long secondStudentId;
	protected long notExistingStudentId = -1;
	protected String notExistingSubjectName = "Physics";
	protected String alreadyExistSubjectName = "Math";
	
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
		jdbcTemplate.execute("DELETE FROM subject_marks");
		jdbcTemplate.execute("DELETE FROM subject");
		jdbcTemplate.execute("DELETE FROM subject_details");
		jdbcTemplate.execute("DELETE FROM student");
	}
	
	private void insertSampleData() {
		SubjectDetails sd = new SubjectDetails("Math",5);
		SubjectDetails sd2 = new SubjectDetails("English",6);
		SubjectDetails sd3 = new SubjectDetails("Chemistry",10);
		
		Student student = new Student("Jan", "Nowak");
		Student student2 = new Student("Marcin", "Kowalski");

		Subject subject = new Subject(sd, Arrays.asList(Mark.THREE, Mark.THREE_AND_HALF, Mark.THREE_AND_HALF),student);
		Subject subject2 = new Subject(sd2, Arrays.asList(Mark.FOUR, Mark.FOUR_AND_HALF, Mark.FIVE),student);
		Subject subject3 = new Subject(sd2, Arrays.asList(Mark.TWO, Mark.THREE, Mark.THREE),student2);
		
		student.addSubjects(subject);
		student.addSubjects(subject2);
		student2.addSubjects(subject3);
		
		studentRepository.saveAll(Arrays.asList(student,student2));
		subjectDetailsRepository.save(sd3);
	}
}