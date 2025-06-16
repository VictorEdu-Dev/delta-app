package org.deltacore.delta.domains.auth.repository;

import org.deltacore.delta.domains.auth.model.Blacklist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BlacklistDAO extends CrudRepository<Blacklist, String> {
    @Query(value = "SELECT b FROM Blacklist b JOIN FETCH b.user u WHERE b.token = :token AND u.username = :username")
    Optional<Blacklist> findByToken(@Param("token") String token, @Param("username") String username);

    @Query(value = "SELECT b FROM Blacklist b WHERE b.expiryDate < :now")
    List<Blacklist> findAllByExpiryDateBefore(@Param("now") Instant now);
}
