package org.deltacore.delta.domains.auth.repository;

import org.deltacore.delta.domains.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM user_delta u LEFT JOIN FETCH u.tutorings WHERE u.username = :username")
    Optional<User> findByUsernameWithTutorings(@Param("username") String username);

    @Query("SELECT u.id FROM user_delta u WHERE u.username = :username")
    Optional<Long> findIdByUsername(String username);
}
