package student.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import entities.Subject;
import exceptions.EntityNotExistException;
import student.StudentController;

@Component
public class SubjectLinkProvider {
	
	// students/{studentId}/subjects/{subjectName} - self
	public static Link linkToSubject(Subject subject) {
		long studentId = subject.getStudent().getId();
		String subjectName = subject.getSubjectDetails().getSubjectName();

		Link link;
		try {
			link = linkTo(methodOn(SubjectController.class).getSubjectForStudentBySubjectName(studentId,subjectName)).withSelfRel();
		} 
		catch (EntityNotExistException e) {throw new RuntimeException("Entity not exist");}
		
		return link;
	}
	
	// students/{studentId} - student
	public static Link linkToParentStudent(Subject subject) {
		long studentId = subject.getStudent().getId();

		Link link;
		try {link = linkTo(methodOn(StudentController.class).getStudent(studentId)).withRel("student");}
		catch (EntityNotExistException e) {throw new RuntimeException("Student not exist");}
		
		return link;
	}
}