package com.example.academic_system.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final Pengguna pengguna;

    public CustomUserDetails(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public Pengguna getUser() {
        return pengguna;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> pengguna.getPeran());
    }

    @Override
    public String getPassword() {
        return pengguna.getKataSandi();
    }

    @Override
    public String getUsername() {
        return pengguna.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
