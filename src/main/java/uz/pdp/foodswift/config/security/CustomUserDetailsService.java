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

        // 1. Foydalanuvchini telefon raqami (username) orqali topish
        AuthUsers user = authUserService.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Foydalanuvchi topilmadi: " + username));

        // 2. Authorities ro'yxatini yasash
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 2a. Rolni "ROLE_" prefiksi bilan qo'shamiz
        if (user.getRole() != null) {
            String roleName = "ROLE_" + user.getRole().getName().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(roleName));

            // 2b. Rolga biriktirilgan barcha permission larni qo'shamiz
            if (user.getRole().getPermissions() != null) {
                user.getRole()
                        .getPermissions()
                        .forEach(permission ->
                                authorities.add(new SimpleGrantedAuthority(permission.getName()))
                        );
            }
        }

        // 3. Biz yaratgan CustomUserDetails obyektini qaytaramiz
        // Bu orqali Thymeleaf ichida principal.fullName xatoliksiz ishlaydi
        return new CustomUserDetails(user, authorities);
    }
}