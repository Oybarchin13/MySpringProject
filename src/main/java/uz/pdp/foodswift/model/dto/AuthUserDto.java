package uz.pdp.foodswift.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserDto {

    private String id;
    private String fullName;
    private String phoneNumber;
    private String password;

    // Eski: private Roles role;  (enum)
    // Yangi: rolni String sifatida — "ADMIN", "FOYDALANUVCHI"
    // Yoki RoleDto sifatida — permission larni ham ko'rsatish kerak bo'lsa
    private String roleName;

    // Ixtiyoriy: Agar UI da permission larni ham ko'rsatish kerak bo'lsa
     private List<String> permissions;
}