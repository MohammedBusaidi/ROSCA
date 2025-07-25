package com.example.RoscaApp.security;


import com.example.RoscaApp.dto.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtService {
    private Environment environment;
    final String SECRET_KEY = environment.getProperty("JWT_SECRET", String.class);
    private String createToken(Map<String,Object> claims, String subject) {
        Key key = getSignInKey();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+
                        1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }

    public String generateToken(String userId, Role role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", "ROLE_"+role.name());
        return createToken(claims, userId);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte [] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserType(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }


    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }


}