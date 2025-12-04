package com.springsecurity.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public String generateToken(String name) {
        Map<String,Object> map = new HashMap<>();
        return Jwts.builder()
                .subject(name)
                .claims(map)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*3))
                .signWith(getkey(), SignatureAlgorithm.HS256).compact();
    }
    private Key getkey(){
        byte[] keybytes = Decoders.BASE64.decode("yourBase64EncodedSecret32BytesOrMoreHere==key1234");
        return Keys.hmacShaKeyFor(keybytes);
    }

    public String extractUsername(String token) {

        return extractClaims(token,Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }
    public Date extractIssuedAt(String token){
        return extractClaims(token,Claims::getIssuedAt);
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);

    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith((SecretKey) getkey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validate(String token, UserDetails userDetails) {
    final String user = extractUsername(token);
    return user.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String Token) {
        Date Edate = extractClaims(Token,Claims::getExpiration);
        return Edate.before(new Date());
    }
}
