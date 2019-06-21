package student.subject;

import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entities.Mark;
import entities.Subject;
import exceptions.EntityNotExistException;
import exceptions.MarkFormatException;

@RestController
@Transactional
@RequestMapping(value = "/students/{studentId}/subjects")
public class SubjectController {
	
	private final SubjectsCollectionAssembler subjectsCollectionAssembler;
	private final SubjectsAssembler subjectsAssembler;
	private final SubjectRepository subjectRepository;
	
	public SubjectController(SubjectsCollectionAssembler subjectsCollectionAssembler,
			SubjectsAssembler subjectsAssembler, SubjectRepository subjectRepository) {
		this.subjectsCollectionAssembler = subjectsCollectionAssembler;
		this.subjectsAssembler = subjectsAssembler;
		this.subjectRepository = subjectRepository;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<ResourceSupport>> getSubjectsForStudent(@PathVariable Long studentId) throws EntityNotExistException {
		Resources<ResourceSupport> subjectResources = subjectsCollectionAssembler.subjectsCollectionToResource(subjectRepository.findByStudentId(studentId));
		return ResponseEntity.ok().body(subjectResources);
	}

	@GetMapping(value="/{subjectName}")
	public HttpEntity<ResourceSupport> getSubjectForStudentBySubjectName(
			@PathVariable Long studentId, @PathVariable String subjectName) throws EntityNotExistException {
		
		ResourceSupport subjectResource = subjectRepository.findByStudentIdAndSubjectName(studentId, subjectName)
											  .map(subjectsAssembler::toResource)
											  .orElseThrow(() -> new EntityNotExistException(studentId));

		return ResponseEntity.ok().body(subjectResource);
	}
	
	@PutMapping(value="/{subjectName}")
	public HttpEntity<Void> addMarkToSubjectForChoosenStudent(
			@PathVariable Long studentId, @PathVariable String subjectName, @RequestBody Float mark)
					throws EntityNotExistException, MarkFormatException {

		Mark markToAdd = Mark.getByValue(mark).orElseThrow(() -> new MarkFormatException(mark));
		
		subjectRepository.findByStudentIdAndSubjectName(studentId, subjectName)
						 .map(updateSubject(markToAdd))
						 .orElseThrow(() -> new EntityNotExistException(studentId));
		
		return ResponseEntity.noContent().build();
	}
	
	private Function<Subject,Subject> updateSubject(Mark mark) {
		return s -> addDegresToSubject(mark, s);
	}

	private Subject addDegresToSubject(Mark mark, Subject s) {
		s.addMark(mark);
		return s;
	}
}