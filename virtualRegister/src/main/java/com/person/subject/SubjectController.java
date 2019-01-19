package com.person.subject;

import java.util.function.Function;

import javax.transaction.Transactional;

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

import com.core.EntityNotExistException;

@RestController
@Transactional
@RequestMapping(value = "/persons/{personId}/subjects")
public class SubjectController {
	
	private final SubjectRepository subjectRepository;
	private final SubjectsResourceAssembler subjectResourceAssemb;
	
	public SubjectController(SubjectRepository subjectRepository, SubjectsResourceAssembler subjectResourceAssemb) {
		this.subjectRepository = subjectRepository;
		this.subjectResourceAssemb = subjectResourceAssemb;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<SubjectResource>> getSubjectForPerson(@PathVariable Long personId) throws EntityNotExistException {
		Resources<SubjectResource> resources = subjectResourceAssemb.toGlobalResource(subjectRepository.findByPersonId(personId));
		return ResponseEntity.ok().body(resources);
	}

	@GetMapping(value="/{subjectName}")
	public HttpEntity<SubjectResource> getSubjectForPersonBySubjectName(
			@PathVariable Long personId, @PathVariable String subjectName) throws EntityNotExistException {
		
		SubjectResource sr = subjectRepository.findByPersonIdAndSubjectName(personId, subjectName)
											  .map(subjectResourceAssemb::toResource)
											  .orElseThrow(() -> new EntityNotExistException(personId));

		return ResponseEntity.ok().body(sr);
	}
	
	@PutMapping(value="/{subjectName}")
	public HttpEntity<Void> addDegreeToSubjectForChoosenPerson(
			@PathVariable Long personId, @PathVariable String subjectName, @RequestBody Degree degree) throws EntityNotExistException {

		subjectRepository.findByPersonIdAndSubjectName(personId, subjectName)
						 .map(updateSubject(degree)
						 .andThen(storeSubject()))
						 .orElseThrow(() -> new EntityNotExistException(personId));
		
		return ResponseEntity.noContent().build();
	}
	
	private Function<Subject,Subject> updateSubject(Degree degree) {
		return s -> addDegresToSubject(degree, s);
	}

	private Subject addDegresToSubject(Degree degree, Subject s) {
		s.addDegree(degree.getDegree());
		return s;
	}
	
	private Function<Subject,Subject> storeSubject() {
		return subjectRepository :: saveAndFlush;
	}

}