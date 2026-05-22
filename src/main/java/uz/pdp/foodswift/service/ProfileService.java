package uz.pdp.foodswift.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.repository.AuthUserRepository; // yoki sizdagi AuthUserRepository

import java.util.Optional;

@Service
public class ProfileService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(AuthUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void updateProfile(String currentPhoneNumber, String fullName, String newPhoneNumber, String newPassword) {
        AuthUsers user = userRepository.findByPhoneNumber(currentPhoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + currentPhoneNumber));

        if (!currentPhoneNumber.equals(newPhoneNumber)) {
            Optional<AuthUsers> existingUser = userRepository.findByPhoneNumber(newPhoneNumber);
            if (existingUser.isPresent()) {
                throw new RuntimeException("Bu telefon raqami allaqachon tizimda ro'yxatdan o'tgan!");
            }
            user.setPhoneNumber(newPhoneNumber);
        }

        user.setFullName(fullName);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
    }
}