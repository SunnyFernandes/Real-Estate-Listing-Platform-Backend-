package com.relp.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.key}")
    private String key;

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.issuer}")
    private String issuer;

    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct() throws UnsupportedEncodingException{
        algorithm = Algorithm.HMAC256(key);
    }

    public String generateToken(String email){
            return JWT.create()
                    .withClaim("email",email)
                    .withExpiresAt(new Date(System.currentTimeMillis()+ expiry))
                    .withIssuer(issuer)
                    .sign(algorithm);
    }

    public String getEmailId(String jwtToken) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(jwtToken);
        return decodedJWT.getClaim("email").asString();
    }
}
