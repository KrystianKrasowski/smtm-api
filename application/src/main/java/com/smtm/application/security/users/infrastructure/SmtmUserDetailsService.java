package com.smtm.application.security.users.infrastructure;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// In order to make Spring Security magic work properly, it seems that is should be the only implementation of UserServiceDetails interface
// It cannot be exposed as @Bean method with parameters.
@Service
public class SmtmUserDetailsService implements UserDetailsService {

    private final DbUsersRepository dbUsersRepository;

    public SmtmUserDetailsService(DbUsersRepository dbUsersRepository) {
        this.dbUsersRepository = dbUsersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return dbUsersRepository.findByEmail(username);
    }
}
