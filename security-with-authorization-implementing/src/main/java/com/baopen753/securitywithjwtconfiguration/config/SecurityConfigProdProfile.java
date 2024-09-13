package com.baopen753.securitywithauthorizationimplementing.config;

import com.baopen753.securitywithauthorizationimplementing.exception.MyAccessDeniedHandler;
import com.baopen753.securitywithauthorizationimplementing.exception.MyBasicAuthenticationEntryPoint;
import com.baopen753.securitywithauthorizationimplementing.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.RequestContextFilter;

import java.util.Collections;


@Configuration
@Profile("prod")
public class SecurityConfigProdProfile {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RequestContextFilter requestContextFilter) throws Exception {


        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false));

        http.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/contact", "/register")    // public IP with POST, PUT, DELETE request should not require CSRF Token
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))   // set HttpOnly is false: to let both browser and JS-based UI can read cookie
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);  // generated Csrf token after authentication
        //.csrf(AbstractHttpConfigurer::disable)


        http.cors(corsConfigurer -> corsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                return config;
            }
        }));


        //.sessionManagement(ssm -> ssm.invalidSessionUrl("/invalidSession"))
        //.sessionManagement(ssm -> ssm.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession))  // spring fw uses changeSessionId by default
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));


        //    .requiresChannel(rcConfig -> rcConfig.anyRequest().requiresSecure())     // only HTTPs traffic

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/myAccount").hasAuthority("VIEW_ACCOUNT")
                .requestMatchers("/myCard").hasAuthority("VIEW_CARDS")
                .requestMatchers("myBalance").hasAuthority("VIEW_BALANCE")
                .requestMatchers("myLoans").hasAuthority("VIEW_LOANS")
                .requestMatchers("user").authenticated()
                .requestMatchers("/hello", "/contact", "/register", "/invalidSession", "/notices").permitAll());

        http.formLogin(Customizer.withDefaults());
        //.formLogin(flc -> flc.loginPage("/login").defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())   --> for monolithic app or Spring MVC

        http.logout(lc -> lc.logoutSuccessUrl("/login?logout=true").permitAll()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
        );


        http.httpBasic(hbConfig -> hbConfig.authenticationEntryPoint(new MyBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehConfig -> ehConfig.accessDeniedHandler(new MyAccessDeniedHandler()));

        return http.build();
    }

    //    @Bean
    //    public UserDetailsService userDetailsService(DataSource dataSource) {
    //        return new JdbcUserDetailsManager(dataSource);
    //    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
