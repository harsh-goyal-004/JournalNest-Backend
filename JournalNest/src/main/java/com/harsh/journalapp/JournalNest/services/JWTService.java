package com.harsh.journalapp.JournalNest.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;


//    This method generates an Access Token
    public String generateAccessToken(String username){
        Map<String,Object> claim = new HashMap<>();
        return Jwts.builder()
                .claims(claim)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .subject(username)
                .signWith(getKey())
                .compact();

    }


//    This method generates a Refresh Token
    public String generateRefreshToken(String username){
        Map<String, Object> claim = new HashMap<>();
        return  Jwts.builder()
                .claims(claim)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7l))
                .subject(username)
                .signWith(getKey())
                .compact();
    }

//    This method extracts username from access token
    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

//    This method validates the token
    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        if(username != null && !username.isEmpty()){
            return (userDetails.getUsername().equals(username) && !isTokenExpired(token));
        }
        return false;
    }

//    This method checks whether the token is expired
    public boolean isTokenExpired(String token){
        Date expiration = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }



    public SecretKey getKey(){
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(byteKey);
    }
}
