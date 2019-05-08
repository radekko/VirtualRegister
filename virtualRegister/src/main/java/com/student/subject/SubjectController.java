package com.student.subject;

import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.DegreeFormatException;
import com.exceptions.EntityNotExistException;

@RestController
@Transactional
@RequestMapping(value = "/students/{studentId}/subjects")
public class SubjectController {
	
	private final SubjectsCollectionAssembler subjectsCollectionAssembler;
	private final SubjectsAssembler subjectsAssembler;
	private final SubjectRepository subjectRepository;
	
	public SubjectController(SubjectsCollectionAssembler subjectsCollectionAssembler,
			SubjectsAssembler subjectsAssembler, SubjectRepository subjectRepository) {
		this.subjectsCollectionAssembler = subjectsCollectionAssembler;
		this.subjectsAssembler = subjectsAssembler;
		this.subjectRepository = subjectRepository;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<ResourceSupport>> getSubjectsForStudent(@PathVariable Long studentId) throws EntityNotExistException {
		Resources<ResourceSupport> subjectResources = subjectsCollectionAssembler.toResource(subjectRepository.findByStudentId(studentId));
		return ResponseEntity.ok().body(subjectResources);
	}

	@GetMapping(value="/{subjectName}")
	public HttpEntity<ResourceSupport> getSubjectForStudentBySubjectName(
			@PathVariable Long studentId, @PathVariable String subjectName) throws EntityNotExistException {
		
		ResourceSupport subjectResource = subjectRepository.findByStudentIdAndSubjectName(studentId, subjectName)
											  .map(subjectsAssembler::toResource)
											  .orElseThrow(() -> new EntityNotExistException(studentId));

		return ResponseEntity.ok().body(subjectResource);
	}
	
	@PutMapping(value="/{subjectName}")
	public HttpEntity<Void> addDegreeToSubjectForChoosenStudent(
			@PathVariable Long studentId, @PathVariable String subjectName, @RequestBody Float degree)
					throws EntityNotExistException, DegreeFormatException {

		Degree degreeToAdd = Degree.getByValue(degree).orElseThrow(() -> new DegreeFormatException(degree));
		
		subjectRepository.findByStudentIdAndSubjectName(studentId, subjectName)
						 .map(updateSubject(degreeToAdd)
						 .andThen(storeSubject()))
						 .orElseThrow(() -> new EntityNotExistException(studentId));
		
		return ResponseEntity.noContent().build();
	}
	
	private Function<Subject,Subject> updateSubject(Degree degree) {
		return s -> addDegresToSubject(degree, s);
	}

	private Subject addDegresToSubject(Degree degree, Subject s) {
		s.addDegree(degree);
		return s;
	}
	
	private Function<Subject,Subject> storeSubject() {
		return subjectRepository :: saveAndFlush;
	}

}