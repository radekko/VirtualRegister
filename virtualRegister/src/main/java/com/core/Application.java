package com.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.person.Person;
import com.person.PersonRepository;
import com.person.subject.Subject;

@ComponentScan({"com.person","com.core"})
@EnableJpaRepositories("com.person")
@EntityScan("com.person")
@SpringBootApplication
public class Application 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
    
    @Bean
	public CommandLineRunner demo(PersonRepository repository) {
    	return (args) -> {
    		Subject subject = new Subject("Math",new ArrayList<>(Arrays.asList(3.0, 3.5, 3.5)));
    		Subject subject2 = new Subject("English",new ArrayList<>(Arrays.asList(4.0, 4.5, 5.0)));
    		Subject subject3 = new Subject("English",new ArrayList<>(Arrays.asList(2.0, 2.5, 3.0)));
    		
			Person person = new Person("Jan", "Nowak");
			Person person2 = new Person("Marcin", "Kowalski");
			
			person.addSubjects(subject);
			person.addSubjects(subject2);
			person2.addSubjects(subject3);
			
			repository.save(person);
			repository.save(person2);
		};
    }
}
