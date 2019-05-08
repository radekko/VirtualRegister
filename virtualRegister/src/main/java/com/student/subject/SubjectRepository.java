package com.student.subject;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{
	@Query("SELECT s FROM Subject s WHERE s.student.id = :studentId AND s.subjectName = :subjectName")
	Optional<Subject> findByStudentIdAndSubjectName(@Param("studentId") Long studentId, @Param("subjectName") String subjectName);

	Set<Subject> findByStudentId(Long studentId);
}
