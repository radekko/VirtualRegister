package student.subject;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import entities.Subject;

@Component
public class SubjectsAssembler {
	
	private final SubjectLinkFactory subjectLinkFactory;
	
	public SubjectsAssembler(SubjectLinkFactory subjectLinkFactory) {
		this.subjectLinkFactory = subjectLinkFactory;
	}

	public ResourceSupport toResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		if(isSubjectIsAssignedToStudent(subject)) 
			subjectResource.add(subjectLinkFactory.getLinks(subject));
			
		return subjectResource;
	}
	
	public ResourceSupport toEmbeddedResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		if(isSubjectIsAssignedToStudent(subject))
			subjectResource.add(subjectLinkFactory.getEmbeddedLinks(subject));
		
		return subjectResource;
	}
	
	private boolean isSubjectIsAssignedToStudent(Subject subject) {
		return subject.getStudent() != null;
	}
}