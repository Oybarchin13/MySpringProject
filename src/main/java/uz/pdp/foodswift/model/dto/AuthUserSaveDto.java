package uz.pdp.foodswift.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserSaveDto {

    private String fullName;
    private String phoneNumber;
    private String password;

    // Eski: private String role; (Roles.valueOf() bilan parse qilinardi)
    // Yangi: role nomi — "ADMIN", "FOYDALANUVCHI" kabi string
    // Mapper da RoleRepository orqali Role entity topiladi
    private String roleName;

    public void validator() {
        if (fullName == null || fullName.isBlank()) {
            throw new RuntimeException("To'liq ism kiritilishi shart!");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new RuntimeException("Telefon raqam kiritilishi shart!");
        }
        if (password == null || password.length() < 4) {
            throw new RuntimeException("Parol kamida 4 ta belgidan iborat bo'lishi kerak!");
        }
    }
}