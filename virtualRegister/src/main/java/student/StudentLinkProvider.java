package student;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import entities.Student;
import exceptions.EntityNotExistException;
import student.subject.SubjectController;

@Component
public class StudentLinkProvider {
	
	// /students
	public static Link linkToStudentCollection() {
		return linkTo(methodOn(StudentController.class).getAllStudents()).withRel("students");
	}
	
	// /students/{studentId}
	public static Link linkToStudent(Student student){
		Link link;
		try {link = linkTo(methodOn(StudentController.class).getStudent(student.getId())).withSelfRel();}
		catch (EntityNotExistException e) {throw new RuntimeException("Entity not exist");}
		return link;
	}
	
	// students/{studentId}/subjects - subjectsForStudent
	public static Link linkToSubjectsCollection(Student student) {
		Link link;
		try {
			link = linkTo(methodOn(SubjectController.class).getSubjectsForStudent(student.getId())).withRel("subjectsForStudent");
		} 
		catch (EntityNotExistException e) {throw new RuntimeException("Entity not exist");}
		
		return link;
	}
}