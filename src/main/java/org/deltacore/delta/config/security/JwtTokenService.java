package org.deltacore.delta.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtTokenService {

    private Algorithm getAlgorithm() {
        String secret = "9PZtBd3RH6Y2M3h8U8nh0R6cWmAQN+itHHeupX7jnhw=";
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(String username) throws JWTCreationException {
        long expirationMillis = 3600000; // 1 hora
        return JWT.create()
                .withSubject(username)
                .withIssuer("DELTA APPLICATION")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(getAlgorithm());
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer("DELTA APPLICATION")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}

