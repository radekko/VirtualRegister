package com.student.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import com.student.StudentLinkProvider;

@Component
public class SubjectsCollectionAssembler {

	private final EmbeddedSubjectAssambler embeddedSubjectAssambler;

	public SubjectsCollectionAssembler() {
		this.embeddedSubjectAssambler = new EmbeddedSubjectAssambler();
	}

	public Resources<ResourceSupport> toResource(Set<Subject> set) {
		Set<Subject> sortedSubjects = new TreeSet<>(set);
		List<ResourceSupport> subjectResourcesList = 
				sortedSubjects.stream().map(embeddedSubjectAssambler::toResource).collect(Collectors.toList());
		
		return new Resources<ResourceSupport>(subjectResourcesList,getLinks(set));
	}
	
	private List<Link> getLinks(Set<Subject> subjects){
		List<Link> links = new ArrayList<>();
		if(!subjects.isEmpty())
			links.add(StudentLinkProvider.linkToSubjectsCollection(subjects.iterator().next().getStudent()));
		return links;
	}
	
	private class EmbeddedSubjectAssambler {
		
		public ResourceSupport toResource(Subject subject) {
			ResourceSupport subjectResource = new SubjectResource(subject);
			if(isSubjectAssignedToStudent(subject))
				subjectResource.add(SubjectLinkProvider.linkToSubject(subject));
			return subjectResource;
		}

		private boolean isSubjectAssignedToStudent(Subject subject) {
			return subject.getStudent() != null;
		}
	}
}