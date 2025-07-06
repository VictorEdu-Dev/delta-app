package org.deltacore.delta.domains.profile.repository;

import org.deltacore.delta.domains.profile.model.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileDAO extends CrudRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p JOIN FETCH p.user u WHERE u.username = ?1")
    Optional<Profile> findByUserUsername(String username);
}
