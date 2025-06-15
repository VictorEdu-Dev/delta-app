package org.deltacore.delta.repositorie.user;

import org.deltacore.delta.model.user.Monitor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MonitorDAO extends CrudRepository<Monitor,Long> {
    @Query(value = "SELECT mo.* FROM monitor mo JOIN user_delta us ON mo.user_monitor_id = us.id WHERE us.username = :username", nativeQuery = true)
    Optional<Monitor> findByUserUsername(@Param("username") String username);
}
