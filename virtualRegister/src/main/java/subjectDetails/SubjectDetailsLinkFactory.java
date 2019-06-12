package subjectDetails;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import entities.SubjectDetails;

@Component
public class SubjectDetailsLinkFactory {
	public Link getCollectionLink() {
		return SubjectDetailsLinkProvider.linkToSubjectDetailsCollection();
	}
	
	public List<Link> getLinks(SubjectDetails subjectDetails){
		List<Link> links = new ArrayList<>();
		links.add(SubjectDetailsLinkProvider.linkToSubjectDetails(subjectDetails.getSubjectName()));
		links.add(SubjectDetailsLinkProvider.linkToSubjectDetailsCollection());
		return links;
	}
	
	public List<Link> getEmbeddedLinks(SubjectDetails subjectDetails){
		List<Link> links = new ArrayList<>();
		links.add(SubjectDetailsLinkProvider.linkToSubjectDetails(subjectDetails.getSubjectName()));
		return links;
	}
}