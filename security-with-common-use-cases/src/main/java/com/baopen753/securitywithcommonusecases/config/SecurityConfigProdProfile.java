package com.baopen753.securitywithcommonusecases.config;

import com.baopen753.securitywithcommonusecases.exception.MyAccessDeniedHandler;
import com.baopen753.securitywithcommonusecases.exception.MyBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;


@Configuration
@Profile("prod")
public class SecurityConfigProdProfile {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(ssm -> ssm.invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
                .sessionManagement(ssm -> ssm.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession))  // spring fw uses changeSessionId by default


               //  .requiresChannel(rcConfig -> rcConfig.anyRequest().requiresSecure())   // only HTTPs traffic
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/myAccount","/myBalance").authenticated())
                .authorizeHttpRequests(request -> request.requestMatchers("/hello", "/register","/invalidSession").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(hbConfig -> hbConfig.authenticationEntryPoint(new MyBasicAuthenticationEntryPoint()))
                .exceptionHandling(ehConfig -> ehConfig.accessDeniedHandler(new MyAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
