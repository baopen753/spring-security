package com.baopen753.securitywithdatabaseconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.filter.RequestContextFilter;

@Configuration
public class SecurityConfig {

    public SecurityConfig(RequestContextFilter requestContextFilter) {
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                //  .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .authorizeHttpRequests(request -> request.requestMatchers("/myAccount", "/myCard", "/myLoans", "/myBalance").authenticated()  // these are protected endpoints
                        .requestMatchers("/hello", "/notices", "/contact").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.withUsername("user").password("{noop}12345").authorities("READ").roles("USER").build();
        UserDetails user2 = User.withUsername("baopen753").password("{bcrypt}$2a$12$qFZsYy8Zf11qLvUQmsoMmuKf681/k8MoM3a18RqK6Yd3iIbTAgUjm").authorities("ADMIN").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2);
    }
    // if we only config UserDetailService Bean without PasswordEncoder bean, Spring Security will throw an exception


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}

