package com.registration.registration.details;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.registration.registration.objects.Church;

public abstract class AbstractDetails implements UserDetails{

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    
    public abstract String getPassword();
    
    public abstract String getUsername();

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
    
    public abstract String getFirstName();
    public abstract String getLastName();
    public abstract String getFullName();
    public abstract String getEmail();
    public abstract String getSex();
    public abstract String getRole();
    public abstract Church getChurch();
    public abstract boolean isApproved();

}
