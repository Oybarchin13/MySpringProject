package uz.pdp.foodswift.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.foodswift.model.entity.AuthUsers;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final AuthUsers authUser;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(AuthUsers authUser, Collection<? extends GrantedAuthority> authorities) {
        this.authUser = authUser;
        this.authorities = authorities;
    }

    public String getFullName() {
        return authUser.getFullName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getPhoneNumber();
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