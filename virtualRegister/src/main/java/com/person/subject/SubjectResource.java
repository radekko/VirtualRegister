package com.person.subject;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class SubjectResource extends ResourceSupport{

	private String subjectName;
	private List<Degree> degree;
	
	public SubjectResource(Subject subject) {
		this.subjectName = subject.getSubjectName();
		this.degree = subject.getDegree();
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<Degree> getDegree() {
		return degree;
	}

	public void setDegree(List<Degree> degree) {
		this.degree = degree;
	}

}
