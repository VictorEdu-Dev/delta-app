package org.deltacore.delta.domains.tutoring.repository;

import org.deltacore.delta.domains.tutoring.model.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectDAO extends CrudRepository<Subject, Long> {
    @Query(value = "SELECT * FROM subject WHERE code = :code", nativeQuery = true)
    Optional<Subject> findByCode(@Param("code") String code);

    @Query(value = "SELECT * FROM subject WHERE is_active = :condition", nativeQuery = true)
    List<Subject> findByIsActive(@Param("condition") Boolean condition);

    @Query("SELECT s FROM Subject s LEFT JOIN FETCH s.tutoring")
    List<Subject> findAllSubjects();
}
