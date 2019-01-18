package com.person.subject;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class SubjectResource extends ResourceSupport{

	private Long id;
	private String subjectName;
	private List<Double> degree;
	
	public SubjectResource(Long id, String subjectName, List<Double> degree) {
		this.id = id;
		this.subjectName = subjectName;
		this.degree = degree;
	}
	
	public SubjectResource(Subject subject) {
		this.id = subject.getId();
		this.subjectName = subject.getSubjectName();
		this.degree = subject.getDegree();
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<Double> getDegree() {
		return degree;
	}

	public void setDegree(List<Double> degree) {
		this.degree = degree;
	}

	public Long getIdent() {
		return id;
	}

	public void setIdent(Long id) {
		this.id = id;
	}
	
}
