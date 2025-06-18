package org.deltacore.delta.domains.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtTokenService {
    private static final String ISSUER = "DELTA APPLICATION";
    private final String secret;

    public JwtTokenService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public TokenInfoDTO generateTokenInfo(String username, String role) throws JWTCreationException {
        Date issuedAt = new Date();
        long expirationMillis = 900_000;
        Date expiresAt = new Date(System.currentTimeMillis() + expirationMillis);

        String token = JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuer(ISSUER)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(getAlgorithm());

        TokenInfoDTO.TokenInfoValueDTO tokenInfoValue = TokenInfoDTO.TokenInfoValueDTO
                .builder()
                .username(username)
                .token(token)
                .expiresAt(expiresAt.toInstant())
                .build();
        return TokenInfoDTO
                .builder()
                .meta("token_info")
                .tokenInfoValue(tokenInfoValue)
                .build();
    }

    public String generateToken(String username, String role) throws JWTCreationException {
        return generateTokenInfo(username, role)
                .tokenInfoValue()
                .token();
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public String getRolesFromToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("role").asString();
    }

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(ISSUER)
                .build();

        return verifier.verify(token);
    }
}

