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
        // 1. Joriy foydalanuvchini bazadan topamiz
        AuthUsers user = userRepository.findByPhoneNumber(currentPhoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + currentPhoneNumber));

        // 2. Agar telefon raqami o'zgarayotgan bo'lsa, yangi raqam band emasligini tekshiramiz
        if (!currentPhoneNumber.equals(newPhoneNumber)) {
            Optional<AuthUsers> existingUser = userRepository.findByPhoneNumber(newPhoneNumber);
            if (existingUser.isPresent()) {
                throw new RuntimeException("Bu telefon raqami allaqachon tizimda ro'yxatdan o'tgan!");
            }
            user.setPhoneNumber(newPhoneNumber);
        }

        // 3. Ism-sharifni yangilash
        user.setFullName(fullName);

        // 4. Parolni tekshirish (bo'sh bo'lmasa encode qilib saqlaydi, bo'sh bo'lsa teginmaydi)
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        // 5. O'zgarishlarni bazaga saqlash
        userRepository.save(user);
    }
}