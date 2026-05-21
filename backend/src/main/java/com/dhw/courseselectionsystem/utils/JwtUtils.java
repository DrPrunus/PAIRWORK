package com.dhw.courseselectionsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String SIGN_KEY = "6YKTKuaWh+a4hei/nOW4gua4heaWsOWMuua4heWSjOWkp+mBkw==";
    private static final Long EXPIRE = 43200000L;

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(Keys.hmacShaKeyFor(SIGN_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SIGN_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}