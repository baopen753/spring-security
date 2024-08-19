package com.baopen753.securitywithcommonusecases.exception;

import com.baopen753.securitywithcommonusecases.error.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String RESPONSE_CONTENT_TYPE = "application/json";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // customize Error response JSON
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

        Error error = Error.builder()
                .date(date)
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        String responseBody = objectMapper.writeValueAsString(error);

        response.setHeader("Baopenbank-Error", "Unauthorized");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.getWriter().write(responseBody);
    }
}
