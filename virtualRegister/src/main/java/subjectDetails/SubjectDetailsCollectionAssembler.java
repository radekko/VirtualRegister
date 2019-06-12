package subjectDetails;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.SubjectDetails;

@Component
public class SubjectDetailsCollectionAssembler {
	private final SubjectDetailsAssembler subjectDetailsAssembler;
	private final SubjectDetailsLinkFactory subjectDetailsLinkFactory;

	public SubjectDetailsCollectionAssembler(SubjectDetailsAssembler subjectDetailsAssembler,
			SubjectDetailsLinkFactory subjectDetailsLinkFactory) {
		this.subjectDetailsAssembler = subjectDetailsAssembler;
		this.subjectDetailsLinkFactory = subjectDetailsLinkFactory;
	}

	public Resources<ResourceSupport> listToResource(List<SubjectDetails> subjectDetails) {
		List<ResourceSupport> studentResources = subjectDetails.stream().sorted().map(subjectDetailsAssembler::toEmbeddedResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(studentResources, subjectDetailsLinkFactory.getCollectionLink());
	}
}