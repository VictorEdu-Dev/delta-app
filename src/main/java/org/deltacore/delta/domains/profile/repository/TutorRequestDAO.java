package org.deltacore.delta.domains.profile.repository;

import org.deltacore.delta.domains.profile.model.TutorRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TutorRequestDAO extends CrudRepository<TutorRequest, Long> {
    @Query(value ="""
            SELECT 1
            FROM tutor_request
            WHERE user_id = :userId
            LIMIT 1
            """, nativeQuery = true)
    Integer verifyTutorRequest(@Param("userId") Long tutorRequest);
}
