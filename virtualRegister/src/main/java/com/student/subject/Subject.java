package com.student.subject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.student.Student;

@Entity
public class Subject implements Comparable<Subject>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String subjectName;
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<Degree> degrees = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Student student;
	
	public Subject() {}
	
	public Subject(String subjectName, List<Degree> degree) {
		this.subjectName = subjectName;
		this.degrees = degree;
	}
	
	public Subject(String subjectName, Student student) {
		this.subjectName = subjectName;
	}
	
	public Long getId() {
		return id;
	}

	public String getSubjectName() {
		return subjectName;
	}
	
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public List<Degree> getDegree() {
		return degrees;
	}
	
	public void setDegree(List<Degree> degree) {
		this.degrees = degree;
	}
	
	public void addDegree(Degree degree) {
		degrees.add(degree);
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Override
	public int compareTo(Subject o) {
		return this.subjectName.compareTo(o.subjectName);
	}
}
