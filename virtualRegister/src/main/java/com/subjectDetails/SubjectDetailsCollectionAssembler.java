package com.subjectDetails;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class SubjectDetailsCollectionAssembler {
	private final SubjectDetailsAssembler subjectDetailsAssembler;

	public SubjectDetailsCollectionAssembler(SubjectDetailsAssembler subjectDetailsAssembler) {
		this.subjectDetailsAssembler = subjectDetailsAssembler;
	}
	
	public Resources<ResourceSupport> listToResource(List<SubjectDetails> subjectDetails) {
		List<ResourceSupport> studentResources = subjectDetails.stream().sorted().map(subjectDetailsAssembler::toEmbeddedResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(studentResources, SubjectDetailsLinkProvider.linkToSubjectDetailsCollection());
	}
}
