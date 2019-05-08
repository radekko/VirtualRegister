package com.student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.EntityNotExistException;
import com.student.subject.NewSubject;
import com.student.subject.Subject;

@RestController
@Transactional
@RequestMapping(value = "/students")
public class StudentController {
	
	private final StudentsCollectionAssembler studentsCollectionAssembler;
	private final StudentsAssembler studentsAssembler;
	private final StudentRepository studentRepository;

	public StudentController(StudentsCollectionAssembler studentsCollectionAssembler, StudentsAssembler studentsAssembler,
			StudentRepository studentRepository) {
		this.studentsCollectionAssembler = studentsCollectionAssembler;
		this.studentsAssembler = studentsAssembler;
		this.studentRepository = studentRepository;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<ResourceSupport>> getAllStudents() {
		Resources<ResourceSupport> resources = studentsCollectionAssembler.listToResource(studentRepository.findAll());
		return ResponseEntity.ok().body(resources);
	}
	
	@GetMapping(value="/{studentId}")
	public HttpEntity<ResourceSupport> getStudent(@PathVariable Long studentId) throws EntityNotExistException {
		ResourceSupport pr = studentRepository.findById(studentId)
				.map(studentsAssembler::toResource)
				.orElseThrow(() -> new EntityNotExistException(studentId));
		
		return ResponseEntity.ok().body(pr);
	}
	
	@PostMapping
	public HttpEntity<Void> createNewStudent(@RequestBody NewStudent newStudent) throws URISyntaxException {
		Student p = studentRepository.save(new Student(newStudent.getFirstName(),newStudent.getLastName()));
		ResourceSupport res = studentsAssembler.toResource(p);
		return ResponseEntity.created(new URI(res.getId().expand().getHref())).build();
	}
	
	@PutMapping(value="/{studentId}/subjects")
	public HttpEntity<Void> addSubjectToStudent(
			@PathVariable Long studentId, @RequestBody NewSubject newSubject) throws EntityNotExistException{
		
		Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotExistException(studentId));
		if(checkIfSubjectNotExist(student, newSubject)) {
			addNewSubjectToStudent(student, newSubject);
			studentRepository.save(student);
		}

		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value="/{studentId}")
	public HttpEntity<Void> renameStudent(@RequestBody NewStudent newStudent, @PathVariable Long studentId) throws EntityNotExistException {
		studentRepository.findById(studentId)
						.map(
								changeStudent(newStudent)
								.andThen(updateChangedStudentInDatabase())
						)
						.orElseThrow(() -> new EntityNotExistException(studentId));
		
		return ResponseEntity.noContent().build();
	}
	
	private boolean checkIfSubjectNotExist(Student p, NewSubject newSubject) {
		return p.getSubjects().stream().noneMatch(s -> s.getSubjectName().equals(newSubject.getSubjectName()));
	}

	private void addNewSubjectToStudent(Student p, NewSubject newSubject) {
		p.addSubjects(new Subject(newSubject.getSubjectName(),p));
	}

	private Function<Student, Student> changeStudent(NewStudent newStudent) {
		return p -> changeStudentDetails(newStudent,p);
	}
	
	private Student changeStudentDetails(NewStudent newStudent, Student p) {
		p.setFirstName(newStudent.getFirstName());
		p.setLastName(newStudent.getLastName());
		return p;
	}
	
	private Function<Student, Student> updateChangedStudentInDatabase() {
		return studentRepository :: save;
	}
	
}