package com.baopen753.authserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;


@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

    // configure filter chain for Protocol Endpoints
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher()).with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer.oidc(Customizer.withDefaults())    // Enable OpenID Connect 1.0
                ).authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())

                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))

                // Accepts access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }


    // configure filter chain for Authentication
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());

        return http.build();
    }



    // managing client credentials
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient clientCredClient = RegisteredClient.withId(UUID.randomUUID().toString())

                // config client credential
                .clientId("baopenapi")
                .clientSecret("{noop}VxubZgAXyyTq9lGjj3qGvWNsHtE4SqTq")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)   // the client credential sent via HTTP Basic Authorization header
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)            // specify grant type
                .scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, "CUSTOMER", "MANAGER")))
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())
                .build();


        RegisteredClient authCodeClient = RegisteredClient.withId(UUID.randomUUID().toString())

                // config client credential
                .clientId("baopenclient")
                .clientSecret("{noop}Qw3rTy6UjMnB9zXcV2pL0sKjHn5TxQqB")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)   // the client credential sent via HTTP Request Body
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)           // specify grant type
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)                // specify refresh token
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, OidcScopes.EMAIL)))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(8))
                        .reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())
                .build();


        RegisteredClient pkceClient = RegisteredClient.withId(UUID.randomUUID().toString())

                // config public client credential
                .clientId("baopenpublicclient")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)                 // the client credential never shared any secret
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)           // specify grant type
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)                // specify refresh token
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.EMAIL, OidcScopes.OPENID)))
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(8))
                        .reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(clientCredClient, authCodeClient, pkceClient);
    }

    // for signing access tokens
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    // is responsible for generating a public key & private key during the startup
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    // for decoding signed access tokens
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // to configure Spring Authorization Server
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    // customization:  adding 'roles' claims into JWT token by copying from roles value from 'scope'
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                context.getClaims().claims((claims) -> {
                    Set<String> roles = context.getClaims().build().getClaim("scope");
                    claims.put("roles", roles);
                });
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
