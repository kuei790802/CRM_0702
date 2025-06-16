package com.example.demo.security;

import com.example.demo.entity.VIPLevel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTool {
    private static final String SECRET = "ben-ben-is-such-a-nice-guy-so-heis-giving-u-a-key-x0x0";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2;
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());


    public static String createToken(String customerName, String account, Long customerId, VIPLevel vipLevel){
        return Jwts.builder()
                .setSubject(account)
                .claim("customerId", customerId)
                .claim("customerName", customerName)
                .claim("viplevel", vipLevel)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 回傳整個 payload
    public static Claims parseToken(String token){
        try {
            Key skey = Keys.hmacShaKeyFor(SECRET.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(skey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e){
            return null;
        }
    }

    // 回傳指定欄位account
    public static String parseTokenToAccount(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("account", String.class) : null;
    }
}
