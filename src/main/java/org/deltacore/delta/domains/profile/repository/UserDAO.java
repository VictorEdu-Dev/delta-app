package org.deltacore.delta.domains.profile.repository;

import org.deltacore.delta.domains.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM user_delta u WHERE u.username = :username")
    Optional<Long> findIdByUsername(String username);
}
