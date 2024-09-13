package com.baopen753.securitywithauthorizationimplementing.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenProvider {


    @Value("${jwt.sender}")
    private String jwtSender;

    // this method generates an HMAC-SHA key from the input is plain text key
    public SecretKey getSecretKey(String key) {
        // convert key in plain format into byte[] string
        byte[] byteOfKey = key.getBytes();
        return Keys.hmacShaKeyFor(byteOfKey);
    }

    public String createToken(SecretKey secretKey) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return Jwts.builder()
                .issuer(jwtSender)
                .subject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 30000000))
                .signWith(secretKey).compact();
    }

    public Claims validateToken(String jwt, SecretKey secretKey) {
        JwtParser parser = createJwtParser(secretKey);                 // symmetric key
        return parser.parseSignedClaims(jwt).getPayload();             // this parseSignedClaims(jwt) will do:   + validate jwt syntax, format
                                                                       //                                        + header parsing, payload parsing, signature verification
                                                                       // claims extraction:                     + parser extracts claims from payload
    }

    private JwtParser createJwtParser(SecretKey secretKey) {
        // Jwts.parser() return a JwtParserBuilder(): has a role to construct a JwtParser through build()
        return Jwts.parser().verifyWith(secretKey).build();
    }
}
