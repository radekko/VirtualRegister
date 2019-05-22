package student;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.Student;
import student.subject.SubjectsCollectionAssembler;

@Component
public class StudentsAssembler {
	private final SubjectsCollectionAssembler subjectsCollectionAssembler;

	public StudentsAssembler(SubjectsCollectionAssembler subjectsCollectionAssembler) {
		this.subjectsCollectionAssembler = subjectsCollectionAssembler;
	}

	public ResourceSupport toResource(Student student) {
		StudentResource studentResource = new StudentResource(student);
		studentResource.add(getLinks(student).collect(Collectors.toList()));
		
		Resources<ResourceSupport> subjectResource = subjectsCollectionAssembler.toResource(student.getSubjects());
		studentResource.embedResource("studentSubjects", subjectResource);

		return studentResource;
	}
	
	public ResourceSupport toEmbeddedResource(Student student) {
		ResourceSupport studentResource = new StudentResource(student);
		studentResource.add(getEmbeddedLinks(student).collect(Collectors.toList()));
		return studentResource;
	}
	
	private Stream<Link> getLinks(Student student){
		return Stream.of(
				StudentLinkProvider.linkToStudent(student),
				StudentLinkProvider.linkToStudentCollection());
	}

	private Stream<Link> getEmbeddedLinks(Student student){
		return Stream.of(
				StudentLinkProvider.linkToStudent(student),
				StudentLinkProvider.linkToSubjectsCollection(student));
	}
}