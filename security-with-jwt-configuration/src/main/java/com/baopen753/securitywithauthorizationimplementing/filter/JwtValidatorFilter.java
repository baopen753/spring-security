package com.baopen753.securitywithauthorizationimplementing.filter;

import com.baopen753.securitywithauthorizationimplementing.constant.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtValidatorFilter extends OncePerRequestFilter {

//    private final JwtTokenProvider jwtTokenProvider;
//
//    public JwtValidatorFilter(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Here are keys for performing JWT validation

        // 1. Retrieve and parse JSON from Authorization header
        String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);

       if(jwt!=null){
           try {
               Environment env = getEnvironment();

               //  get key to compare with secret key from request
               String key = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

               //  SecretKey secretKey = jwtTokenProvider.getSecretKey(key);
               SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

               //  Claims claims = jwtTokenProvider.validateToken(jwt, secretKey);
               Claims claims = Jwts.parser().verifyWith(secretKey).build()
                       .parseSignedClaims(jwt).getPayload();

               String username = claims.get("username").toString();
               String authorities = claims.get("authorities").toString();

               Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
               SecurityContextHolder.getContext().setAuthentication(authentication);

           } catch (Exception ex) {
               throw new BadCredentialsException("Invalid JWT received: ", ex);
           }
       }

        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().endsWith("/user");   // no invoke validate filter with endpoint /user
    }
}
