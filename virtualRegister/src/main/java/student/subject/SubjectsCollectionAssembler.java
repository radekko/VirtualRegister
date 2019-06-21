package student.subject;

import java.util.ArrayList;
import java.util.Collection;
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

	private final SubjectLinkFactory subjectLinkFactory;

	public SubjectsCollectionAssembler(SubjectLinkFactory subjectLinkFactory) {
		this.subjectLinkFactory = subjectLinkFactory;
	}

	public Resources<ResourceSupport> subjectsCollectionToResource(Set<Subject> subjectsSet) {
		return new Resources<ResourceSupport>(createSubjectsResource(subjectsSet),getLinksToCollection(subjectsSet));
	}
	
	private List<ResourceSupport> createSubjectsResource(Set<Subject> subjects){
		Set<Subject> sortedSubjects = new TreeSet<>(subjects);
		return sortedSubjects.stream().map(this::collectionElementToResource).collect(Collectors.toList());
	}
	
	private ResourceSupport collectionElementToResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		subjectResource.add(subjectLinkFactory.getEmbeddedLinks(subject));
		return subjectResource;
	}
	
	private List<Link> getLinksToCollection(Collection<Subject> subjects){
		List<Link> links = new ArrayList<>();
		if(!subjects.isEmpty())
			links.add(subjectLinkFactory.getCollectionLink(subjects.iterator().next().getStudent()));
		return links;
	}
}