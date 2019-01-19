package com.integration.test;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.person.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PersonRepository personRepository;
	
	@Test
	public void testGetAllPersons() throws Exception {
		List<Person> list = Arrays.asList(
				new Person(1,"Jan", "Nowak"),
				new Person(2,"Dariusz", "Kowalski"),
				new Person(3,"Mariusz", "Szary"));
		
		given(personRepository.findAll()).willReturn(list);

		mvc.perform(get("/persons").accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$._embedded.personResources[0].ident").value(1))
			.andExpect(jsonPath("$._embedded.personResources[0].firstName").value("Jan"))
			.andExpect(jsonPath("$._embedded.personResources[0].lastName").value("Nowak"))
			.andExpect(jsonPath("$._embedded.personResources[0]._links.self.href").value("http://localhost/persons/1"))
			.andExpect(jsonPath("$._embedded.personResources[0]._links.persons.href").value("http://localhost/persons"))
			.andExpect(jsonPath("$._embedded.personResources[1].firstName").value("Dariusz"))
			.andReturn();
	}
	
	@Test
	public void testGetPerson() throws Exception {
		Optional<Person> p = Optional.of(new Person(1,"Dariusz", "Kowalski"));
		given(personRepository.findById(Mockito.any(Long.class))).willReturn(p);

		mvc.perform(get("/persons/{id}",1).accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.ident").value(1))
			.andExpect(jsonPath("$.firstName").value("Dariusz"))
			.andExpect(jsonPath("$.lastName").value("Kowalski"))
			.andExpect(jsonPath("$._links.self.href").value("http://localhost/persons/1"))
			.andExpect(jsonPath("$._links.persons.href").value("http://localhost/persons"))
			.andReturn();
	}
	
	@Test
	public void testPostPerson() throws Exception {
		Person p = new Person(1,"Marek","Zegarek");
		given(personRepository.save(Mockito.any(Person.class))).willReturn(p);
		
		final String newAuthorRequestJson = "{ \"firstName\" : \"Marek\", \"lastName\" : \"Zegarek\"  }";
		mvc.perform(post("/persons").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(newAuthorRequestJson))
				.andDo(print())
				.andExpect(header().string("Location", "http://localhost/persons/1"))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void testPutPerson() throws Exception {
		Optional<Person> p = Optional.of(new Person(1,"Marek", "Zegarek"));
		given(personRepository.findById(Mockito.any(Long.class))).willReturn(p);
		given(personRepository.save(Mockito.any(Person.class))).willReturn(p.get());
		
		final String newAuthorRequestJson = "{ \"firstName\" : \"Marek\", \"lastName\" : \"Zegarek\"  }";
		mvc.perform(put("/persons/{id}",1).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(newAuthorRequestJson))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void testPutPersonThrowException() throws Exception {
		Optional<Person> p = Optional.empty();
		given(personRepository.findById(Mockito.any(Long.class))).willReturn(p);
		
		final String newAuthorRequestJson = "{ \"firstName\" : \"Marek\", \"lastName\" : \"Zegarek\"  }";
		mvc.perform(put("/persons/{id}",1).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(newAuthorRequestJson))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testPutAddSubjectToPerson() throws Exception {
		Optional<Person> p = Optional.of(new Person(1,"Marek", "Zegarek"));
		given(personRepository.findById(Mockito.any(Long.class))).willReturn(p);
		final String newSubjectRequestJson = "{ \"subjectName\" : \"math\"  }";

		mvc.perform(put("/persons/{id}/subjects",1).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(newSubjectRequestJson))
				.andExpect(status().isNoContent());
	}
}
