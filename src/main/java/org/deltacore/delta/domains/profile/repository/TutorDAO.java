package org.deltacore.delta.domains.profile.repository;

import org.deltacore.delta.domains.profile.model.Tutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TutorDAO extends CrudRepository<Tutor,Long> {

    @Query(value = "SELECT t.* " +
            "FROM monitor t " +
            "JOIN user_monitor u ON t.user_monitor_id = u.id " +
            "WHERE u.username = :username ",
            nativeQuery = true)
    Optional<Tutor> findByUserUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM monitor WHERE is_active = :condition", nativeQuery = true)
    List<Tutor> findAllActiveOrInactiveTutors(@Param("condition") Boolean condition);
}
