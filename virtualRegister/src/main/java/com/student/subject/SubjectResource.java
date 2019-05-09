package com.student.subject;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value="subject", collectionRelation="subjects")
public class SubjectResource extends ResourceSupport{

	private String subjectName;
	private int ects;
	private List<Mark> marks;
	
	public SubjectResource(Subject subject) {
		this.subjectName = subject.getSubjectDetails().getSubjectName();
		this.ects = subject.getSubjectDetails().getEcts();
		this.marks = subject.getMark();
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<Mark> getMarks() {
		return marks;
	}

	public void setMarks(List<Mark> marks) {
		this.marks = marks;
	}

	public int getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}
}