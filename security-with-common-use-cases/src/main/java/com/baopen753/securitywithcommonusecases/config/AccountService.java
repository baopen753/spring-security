package com.baopen753.securitywithcommonusecases.config;

import com.baopen753.securitywithcommonusecases.model.Account;
import com.baopen753.securitywithcommonusecases.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor    // create a constructor with final properties
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * @param username the username identifying the user whose data is required.
     *  @return
     *  @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found account with username: " + username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRole()));
        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}




