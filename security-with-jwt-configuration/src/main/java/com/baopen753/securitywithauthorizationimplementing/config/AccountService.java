package com.baopen753.securitywithauthorizationimplementing.config;

import com.baopen753.securitywithauthorizationimplementing.model.Customer;
import com.baopen753.securitywithauthorizationimplementing.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor    // create a constructor with final properties
public class AccountService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findCustomerByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not found account with username: " + username));
        Set<GrantedAuthority> authorities =  customer.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toSet());
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }
}




