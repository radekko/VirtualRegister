package com.subjectDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDetailsRepository extends JpaRepository<SubjectDetails, Long>{
	Optional<SubjectDetails> findBySubjectName(String name);
}
