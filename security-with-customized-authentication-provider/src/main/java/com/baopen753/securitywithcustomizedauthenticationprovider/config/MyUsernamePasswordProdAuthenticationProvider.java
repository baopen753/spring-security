package com.baopen753.securitywithcustomizedauthenticationprovider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component       // define this class as a singleton bean in application context
@Profile("prod") // this bean is only activated in 'prod' profile
public class MyUsernamePasswordProdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // in this Profile 'prod', this method only authenticates the username regardless of password in order to improve testing performance
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // get credentials entered by user
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // get UserDetail stored in database
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//        // check match password
//        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
//        } else {
//            throw new BadCredentialsException("Invalid password");
//        }
    }


    /**
     * This method is used to check which types of authentication is going to be supported
     *
     * @param authentication
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
