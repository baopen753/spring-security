package com.baopen753.securitywithauthorizationimplementing.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {

    /**
     * This method does checking whether the username contains 'test' within username or not.
     * <p>
     * How to do:  <p>
     * + type-casting ServletRequest -> HttpServletRequest, ServletResponse -> HttpServletResponse <p>
     * + check Authorization header is existed ?. <p>
     * + extract the Header and decode based64 token (because user's credential is encoded in base64).<p>
     * + after decoding, we get format of string:  'username:password'.<p>
     * + check username contains('test') ??.<p>
     * + if no, return Bad Request 400.<p>
     * + if yes, send request to BasicAuthenticationFilter.<p>
     *
     * @param request
     * @param response
     * @param chain
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. type casting ServletRequest -> HttPRequest and same with response due to using Http protocol
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 2. get Authorization header
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            header = header.trim();
            if (StringUtils.startsWithIgnoreCase(header, "Basic ")) {
                byte[] base64Token = header.split(" ")[1].getBytes(StandardCharsets.UTF_8);
                byte[] decode;

                try {

                    decode = Base64.getDecoder().decode(base64Token);
                    String token = new String(decode, StandardCharsets.UTF_8);

                    String username = token.split(":")[0];
                    if (username.toLowerCase().contains("test")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException("Failed to decode basic authentication token");
                }
            }
        }
        chain.doFilter(request, response);
    }
}
