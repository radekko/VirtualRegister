package subjectDetails;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.SubjectDetails;

@Component
public class SubjectDetailsCollectionAssembler {
	
	private final SubjectDetailsLinkFactory subjectDetailsLinkFactory;

	public SubjectDetailsCollectionAssembler(SubjectDetailsLinkFactory subjectDetailsLinkFactory) {
		this.subjectDetailsLinkFactory = subjectDetailsLinkFactory;
	}

	public Resources<ResourceSupport> subjectDetailsCollectionToResource(List<SubjectDetails> subjectDetails) {
		List<ResourceSupport> studentResources = subjectDetails.stream().sorted().map(this::collectionElementToResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(studentResources, subjectDetailsLinkFactory.getCollectionLink());
	}
	
	private ResourceSupport collectionElementToResource(SubjectDetails subjectDetails) {
		ResourceSupport subjectDetailsResource = new SubjectDetailsResource(subjectDetails);  
		subjectDetailsResource.add(subjectDetailsLinkFactory.getEmbeddedLinks(subjectDetails));
		return subjectDetailsResource;
	}
}