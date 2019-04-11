package com.person.subject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class SubjectsAssembler {
	
	private final SubjectLinkProvider subjectRelProvider;
	
	public SubjectsAssembler(SubjectLinkProvider subjectRelProvider) {
		this.subjectRelProvider = subjectRelProvider;
	}

	public Resources<ResourceSupport> entityListToResource(Set<Subject> set) {
		List<ResourceSupport> subjectResourcesList = set.stream().map(this::embeddedToResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(subjectResourcesList,linkToCollection(set.iterator().next()));
	}
	
	public ResourceSupport singleEntityToResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		if(ifSubjectIsAssignedToPerson(subject)) 
			subjectResource.add(subjectRelProvider.getLinksForChosenSubject(subject));
			
		return subjectResource;
	}
	
	private boolean ifSubjectIsAssignedToPerson(Subject subject) {
		return subject.getPerson() != null;
	}
	
	private ResourceSupport embeddedToResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		subjectResource.add(subjectRelProvider.getLinksForEmbeddedSubject(subject));
		return subjectResource;
	}

	private List<Link> linkToCollection(Subject subject) {
		return subjectRelProvider.getLinksForCollection(subject);
	}
}
