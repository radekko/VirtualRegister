package com.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import com.student.subject.SubjectsCollectionAssembler;

@Component
public class StudentsAssembler {
	private final SubjectsCollectionAssembler subjectsCollectionAssembler;

	public StudentsAssembler(SubjectsCollectionAssembler subjectsCollectionAssembler) {
		this.subjectsCollectionAssembler = subjectsCollectionAssembler;
	}

	public ResourceSupport toResource(Student student) {
		StudentResource studentResource = new StudentResource(student);
		studentResource.add(getLinks(student));
		
		Resources<ResourceSupport> subjectResource = subjectsCollectionAssembler.toResource(student.getSubjects());
		studentResource.embedResource("studentSubjects", subjectResource);

		return studentResource;
	}
	
	private List<Link> getLinks(Student student){
		List<Link> links = new ArrayList<>();
		links.add(StudentLinkProvider.linkToStudent(student));
		links.add(StudentLinkProvider.linkToStudentCollection());
		return links;
	}
}