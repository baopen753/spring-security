package com.baopen753.securitywithbasicconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.RequestContextFilter;

@Configuration
public class SecurityConfig {

    public SecurityConfig(RequestContextFilter requestContextFilter) {
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                //  .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/myAccount", "/myCard"   , "/myLoans", "/myBalance").authenticated()  // these are protected endpoints
                        .requestMatchers("/hello","/notices","/contact").permitAll())
                .formLogin(formLoginConfig -> formLoginConfig.disable())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}

