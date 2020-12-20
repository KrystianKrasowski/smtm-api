package com.smtm.application.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.smtm.security.api.Authorization;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Authorization authorization;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, Authorization authorization) {
        super(authenticationManager);
        this.authorization = authorization;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        extractToken(request)
            .flatMap(this::authorize)
            .ifPresent(this::setAuthenticationToken);

        chain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
            .filter(header -> header.startsWith("Bearer "))
            .map(header -> header.replace("Bearer ", ""));
    }

    private Optional<UsernamePasswordAuthenticationToken> authorize(String tokenValue) {
        return Optional.ofNullable(authorization.authorize(tokenValue))
            .map(token -> new UsernamePasswordAuthenticationToken(token.getUserId(), null, new ArrayList<>()));
    }

    private void setAuthenticationToken(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder
            .getContext()
            .setAuthentication(token);
    }
}
