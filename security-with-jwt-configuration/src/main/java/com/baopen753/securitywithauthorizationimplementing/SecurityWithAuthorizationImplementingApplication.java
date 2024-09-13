package com.baopen753.securitywithauthorizationimplementing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)  // not recommend in production environment
public class SecurityWithAuthorizationImplementingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityWithAuthorizationImplementingApplication.class, args);
    }

}
