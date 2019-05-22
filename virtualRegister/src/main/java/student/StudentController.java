package student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

import entities.Student;
import entities.Subject;
import entities.SubjectDetails;
import exceptions.EntityAlreadyExistException;
import exceptions.EntityNotExistException;
import subjectDetails.SubjectDetailsRepository;

@RestController
@Transactional
@RequestMapping(value = "/students")
public class StudentController {
	
	private final StudentsCollectionAssembler studentsCollectionAssembler;
	private final StudentsAssembler studentsAssembler;
	private final StudentRepository studentRepository;
	private final SubjectDetailsRepository subjectDetailsRepository;

	public StudentController(StudentsCollectionAssembler studentsCollectionAssembler,
			StudentsAssembler studentsAssembler, StudentRepository studentRepository,
			SubjectDetailsRepository subjectDetailsRepository) {
		this.studentsCollectionAssembler = studentsCollectionAssembler;
		this.studentsAssembler = studentsAssembler;
		this.studentRepository = studentRepository;
		this.subjectDetailsRepository = subjectDetailsRepository;
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
	public HttpEntity<Void> createNewStudent(@Valid @RequestBody NewStudent newStudent) throws URISyntaxException {
		Student p = studentRepository.save(new Student(newStudent.getFirstName(),newStudent.getLastName()));
		ResourceSupport res = studentsAssembler.toResource(p);
		return ResponseEntity.created(new URI(res.getId().expand().getHref())).build();
	}
	
	@PutMapping(value="/{studentId}/subjects")
	public HttpEntity<Void> addSubjectToStudent(
			@PathVariable Long studentId, @NotBlank @RequestBody String subjectName)
					throws EntityNotExistException, EntityAlreadyExistException{

		Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotExistException(studentId));

		if(checkIfSubjectDetailsHasAlreadyAddedToPerson(subjectName, student))
			throw new EntityAlreadyExistException(subjectName);
		
		SubjectDetails sd = subjectDetailsRepository
						   .findBySubjectName(subjectName)
						   .orElseThrow(() -> new EntityNotExistException(subjectName));
		
		student.addSubjects(new Subject(sd,student));
		return ResponseEntity.noContent().build();
	}

	private boolean checkIfSubjectDetailsHasAlreadyAddedToPerson(String subjectName, Student student) {
		return student.getSubjects().stream().map(Subject::getSubjectDetails).anyMatch(sd -> sd.getSubjectName().equals(subjectName));
	}
	
	@PutMapping(value="/{studentId}")
	public HttpEntity<Void> renameStudent(@Valid @RequestBody NewStudent newStudent, @PathVariable Long studentId) throws EntityNotExistException {
		studentRepository.findById(studentId)
						 .map(changeStudent(newStudent))
						 .orElseThrow(() -> new EntityNotExistException(studentId));
		
		return ResponseEntity.noContent().build();
	}

	private Function<Student, Student> changeStudent(NewStudent newStudent) {
		return p -> changeStudentDetails(newStudent,p);
	}
	
	private Student changeStudentDetails(NewStudent newStudent, Student p) {
		p.setFirstName(newStudent.getFirstName());
		p.setLastName(newStudent.getLastName());
		return p;
	}
}