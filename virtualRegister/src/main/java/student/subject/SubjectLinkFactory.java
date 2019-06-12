package student.subject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import entities.Student;
import entities.Subject;
import student.StudentLinkProvider;

@Component
public class SubjectLinkFactory {
	public Link getCollectionLink(Student student) {
		return StudentLinkProvider.linkToSubjectsCollection(student);
	}
	
	public List<Link> getLinks(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(SubjectLinkProvider.linkToSubject(subject));    	                     // students/1/subjects/English - self
		links.add(SubjectLinkProvider.linkToParentStudent(subject));					 // students/1 - student
		links.add(StudentLinkProvider.linkToSubjectsCollection(subject.getStudent()));   // students/1/subjects - subjectsForStudent
		return links;
	}
	
	public List<Link> getEmbeddedLinks(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(SubjectLinkProvider.linkToSubject(subject));
		return links;
	}
}
