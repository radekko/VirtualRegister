package student;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.Student;
import student.subject.SubjectsCollectionAssembler;

@Component
public class StudentsAssembler {
	private final SubjectsCollectionAssembler subjectsCollectionAssembler;
	private final StudentLinkFactory studentLinkFactory;

	public StudentsAssembler(SubjectsCollectionAssembler subjectsCollectionAssembler,
			StudentLinkFactory studentLinkFactory) {
		this.subjectsCollectionAssembler = subjectsCollectionAssembler;
		this.studentLinkFactory = studentLinkFactory;
	}

	public ResourceSupport toResource(Student student) {
		StudentResource studentResource = new StudentResource(student);
		studentResource.add(studentLinkFactory.getLinks(student));
		studentResource.embedResource("studentSubjects", createEmbeddedSubjectResource(student));
		return studentResource;
	}
	
	private Resources<ResourceSupport> createEmbeddedSubjectResource(Student student) {
		return subjectsCollectionAssembler.subjectsCollectionToResource(student.getSubjects());
	}
}