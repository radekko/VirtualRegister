package com.person.subject;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{
	@Query("SELECT s FROM Subject s WHERE s.person.id = :personId AND s.subjectName = :subjectName")
	Optional<Subject> findByPersonIdAndSubjectName(@Param("personId") Long personId, @Param("subjectName") String subjectName);

	Set<Subject> findByPersonId(Long personId);
}
