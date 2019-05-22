package subjectDetails;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import entities.SubjectDetails;

@Component
public class SubjectDetailsAssembler {

	public ResourceSupport toResource(SubjectDetails subjectDetails) {
		ResourceSupport subjectDetailsResource = new SubjectDetailsResource(subjectDetails);  
		subjectDetailsResource.add(SubjectDetailsLinkProvider.linkToSubjectDetails(subjectDetails.getSubjectName()));
		subjectDetailsResource.add(SubjectDetailsLinkProvider.linkToSubjectDetailsCollection());
		return subjectDetailsResource;
	}
	
	public ResourceSupport toEmbeddedResource(SubjectDetails subjectDetails) {
		ResourceSupport subjectDetailsResource = new SubjectDetailsResource(subjectDetails);  
		subjectDetailsResource.add(SubjectDetailsLinkProvider.linkToSubjectDetails(subjectDetails.getSubjectName()));
		return subjectDetailsResource;
	}
}