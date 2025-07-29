package org.deltacore.delta.domains.auth.repository;

import jakarta.transaction.Transactional;
import org.deltacore.delta.domains.auth.model.RefreshToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenDAO extends CrudRepository<RefreshToken, Long> {
    @Query("SELECT rt FROM RefreshToken rt " +
            "JOIN FETCH rt.user u " +
            "WHERE u.username = :username AND rt.revoked = false")
    List<RefreshToken> findByUser(@Param("username") String username);

    @Query("SELECT rt FROM RefreshToken rt JOIN FETCH rt.user u WHERE rt.token = :refreshToken")
    Optional<RefreshToken> findByRefreshToken(@Param("refreshToken") UUID refreshToken);


    @Query("SELECT rt.id FROM RefreshToken rt WHERE rt.revoked = true")
    List<Long> findRevokedTokenIds(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.expiresAt < CURRENT_TIMESTAMP AND t.revoked = false")
    void markExpiredTokensAsRevoked();

    @Transactional
    void deleteByIdIn(List<Long> ids);
}
