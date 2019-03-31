package com.person.subject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.person.Person;

@Entity
public class Subject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String subjectName;
	
	@ElementCollection
	private List<Double> degrees = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Person person;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public List<Double> getDegree() {
		return degrees;
	}
	public void setDegree(List<Double> degree) {
		this.degrees = degree;
	}
	public void addDegree(double degree) {
		degrees.add(degree);
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Subject(Long id, String subjectName, List<Double> degree) {
		this.id = id;
		this.subjectName = subjectName;
		this.degrees = degree;
	}
	public Subject(String subjectName, List<Double> degree) {
		this.subjectName = subjectName;
		this.degrees = degree;
	}
	public Subject(String subjectName, Person person) {
		this.subjectName = subjectName;
	}
	public Subject() {
	}
}
