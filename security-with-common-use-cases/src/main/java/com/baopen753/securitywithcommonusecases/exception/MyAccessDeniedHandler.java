package com.baopen753.securitywithcommonusecases.exception;

import com.baopen753.securitywithcommonusecases.error.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private static final String RESPONSE_CONTENT_TYPE = "application/json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        String message = (accessDeniedException != null || accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage() : "Forbidden";


        Error error = Error.builder()
                .date(date)
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        String responseBody = objectMapper.writeValueAsString(error);
        response.setHeader("Baopenbank-Denied-Access", "Authorization failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.getWriter().write(responseBody);
    }
}

