package student.subject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class NewSubjectDetails {
	
	@NotBlank
	private String subjectName;
	@Min(value = 0)
	private int ects;
	
	public NewSubjectDetails() {}

	public NewSubjectDetails(String subjectName, int ects) {
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