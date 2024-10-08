package com.baopen753.securitywithauthorizationimplementing.controller;

import com.baopen753.securitywithauthorizationimplementing.constant.ApplicationConstants;
import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import com.baopen753.securitywithauthorizationimplementing.model.LoginRequestDto;
import com.baopen753.securitywithauthorizationimplementing.model.LoginResponseDto;
import com.baopen753.securitywithauthorizationimplementing.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

public class UserController {

    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment environment;

    @Autowired
    public UserController(CustomerRepository customerRepository, AuthenticationManager authenticationManager, Environment environment) {
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.environment = environment;
    }


    @GetMapping("/user")
    public ResponseEntity<Customer> getUserAfterLogin(Authentication authentication) {
        Optional<Customer> user = customerRepository.findCustomerByEmail(authentication.getName());
        return ResponseEntity.ok(user.orElse(null));
    }

    // this endpoint is used for process manual authenticating
    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginRequestDto loginRequestDto) {

        String jwt = "";

        // forming entered credential from request into unauthenticated Authentication object
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDto.username(), loginRequestDto.password());

        // put into AuthNManager to process authenticating
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if (authenticationResponse.isAuthenticated()) {
            String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            jwt = produceJwt(authenticationResponse, secretKey);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header(ApplicationConstants.JWT_HEADER, jwt)
                .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), jwt));
    }

    private String produceJwt(Authentication authenticationResponse, SecretKey secretKey) {
        String jwt = Jwts.builder()
                .issuer("baopen")
                .subject("JWT token")
                .claim("username", authenticationResponse.getName())
                .claim("authorities", authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 30000000))
                .signWith(secretKey)
                .compact();
        return jwt;
    }

}
