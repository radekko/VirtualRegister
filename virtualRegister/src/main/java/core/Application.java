package core;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import entities.Mark;
import entities.Student;
import entities.Subject;
import entities.SubjectDetails;
import student.StudentRepository;
import subjectDetails.SubjectDetailsRepository;

@ComponentScan({"logging","exceptions","student","subjectDetails"})
@EnableJpaRepositories({"student","subjectDetails"})
@EntityScan({"entities"})
@SpringBootApplication
public class Application 
{
	
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
    
    @Bean
    @Profile("!test")
	public CommandLineRunner demo(StudentRepository repository, SubjectDetailsRepository sdRepo) {
    	return (args) -> {
    		SubjectDetails sd = new SubjectDetails("Math",5);
    		SubjectDetails sd2 = new SubjectDetails("English",6);
    		
    		Student student = new Student("Jan", "Nowak");
    		Student student2 = new Student("Marcin", "Kowalski");

    		Subject subject = new Subject(sd, Arrays.asList(Mark.THREE, Mark.THREE_AND_HALF, Mark.THREE_AND_HALF),student);
    		Subject subject2 = new Subject(sd2, Arrays.asList(Mark.FOUR, Mark.FOUR_AND_HALF, Mark.FIVE),student);
    		Subject subject3 = new Subject(sd2, Arrays.asList(Mark.TWO, Mark.THREE, Mark.THREE),student2);
			
			student.addSubject(subject);
			student.addSubject(subject2);
			student2.addSubject(subject3);
			
			repository.saveAll(Arrays.asList(student,student2));
		};
    }
}