package com.baopen753.securitywithdatabaseconfig.service;

import com.baopen753.securitywithdatabaseconfig.model.Account;
import com.baopen753.securitywithdatabaseconfig.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;


    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User details cannot find for the username: " + username));
        List<GrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(account.getRole()));
        return new User(account.getUsername(), account.getPassword(), grantedAuthorityList);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}
