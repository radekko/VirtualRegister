package com.subjectDetails;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value="subject_details", collectionRelation="subjects_details")
public class SubjectDetailsResource extends ResourceSupport{

	private String subjectName;
	private int ects;
	
	public SubjectDetailsResource() {}
	public SubjectDetailsResource(SubjectDetails subjectDetails) {
		this.subjectName = subjectDetails.getSubjectName();
		this.ects = subjectDetails.getEcts();
	}

	public SubjectDetailsResource(String subjectName, int ects) {
		this.subjectName = subjectName;
		this.ects = ects;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public int getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}
}