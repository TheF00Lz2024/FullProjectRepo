package com.cognizant.config;

import com.cognizant.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

import java.util.Date;
import java.util.HashMap;
import java.security.Key;
import java.util.Map;

@Service("JWTTokenImpl")
public class JWTTokenImpl implements JWTTokenGenerator{
    //get secret key from application.properties file
    @Value("${jwt.secret}")
    private String secret;
    //function to generate JWT token
    @Override
    public Map<String, String> generateToken(User user) {
        // decode the secret and make new Key
        Key newKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        // creating the jwt token
        String jwtToken = Jwts.builder().subject(user.getUsername()+ ",Login: Success").issuedAt(new Date()).signWith(newKey).compact();
        Map<String, String> jwtTokenMap = new HashMap<>();
        // add the created key to map and some additional data to check
        jwtTokenMap.put("token",jwtToken);
        jwtTokenMap.put("message","Login Successful");
        return jwtTokenMap;
    }
}
