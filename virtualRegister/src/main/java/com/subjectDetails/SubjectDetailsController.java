package com.subjectDetails;

import java.net.URI;
import java.net.URISyntaxException;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.EntityNotExistException;
import com.student.subject.NewSubjectDetails;

@RestController
@Transactional
@RequestMapping(value = "/subject_details")
public class SubjectDetailsController {
	
	private final SubjectDetailsRepository subjectDetailsRepository;
	private final SubjectDetailsCollectionAssembler subjectDetailsCollectionAssembler;
	private final SubjectDetailsAssembler subjectDetailsAssembler;

	public SubjectDetailsController(SubjectDetailsRepository subjectDetailsRepository,
			SubjectDetailsCollectionAssembler subjectDetailsCollectionAssembler,
			SubjectDetailsAssembler subjectDetailsAssembler) {
		this.subjectDetailsRepository = subjectDetailsRepository;
		this.subjectDetailsCollectionAssembler = subjectDetailsCollectionAssembler;
		this.subjectDetailsAssembler = subjectDetailsAssembler;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<ResourceSupport>> getAllSubjectsDetails() {
		Resources<ResourceSupport> resources = subjectDetailsCollectionAssembler.listToResource(subjectDetailsRepository.findAll());
		return ResponseEntity.ok().body(resources);
	}
	
	@GetMapping(value="/{subjectName}")
	public HttpEntity<ResourceSupport> getSubjectDetails(@PathVariable String subjectName)
			throws EntityNotExistException {
		
		ResourceSupport subjectDetailsResource = subjectDetailsRepository
												 .findBySubjectName(subjectName)
												 .map(subjectDetailsAssembler::toResource)
												 .orElseThrow(() -> new EntityNotExistException(subjectName));

		return ResponseEntity.ok().body(subjectDetailsResource);
	}
	
	@PostMapping
	public HttpEntity<Void> addSubjectDetails(@Valid @RequestBody NewSubjectDetails newSubjectDetails) throws URISyntaxException {
		SubjectDetails s = subjectDetailsRepository.save(new SubjectDetails(newSubjectDetails.getSubjectName(),newSubjectDetails.getEcts()));
		ResourceSupport res = subjectDetailsAssembler.toResource(s);
		return ResponseEntity.created(new URI(res.getId().expand().getHref())).build();
	}}