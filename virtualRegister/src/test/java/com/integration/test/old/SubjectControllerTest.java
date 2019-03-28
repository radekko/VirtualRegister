package com.integration.test.old;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.core.Application;
import com.person.Person;
import com.person.subject.Subject;
import com.person.subject.SubjectRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SubjectControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private SubjectRepository subjectRepository;
	
	@Test
	public void testGetSubjectsForPerson() throws Exception {
		List<Subject> list = Arrays.asList(prepareExampeSubject());
		
		given(subjectRepository.findByPersonId(Mockito.any(Long.class))).willReturn(list);
		
		mvc.perform(get("/persons/{personId}/subjects",new Long(1)).accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$._embedded.subjectResources[0].subjectName").value("Math"))
			.andExpect(jsonPath("$._embedded.subjectResources[0].degree").isArray())
			.andExpect(jsonPath("$._embedded.subjectResources[0].degree[0]").value(1.0))
			.andExpect(jsonPath("$._embedded.subjectResources[0].degree[1]").value(2.0))
			.andExpect(jsonPath("$._embedded.subjectResources[0].degree[2]").value(3.0))
			.andExpect(jsonPath("$._embedded.subjectResources[0]._links.person.href").value("http://localhost/persons/1"))
			.andExpect(jsonPath("$._embedded.subjectResources[0]._links.self.href").value("http://localhost/persons/1/subjects/Math"))
			.andReturn();
	}

	@Test
	public void testGetSubjectForPersonBySubjectName() throws Exception {
		Optional<Subject> sub = Optional.of(prepareExampeSubject());
		
		given(subjectRepository.findByPersonIdAndSubjectName(Mockito.any(Long.class),Mockito.any(String.class))).willReturn(sub);
		
		mvc.perform(get("/persons/{personId}/subjects/{subjectName}",new Long(1),"Math").accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.subjectName").value("Math"))
			.andExpect(jsonPath("$.degree").isArray())
			.andExpect(jsonPath("$.degree[0]").value(1.0))
			.andExpect(jsonPath("$.degree[1]").value(2.0))
			.andExpect(jsonPath("$.degree[2]").value(3.0))
			.andExpect(jsonPath("$._links.person.href").value("http://localhost/persons/1"))
			.andExpect(jsonPath("$._links.self.href").value("http://localhost/persons/1/subjects/Math"))
			.andReturn();
	}
	
	private Subject prepareExampeSubject() {
		Person p = new Person(1,"Jan", "Nowak");
		Subject s = new Subject(new Long(1),"Math", Arrays.asList(1.0,2.0,3.0));
		s.setPerson(p);
		return s;
	}
}
