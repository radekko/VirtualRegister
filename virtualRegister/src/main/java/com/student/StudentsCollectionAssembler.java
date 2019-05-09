package com.student;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class StudentsCollectionAssembler {

	private final StudentsAssembler studentsAssembler;

	public StudentsCollectionAssembler(StudentsAssembler studentsAssembler) {
		this.studentsAssembler = studentsAssembler;
	}

	public Resources<ResourceSupport> listToResource(List<Student> students) {
		List<ResourceSupport> studentResources = students.stream().map(studentsAssembler::toEmbeddedResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(studentResources, StudentLinkProvider.linkToStudentCollection());
	}
}