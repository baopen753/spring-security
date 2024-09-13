package com.baopen753.securitywithauthorizationimplementing.config;

import com.baopen753.securitywithauthorizationimplementing.exception.MyAccessDeniedHandler;
import com.baopen753.securitywithauthorizationimplementing.exception.MyBasicAuthenticationEntryPoint;
import com.baopen753.securitywithauthorizationimplementing.filter.CsrfCookieFilter;
import com.baopen753.securitywithauthorizationimplementing.filter.JwtGeneratorFilter;
import com.baopen753.securitywithauthorizationimplementing.filter.JwtValidatorFilter;
import com.baopen753.securitywithauthorizationimplementing.filter.RequestValidationBeforeFilter;
import com.baopen753.securitywithauthorizationimplementing.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import java.util.List;


@Configuration
@Profile("!prod")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RequestContextFilter requestContextFilter) throws Exception {

        // 1.   http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false));

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //.sessionManagement(ssm -> ssm.invalidSessionUrl("/invalidSession"))
        //.sessionManagement(ssm -> ssm.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession))  // spring fw uses changeSessionId by default


        http.cors(corsConfigurer -> corsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(List.of("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        }));

        http.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/contact", "/register","/apiLogin")             // for public endpoint, no need to protect from CSRF attack                              // public IP with POST, PUT, DELETE request should not require CSRF Token
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))                             // set HttpOnly is false: to let both browser and JS-based UI can read cookie
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)                                 // generated Csrf token after authentication
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)                   // invoke RequestValidatorFilter to check business logic before authentication
                .addFilterAfter(new JwtGeneratorFilter(new JwtTokenProvider()), BasicAuthenticationFilter.class)         // after authentication successfully in very first request to secure endpoint, generate JWT
                .addFilterBefore(new JwtValidatorFilter(), BasicAuthenticationFilter.class);       // for up-coming request, validate jwt before process authentication to prevent unnecessary requests

        //.csrf(AbstractHttpConfigurer::disable)

        http.requiresChannel(rcConfig -> rcConfig.anyRequest().requiresInsecure());      // only HTTP traffic

        http.authorizeHttpRequests(requests -> requests
//                    .requestMatchers("/myAccount").hasAuthority("VIEW_ACCOUNT")
//                    .requestMatchers("/myCard").hasAuthority("VIEW_CARDS")
//                    //   .requestMatchers("/myCard/{name}").access(new WebExpressionAuthorizationManager(" #name == authentication.name"))
//                    .requestMatchers("/myBalance").hasAuthority("VIEW_BALANCE")
//                .requestMatchers("/myLoans").hasAnyAuthority("VIEW_LOANS","VIEW_ACCOUNTS","VIEW_BALANCE")
                .requestMatchers("/myAccount").hasRole("USER")   // adding '*'  to make sure matcher requiring any parameters
                .requestMatchers("/myCard/{name}").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/myCard").hasRole("USER")
                .requestMatchers("/myBalance").hasAnyRole("ADMIN")
                .requestMatchers("/myLoans").hasAnyRole("USER")
                .requestMatchers("/user").authenticated()
                .requestMatchers("/hello", "/contact", "/register", "/invalidSession", "/notices","/apiLogin").permitAll());

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

    // this method aims for registering the customized AuthenticationProvider with the AuthenticationManger
    //  --> to provide manually authenticating
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        MyUsernamePasswordAuthenticationProvider authenticationProvider = new MyUsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);   // need config this for manual authenticating to make sure authenticated credentials are preserved after authenticating --> allow access in later
        return providerManager;
    }

}
