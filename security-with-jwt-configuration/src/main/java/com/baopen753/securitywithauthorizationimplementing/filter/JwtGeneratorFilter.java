package com.baopen753.securitywithauthorizationimplementing.filter;

import com.baopen753.securitywithauthorizationimplementing.constant.ApplicationConstants;
import com.baopen753.securitywithauthorizationimplementing.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtGeneratorFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtGeneratorFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get Authentication after going through BasicAuthenticationFilter (Authentication successfully)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // check AuthN after going through BasicAuthenticationFilter
        if (authentication != null) {
            Environment env = getEnvironment();
            String key = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = jwtTokenProvider.getSecretKey(key);

            // build a complete jwt
            String jwt =  jwtTokenProvider.createToken(secretKey);

            // set jwt to a header
            response.setHeader(ApplicationConstants.JWT_HEADER, jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().endsWith("/user");   //   not filter with endpoint /user
    }
}
