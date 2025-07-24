package org.deltacore.delta.domains.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.auth.dto.ForgotPasswordRequest;
import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;

import org.deltacore.delta.domains.auth.dto.LoginRequest;
import org.deltacore.delta.domains.auth.exception.InvalidRecoveryCodeException;
import org.deltacore.delta.domains.auth.exception.InvalidTokenException;
import org.deltacore.delta.domains.auth.model.RecoveryCode;
import org.deltacore.delta.domains.auth.repository.RecoveryCodeDAO;
import org.deltacore.delta.domains.profile.exception.UserNotFoundException;
import org.deltacore.delta.domains.auth.model.RefreshToken;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.auth.repository.RefreshTokenDAO;
import org.deltacore.delta.domains.profile.repository.UserDAO;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthCmdService {
    private static final String DEFAULT_ROLE = "ROLE_STUDENT";
    private UserDAO userDAO;
    private RefreshTokenDAO refreshTokenDAO;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RecoveryCodeDAO recoveryCodeDAO;
    private JavaMailSender mailSender;

    public TokenInfoDTO getToken(LoginRequest request,
                                 AuthenticationManager authManager,
                                 JwtTokenService jwtService) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username().toLowerCase(), request.password())
        );

        String username = authentication.getName();
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        String role = Optional.ofNullable(user.getRole())
                .map(r -> "ROLE_" + r.name())
                .orElse(DEFAULT_ROLE);

        return jwtService.generateTokenInfo(authentication.getName(), role);
    }

    public TokenInfoDTO refresh(UUID token, JwtTokenService jwtService) {
        RefreshToken refreshToken = refreshTokenDAO.findByRefreshToken(token)
                .filter(rt -> !rt.isRevoked())
                .filter(rt -> rt.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired refresh token: " + token));

        String username = refreshToken.getUser().getUsername();
        String role = "ROLE_" + refreshToken.getUser().getRole().name();

        refreshToken.setRevoked(true);
        refreshTokenDAO.save(refreshToken);

        return jwtService.generateTokenInfo(username, role);
    }

    public TokenInfoDTO revoke(UUID token) {
        RefreshToken refreshToken = refreshTokenDAO.findByRefreshToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found: " + token));

        if (refreshToken.isRevoked()) {
            throw new InvalidTokenException("Refresh token already revoked: " + token);
        }

        refreshToken.setRevoked(true);
        refreshTokenDAO.save(refreshToken);

        return TokenInfoDTO
                .builder()
                .meta("Refresh token revoked successfully.")
                .build();
    }

    @Transactional
    public void changePassword(LoginRequest.@Valid
                               LoginChangePasswordRequest login,
                               AuthenticationManager authManager,
                               AuthenticatedUserProvider authenticatedUser) {
        String currentUsername = authenticatedUser.currentUsername();
        String currentPassword = login.currentPassword();
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(currentUsername, currentPassword)
        );

        User user = userDAO.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authentication.getName()));

        String encodedNewPassword = bCryptPasswordEncoder.encode(login.newPassword());
        user.setPasswordHash(encodedNewPassword);
    }

    public void sendRecoveryCode(ForgotPasswordRequest request, RecoveryCode.Reason reason) {
        String email = request.email().replaceAll("\\s+", "").toLowerCase();
        User username = userDAO.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.email()));

        String code = generateCode();
        OffsetDateTime expiration = OffsetDateTime.now().plusMinutes(5);

        RecoveryCode recoveryCode = new RecoveryCode();
        recoveryCode.setCode(code);
        recoveryCode.setUsername(username.getUsername());
        recoveryCode.setEmail(username.getEmail());
        recoveryCode.setExpiration(expiration);
        recoveryCode.setReason(reason);
        recoveryCodeDAO.save(recoveryCode);

        sendEmail(username.getEmail(), username.getUsername(), code);
    }

    private String generateCode() {
        return String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
    }

    public void sendEmail(String to, String username, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            helper.setFrom("deltacoreapp-no-reply@gmail.com");
            helper.setTo(to);
            helper.setSubject("Delta Application - Código de Verificação");

            String htmlContent = """
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;">
                <div style="background-color: #ffffff; padding: 25px; border-radius: 8px; max-width: 600px; margin: auto; box-shadow: 0 0 10px rgba(0,0,0,0.05);">
                  <h2 style="color: #333333;">Eai, mofi %s,</h2>
                  <p style="font-size: 16px; color: #555555;">
                    Aqui está seu código de verificação:
                  </p>
                  <p style="font-size: 24px; font-weight: bold; color: #222222; margin: 20px 0;">
                    %s
                  </p>
                  <p style="font-size: 14px; color: #999999;">
                    Esse código aí vai explodir em 5 minutos.
                  </p>
                  <p style="font-size: 14px; color: #999999; margin-top: 20px;">
                    Se você não pediu por isso, você foi hackeado... Ou então, só ignore este email.
                  </p>
                  <p style="margin-top: 30px; color: #666666;">
                    — Delta Core Team
                  </p>
                </div>
              </body>
            </html>
            """.formatted(username, code);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail de verificação", e);
        }
    }

    @Transactional
    public boolean verifyRecoveryCode(String email, String code, RecoveryCode.Reason reason) {
        Optional<RecoveryCode> recoveryCodeOpt = recoveryCodeDAO
                .findTopByEmailAndCodeAndReasonOrderByCreatedAtDesc(email, code, reason);

        if (recoveryCodeOpt.isEmpty()) return false;
        RecoveryCode recoveryCode = recoveryCodeOpt.get();

        boolean verify = !OffsetDateTime.now().isAfter(recoveryCode.getExpiration());
        if (!verify) recoveryCodeDAO.deleteByEmailCodeAndReason(email, code, reason);
        return verify;
    }

    @Transactional(noRollbackFor = InvalidRecoveryCodeException.class)
    public void resetPassword(String email, String code, String s) {
        RecoveryCode recoveryCode = recoveryCodeDAO
                .findTopByEmailAndCodeAndReasonOrderByExpirationDesc(email, code, RecoveryCode.Reason.FORGOT_PASSWORD)
                .orElseThrow(() -> new InvalidRecoveryCodeException("Invalid or expired recovery code"));

        if (OffsetDateTime.now().isAfter(recoveryCode.getExpiration())) {
            recoveryCodeDAO.deleteByEmailCodeAndReason(email, code, RecoveryCode.Reason.FORGOT_PASSWORD);
            throw new InvalidRecoveryCodeException("Recovery code has expired");
        }

        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newPassword = bCryptPasswordEncoder.encode(s);
        user.setPasswordHash(newPassword);
        recoveryCodeDAO.delete(recoveryCode);
        sendPasswordChangedEmail(email, user.getUsername());
    }

    public void sendPasswordChangedEmail(String to, String username) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            helper.setFrom("deltacoreapp-no-reply@gmail.com");
            helper.setTo(to);
            helper.setSubject("Delta - Senha alterada com sucesso");

            String htmlContent = """
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;">
                <div style="background-color: #ffffff; padding: 25px; border-radius: 8px; max-width: 600px; margin: auto; box-shadow: 0 0 10px rgba(0,0,0,0.05);">
                  <h2 style="color: #333333;">Olá, %s</h2>
                  <p style="font-size: 16px; color: #555555;">
                    O indiano que chamei conseguiu invadir o banco e, assim, sua senha foi alterada com sucesso.
                  </p>
                  <p style="font-size: 14px; color: #999999; margin-top: 20px;">
                    Se você não realizou essa alteração, entre em contato imediatamente com o suporte.
                  </p>
                </div>
              </body>
            </html>
            """.formatted(username);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail de confirmação de alteração de senha", e);
        }
    }


    @Autowired
    private void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Autowired @Lazy
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired @Lazy
    public void setRefreshTokenDAO(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Autowired @Lazy
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired @Lazy
    public void setRecoveryCodeDAO(RecoveryCodeDAO recoveryCodeDAO) {
        this.recoveryCodeDAO = recoveryCodeDAO;
    }
}
