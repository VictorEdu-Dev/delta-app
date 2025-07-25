package org.deltacore.delta.domains.tutoring.repository;

import org.deltacore.delta.domains.tutoring.dto.SubjectDTO;
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

    @Query(value = """
            SELECT
                s.id AS id,
                s.code AS code,
                s.name AS name,
                s.is_active AS isActive
            FROM
                subject s
            WHERE
                s.is_active = true
            ORDER BY
                s.name
            """, nativeQuery = true)
    List<SubjectDTO.SubjectInfoDTO> findAvailableSubjects();
}
