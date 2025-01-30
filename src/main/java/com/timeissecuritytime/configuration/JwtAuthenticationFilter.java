package com.timeissecuritytime.configuration;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.timeissecuritytime.service.UserService;
import com.timeissecuritytime.util.JwtTokenUtil;
import com.timeissecuritytime.Constants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Optional<String> tokenOptional = extractToken(request);
            if (tokenOptional.isPresent()) {
                String token = tokenOptional.get();
                if (jwtTokenUtil.validateToken(token)) {
                    setAuthentication(token);
                } else {
                    handleInvalidToken(response);
                    return;
                }
            }
        } catch (Exception e) {
            handleFilterException(response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(Constants.AUTHORIZATION_HEADER))
                .filter(header -> header.startsWith(Constants.BEARER_PREFIX))
                .map(header -> header.substring(Constants.BEARER_PREFIX.length()));
    }

    private void setAuthentication(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleInvalidToken(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
        response.getWriter().write(Constants.INVALID_TOKEN_MESSAGE);
    }

    private void handleFilterException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  
        response.getWriter().write(Constants.UNEXPECTED_ERROR_MESSAGE);
    }
}
