package org.deltacore.delta.domains.tutoring.repository;

import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TutoringDAO extends CrudRepository<Tutoring, Long> {

    @EntityGraph(attributePaths = {"monitor", "users", "daysOfWeek"})
    Optional<Tutoring> findByMonitor_UserMonitor_Username(String username);
}
