package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
