package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDAO extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}
