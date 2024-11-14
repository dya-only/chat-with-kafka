package me.dyacode.chat_with_kafka.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private String secret = "1c9eeee862d5dc324cffc1c4009cb3e4d3ab2e434159ee4e813dda0ae33c9bf3f2caa5ad3f604cfb4a2e7914fe3597d8cb1a10842807d55e55a285abc6cf2e414d4d5b07a3907d537ca78ca081181492681345acd721eca052cd85ffc71908bc59c4d538f0c56f47f038f8149e95221c42988ea4ad8fd76a207ffa8e109a937086a6fa91c26a3e066459cb4e65b012789f16c2203693d9b4ae534ee9ea31c70925bdae04af638e59b09bb03ce8f7428e8ed101cdeae8a58bfb9e5fc73b6e64874db4e12421885c84fcb7bbc354fcbe5b7582263e6170d10ff6574f7f0136199d1889be259080352cebf7cec0f9e54ba07239af5a2a65f18a5ed167a4a8f28df3";

    Key key = Keys.hmacShaKeyFor(secret.getBytes());

    public String createToken(Long id) {
        return Jwts
                .builder()
                .claim("id", id)
                .signWith(key)
                .compact();
    }

    public Long getId(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ignored) {
        }

        return false;
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}