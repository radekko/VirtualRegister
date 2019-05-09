package com.subjectDetails;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.student.subject.Subject;

@Entity
public class SubjectDetails implements Comparable<SubjectDetails>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    @OneToMany(mappedBy = "subjectDetails", cascade = CascadeType.ALL)
	private Set<Subject> subject;
    
    private int ects;
    
	public SubjectDetails() {}
	
	@Column(unique = true)
	private String subjectName;

	public SubjectDetails(String subjectName,int ects) {
		this.subjectName = subjectName;
		this.ects = ects;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public Set<Subject> getSubject() {
		return subject;
	}

	public int getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}

	@Override
	public int compareTo(SubjectDetails s) {
		return this.subjectName.compareTo(s.subjectName);
	}
}