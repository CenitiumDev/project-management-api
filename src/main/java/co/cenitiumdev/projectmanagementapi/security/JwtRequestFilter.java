package co.cenitiumdev.projectmanagementapi.security;

import co.cenitiumdev.projectmanagementapi.config.JwtAuthenticationEntryPoint;
import co.cenitiumdev.projectmanagementapi.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;


    public JwtRequestFilter(JwtUtil jwtUtil, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired: " + e.getMessage());
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("JWT Token has expired.", e));
                return;
            } catch (SignatureException e) { // Manejar SignatureException
                logger.warn("Invalid JWT Signature: " + e.getMessage());
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("Invalid JWT signature.", e));
                return; // ¡CRUCIAL: Detener la cadena de filtros aquí!
            } catch (MalformedJwtException e) {
                logger.warn("Malformed JWT: " + e.getMessage());
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("Malformed JWT token.", e));
                return;
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token: " + e.getMessage());
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("Unable to get JWT token.", e));
                return;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String or is missing for protected endpoint.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
