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

import com.student.Student;
import com.student.StudentRepository;
import com.student.subject.Degree;
import com.student.subject.Subject;

@ComponentScan({"com.student","com.core","com.logging"})
@EnableJpaRepositories("com.student")
@EntityScan("com.student")
@SpringBootApplication
public class Application 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
    
    @Bean
	public CommandLineRunner demo(StudentRepository repository) {
    	return (args) -> {
    		Subject subject = new Subject("Math",new ArrayList<>(Arrays.asList(Degree.THREE, Degree.THREE_AND_HALF, Degree.THREE_AND_HALF)));
    		Subject subject2 = new Subject("English",new ArrayList<>(Arrays.asList(Degree.FOUR, Degree.FOUR_AND_HALF, Degree.FIVE)));
    		Subject subject3 = new Subject("English",new ArrayList<>(Arrays.asList(Degree.TWO, Degree.THREE, Degree.THREE)));
    		
			Student student = new Student("Jan", "Nowak");
			Student student2 = new Student("Marcin", "Kowalski");
			
			student.addSubjects(subject);
			student.addSubjects(subject2);
			student2.addSubjects(subject3);
			
			repository.save(student);
			repository.save(student2);
		};
    }
}