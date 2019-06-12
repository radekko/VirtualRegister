package student.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import entities.Subject;

@Component
public class SubjectsCollectionAssembler {

	private final SubjectsAssembler subjectsAssembler;
	private final SubjectLinkFactory subjectLinkFactory;

	public SubjectsCollectionAssembler(SubjectsAssembler subjectsAssembler, SubjectLinkFactory subjectLinkFactory) {
		this.subjectsAssembler = subjectsAssembler;
		this.subjectLinkFactory = subjectLinkFactory;
	}

	public Resources<ResourceSupport> toResource(Set<Subject> set) {
		Set<Subject> sortedSubjects = new TreeSet<>(set);
		List<ResourceSupport> subjectResourcesList = 
				sortedSubjects.stream().map(subjectsAssembler::toEmbeddedResource).collect(Collectors.toList());
		
		return new Resources<ResourceSupport>(subjectResourcesList,getLinks(set));
	}
	
	private List<Link> getLinks(Set<Subject> subjects){
		List<Link> links = new ArrayList<>();
		if(!subjects.isEmpty())
			links.add(subjectLinkFactory.getCollectionLink(subjects.iterator().next().getStudent()));
		return links;
	}
}