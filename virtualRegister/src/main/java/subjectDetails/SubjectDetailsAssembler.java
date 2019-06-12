package subjectDetails;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import entities.SubjectDetails;

@Component
public class SubjectDetailsAssembler {
	
	private final SubjectDetailsLinkFactory subjectDetailsLinkFactory;
	
	public SubjectDetailsAssembler(SubjectDetailsLinkFactory subjectDetailsLinkFactory) {
		this.subjectDetailsLinkFactory = subjectDetailsLinkFactory;
	}

	public ResourceSupport toResource(SubjectDetails subjectDetails) {
		ResourceSupport subjectDetailsResource = new SubjectDetailsResource(subjectDetails);
		subjectDetailsResource.add(subjectDetailsLinkFactory.getLinks(subjectDetails));
		return subjectDetailsResource;
	}
	
	public ResourceSupport toEmbeddedResource(SubjectDetails subjectDetails) {
		ResourceSupport subjectDetailsResource = new SubjectDetailsResource(subjectDetails);  
		subjectDetailsResource.add(subjectDetailsLinkFactory.getEmbeddedLinks(subjectDetails));
		return subjectDetailsResource;
	}
}