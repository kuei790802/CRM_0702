package com.example.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTool {
    private static final long EXP_TIME = 3* 60 * 1000;
    private static final String SECURET = "BENBENEBNBENBEN123456789";
    private static final Key key = Keys.hmacShaKeyFor(SECURET.getBytes());


    public static String createToken(String email){
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static String parseToken(String token){
        Key skey = Keys.hmacShaKeyFor(SECURET.getBytes());
        JwtParser parser = Jwts.parserBuilder().setSigningKey(skey).build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        String email = claims.getSubject();
        return email;
    }
}
