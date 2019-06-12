package student;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.Student;

@Component
public class StudentsCollectionAssembler {

	private final StudentsAssembler studentsAssembler;
	private final StudentLinkFactory linkFactory;

	public StudentsCollectionAssembler(StudentsAssembler studentsAssembler, StudentLinkFactory linkFactory) {
		this.studentsAssembler = studentsAssembler;
		this.linkFactory = linkFactory;
	}

	public Resources<ResourceSupport> listToResource(List<Student> students) {
		List<ResourceSupport> studentResources = students.stream().map(studentsAssembler::toEmbeddedResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(studentResources, linkFactory.getCollectionLink());
	}
}