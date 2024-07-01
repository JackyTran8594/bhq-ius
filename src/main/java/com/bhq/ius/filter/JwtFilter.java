package com.bhq.ius.filter;

import com.bhq.ius.domain.service.UserDetailCustomServiceImpl;
import com.bhq.ius.domain.service.UserService;
import com.bhq.ius.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

//    @Autowired
    private final UserDetailCustomServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        long startTime = System.currentTimeMillis();
        log.info("==== start time ==== {}", startTime);
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtUtil.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
                    log.warn("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    log.warn("JWT Token has expired");
                }
            } else {
                log.warn("JWT Token does not begin with Bearer String");
            }
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                log.info("===SecurityContextHolder getPrincipal UserDetails: "
                        + ((UserDetails) principal).getUsername());
            } else {
                log.info("===SecurityContextHolder getPrincipal: "
                        + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            }

        }
        // Once we get the token validate it.
        if (username != null
                && (SecurityContextHolder.getContext().getAuthentication() == null
                || "anonymousUser".equalsIgnoreCase(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }

        ContentCachingResponseWrapper responseCachingWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        filterChain.doFilter(request, response);

        // copy body to response
        responseCachingWrapper.copyBodyToResponse();


    }


}
