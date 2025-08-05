package com.trenkwalder.parttimeemployment.security;

import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("A user with this email was not found.")
                );
    }

    private String getRole(User user) {
        if (user.getRole() == null) {
            return "USER";
        }
        return user.getRole();
    }
}
