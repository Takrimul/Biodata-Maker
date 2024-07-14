package com.takrimul.basic_crud_app.service;

import com.takrimul.basic_crud_app.model.RegisteredUser;
import com.takrimul.basic_crud_app.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class RegisteredUserUserDetailsService implements UserDetailsService {
    @Autowired
    RegisteredUserRepository repository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RegisteredUser> registeredUser = repository.findByUsername(username);
        return registeredUser.map(RegisteredUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

    }
//    public String registerUser(RegisteredUser registeredUser) {
//        registeredUser.setPassword(passwordEncoder.encode(registeredUser.getPassword()));
//        repository.save(registeredUser);
//        return "User registered successfully";
//    }
}
