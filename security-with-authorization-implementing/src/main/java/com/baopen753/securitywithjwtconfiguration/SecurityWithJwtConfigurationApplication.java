package com.baopen753.securitywithjwtconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableWebSecurity(debug = true)  // not recommend in production environment
public class SecurityWithJwtConfigurationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityWithJwtConfigurationApplication.class, args);
    }

}
