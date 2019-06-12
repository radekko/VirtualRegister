package student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import entities.Student;

@Component
public class StudentLinkFactory{

	public Link getCollectionLink() {
		return StudentLinkProvider.linkToStudentCollection();
	}
	
	public List<Link> getLinks(Student student) {
		List<Link> links = new ArrayList<>();
		links.add(StudentLinkProvider.linkToStudent(student));
		links.add(StudentLinkProvider.linkToStudentCollection());
		return links;
	}

	public List<Link> getEmbeddedLinks(Student student) {
		List<Link> links = new ArrayList<>();
		links.add(StudentLinkProvider.linkToStudent(student));
		links.add(StudentLinkProvider.linkToSubjectsCollection(student));
		return links;
	}
}