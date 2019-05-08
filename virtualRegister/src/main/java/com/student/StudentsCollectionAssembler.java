package com.student;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class StudentsCollectionAssembler {

	private final EmbeddedStudentAssambler embeddedStudentAssambler;

	public StudentsCollectionAssembler() {
		this.embeddedStudentAssambler = new EmbeddedStudentAssambler();
	}

	public Resources<ResourceSupport> listToResource(List<Student> students) {
		List<ResourceSupport> studentResources = students.stream().map(embeddedStudentAssambler::toResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(studentResources, StudentLinkProvider.linkToStudentCollection());
	}

	private class EmbeddedStudentAssambler {

		public ResourceSupport toResource(Student student) {
			ResourceSupport studentResource = new StudentResource(student);
			studentResource.add(StudentLinkProvider.linkToStudent(student));
			studentResource.add(StudentLinkProvider.linkToSubjectsCollection(student));
			return studentResource;
		}
	}
}