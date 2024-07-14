package com.takrimul.basic_crud_app.service;

import com.takrimul.basic_crud_app.model.RegisteredUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RegisteredUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public RegisteredUserDetails(RegisteredUser registeredUser) {
        this.username = registeredUser.getUsername();
        this.password = registeredUser.getPassword();
        this.authorities = Arrays.stream(registeredUser.getRole().split(","))
                .map(String::trim)
                .map(role -> "ROLE_".startsWith(role) ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Ensure USER authority is always present
        if (this.authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // Log the authorities for debugging
        System.out.println("User " + username + " authorities: " + this.authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
