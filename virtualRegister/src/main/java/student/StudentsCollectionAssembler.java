package student;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.Student;

@Component
public class StudentsCollectionAssembler {

	private final StudentLinkFactory studentLinkFactory;

	public StudentsCollectionAssembler(StudentLinkFactory studentLinkFactory) {
		this.studentLinkFactory = studentLinkFactory;
	}

	public Resources<ResourceSupport> studentsCollectionToResource(List<Student> students) {
		return new Resources<ResourceSupport>(createStudentsResource(students), getLinksToCollection());
	}

	private List<ResourceSupport> createStudentsResource(List<Student> students){
		return students.stream().map(this::collectionElementToResource).collect(Collectors.toList());
	}
	
	private ResourceSupport collectionElementToResource(Student student) {
		ResourceSupport studentResource = new StudentResource(student);
		studentResource.add(studentLinkFactory.getEmbeddedLinks(student));
		return studentResource;
	}
	
	private Link getLinksToCollection() {
		return studentLinkFactory.getCollectionLink();
	}
}