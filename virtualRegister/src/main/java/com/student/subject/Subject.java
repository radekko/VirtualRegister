package com.student.subject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.student.Student;
import com.subjectDetails.SubjectDetails;

@Entity
public class Subject implements Comparable<Subject>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subjectId;
	
	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "subjectDetails_id")
	private SubjectDetails subjectDetails;
	
	@ElementCollection
	@CollectionTable(joinColumns=@JoinColumn(name="subject_id"))
	@Enumerated(EnumType.STRING)
	private List<Mark> marks = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
	private Student student;
	
	public Subject() {}
	
	public Long getSubjectId() {
		return subjectId;
	}

	public Subject(SubjectDetails subjectDetails, List<Mark> mark, Student student) {
		this.subjectDetails = subjectDetails;
		this.marks = mark;
		this.student = student;
	}
	
	public Subject(SubjectDetails subjectDetails) {
		this.subjectDetails = subjectDetails;
	}

	public Subject(SubjectDetails subjectDetails, Student student) {
		this.subjectDetails = subjectDetails;
		this.student = student;
	}

	public List<Mark> getMark() {
		return marks;
	}
	
	public void setMark(List<Mark> mark) {
		this.marks = mark;
	}
	
	public void addMark(Mark mark) {
		marks.add(mark);
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public SubjectDetails getSubjectDetails() {
		return subjectDetails;
	}

	public void setSubjectDetails(SubjectDetails subjectDetails) {
		this.subjectDetails = subjectDetails;
	}

	@Override
	public int compareTo(Subject s) {
		return this.subjectDetails.compareTo(s.subjectDetails);
	}
}