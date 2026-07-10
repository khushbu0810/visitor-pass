package com.example.ResidentMicroservice.config.security;

import com.example.ResidentMicroservice.model.User;
import lombok.Getter;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UserPrinciple implements UserDetails {

    private final String username;
    @Getter
    private final UUID id;
    private final String password;
    private final boolean accountStatus;
    private final String role;

    public UserPrinciple(User user) {
        username = user.getEmail();
        password = user.getPassword();
        accountStatus = user.getAccountStatus();
        this.id = user.getUserId();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ role));
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
        if (!accountStatus) {
            throw new LockedException("Account is locked");
        }
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
