package uz.pdp.foodswift.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.service.AuthUserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserService authUserService;

    public CustomUserDetailsService(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AuthUsers user = authUserService.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Foydalanuvchi topilmadi: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user.getRole() != null) {
            String roleName = "ROLE_" + user.getRole().getName().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(roleName));

            if (user.getRole().getPermissions() != null) {
                user.getRole()
                        .getPermissions()
                        .forEach(permission ->
                                authorities.add(new SimpleGrantedAuthority(permission.getName()))
                        );
            }
        }

        return new CustomUserDetails(user, authorities);
    }
}