package com.akarinti.preapproved.configuration.jwt;

import com.akarinti.preapproved.service.SignInService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${token.prefix}")
    String tokenPrefix;

    @Value("${header.string}")
    String header;

    @Value("${token.audience}")
    String tokenAudience;

    @Autowired
    SignInService signInService;

    @Autowired
    TokenProvider tokenProvider;

    @Value("${token.username}")
    String tokenUsername;

    @Value("${token.password}")
    String tokenPassword;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException  {
        String headerReq = request.getHeader(header);
        String username = null;
        String authToken = null;
        String audience = null;

        if (headerReq != null && headerReq.startsWith(tokenPrefix)) {
            authToken = headerReq.replace(tokenPrefix,"");
            try {
                Claims claims = tokenProvider.getClaimsFromToken(authToken);
                audience = claims.get(Claims.AUDIENCE, String.class);
                username = claims.get(Claims.SUBJECT, String.class);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        if (audience != null && audience.equals(tokenAudience)) {
            if (tokenProvider.validatePublicToken(authToken)) {
                UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), username);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //UserDetails userDetails = signInService.loadUserByUsername(username);
            if (tokenProvider.validateToken(authToken)) {
                UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), username);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
