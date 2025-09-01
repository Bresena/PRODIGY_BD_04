package com.user.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.user.model.User;
import com.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    
    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String email = null;
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.extractAllClaims(token);
                email = claims.getSubject();
            } catch (Exception e) {
                // invalid token — you can set response status here if you want
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
        	Optional<User> optionalUser = userRepository.findByEmail(email);
        	if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user.getEmail(), null,
                                List.of(new SimpleGrantedAuthority(user.getRole().name())));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}