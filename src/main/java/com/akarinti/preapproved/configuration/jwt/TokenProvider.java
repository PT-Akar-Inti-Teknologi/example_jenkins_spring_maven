package com.akarinti.preapproved.configuration.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider {

    @Value("${access.token.validity.seconds}")
    Long accessTokenValidity;

    @Value("${signing.key}")
    String signingKey;

    @Value("${authorities.key}")
    String authoritiesKey;

    @Value("${jwt.audience}")
    String audience;

    @Value("${token.audience}")
    String tokenAudience;

    @Value("${token.username}")
    String tokenUsername;

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(Claims.SUBJECT, String.class);
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(Claims.EXPIRATION, Date.class);
    }

    public Claims getClaimsFromToken(String token){
        return getAllClaimsFromToken(token);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authoritiesKey, authorities)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity*1000 * 11000)) // expire 30 years
                .compact();
    }

    public String generatePublicToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authoritiesKey, authorities)
                .setAudience(tokenAudience)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity*1000 * 11000)) // expire 30 years
                .compact();
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);

        //return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        return !isTokenExpired(token);
    }

    public Boolean validatePublicToken(String token) {
        final String username = getUsernameFromToken(token);

        return !isTokenExpired(token) && username.equals(tokenUsername);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final String sessionId) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(signingKey);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(authoritiesKey).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(sessionId, token, authorities);
    }
}
