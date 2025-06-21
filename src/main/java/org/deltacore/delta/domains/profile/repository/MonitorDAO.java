package org.deltacore.delta.domains.profile.repository;

import org.deltacore.delta.domains.profile.model.Tutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MonitorDAO extends CrudRepository<Tutor,Long> {
    @Query(value = "SELECT mo.* FROM monitor mo JOIN user_delta us ON mo.user_monitor_id = us.id WHERE us.username = :username", nativeQuery = true)
    Optional<Tutor> findByUserUsername(@Param("username") String username);
}
