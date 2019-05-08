package com.student.subject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import com.student.StudentLinkProvider;

@Component
public class SubjectsAssembler {
	
	public ResourceSupport toResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		if(ifSubjectIsAssignedToStudent(subject)) 
			subjectResource.add(getLinks(subject));
			
		return subjectResource;
	}
	
	private List<Link> getLinks(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(SubjectLinkProvider.linkToSubject(subject));    	                   // students/1/subjects/English - self
		links.add(SubjectLinkProvider.linkToParentStudent(subject));					   // students/1 - student
		links.add(StudentLinkProvider.linkToSubjectsCollection(subject.getStudent()));   // students/1/subjects - subjectsForStudent
		return links;
	}
	
	private boolean ifSubjectIsAssignedToStudent(Subject subject) {
		return subject.getStudent() != null;
	}
	
}