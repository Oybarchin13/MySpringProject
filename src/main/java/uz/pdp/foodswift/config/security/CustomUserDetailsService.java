package uz.pdp.foodswift.config.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.service.AuthUserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
    private final AuthUserService authUserService; // Siz yuqoridagi kodni yozgan service klassingiz

    public CustomUserDetailsService(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String phoneNumber = username;
        AuthUsers user = authUserService.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Telefon raqam yoki foydalanuvchi topilmadi: " + phoneNumber));

        return User.withUsername(user
                        .getPhoneNumber()) // <--- MUHIM: Bu yerga baribir 'withUsername' yoziladi, lekin ichiga foydalanuvchining telefon raqami beriladi!
//                .username(user.getFullName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
