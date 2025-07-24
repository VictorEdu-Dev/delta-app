package org.deltacore.delta.domains.auth.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import org.deltacore.delta.domains.auth.exception.InvalidRecoveryCodeException;
import org.deltacore.delta.domains.auth.model.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecoveryCodeDAO extends JpaRepository<RecoveryCode, String> {
    Optional<RecoveryCode> findTopByEmailAndCodeAndReasonOrderByCreatedAtDesc(@Email String email, @Positive String code, RecoveryCode.Reason reason);

    Optional<RecoveryCode> findTopByEmailAndCodeAndReasonOrderByExpirationDesc(String email, String code, RecoveryCode.Reason reason);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecoveryCode rc WHERE rc.email = :email AND rc.code = :code AND rc.reason = :reason")
    void deleteByEmailCodeAndReason(@Param("email") String email, @Param("code") String code, @Param("reason") RecoveryCode.Reason reason);
}
