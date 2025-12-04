package com.springsecurity.demo.filter;

import com.springsecurity.demo.JwtService;
import com.springsecurity.demo.configuration.BankUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Autowired
    BankUserDetailsService bankUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String Token=null;
        String username = null;
        if(header!=null && header.startsWith("Bearer ")){
            Token = header.substring(7);
            username = jwtService.extractUsername(Token);

        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = bankUserDetailsService.loadUserByUsername(username);
            if(jwtService.validate(Token,userDetails))
            {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());
                System.out.println("Roles: "+userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }
filterChain.doFilter(request,response);
    }
}
