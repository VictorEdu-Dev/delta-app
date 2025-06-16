package org.deltacore.delta.domains.auth.repository;

import org.deltacore.delta.domains.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
