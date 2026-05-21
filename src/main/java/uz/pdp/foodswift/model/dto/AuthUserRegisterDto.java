package uz.pdp.foodswift.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserRegisterDto {

//    @NotBlank(message = "Ism va familiya bo'sh bo'lmasligi kerak")
//    @Size(min = 3, max = 50, message = "Ism va familiya 3 dan 50 gacha belgidan iborat bo'lishi kerak")
    private String fullName;

//    @NotBlank(message = "Telefon raqami kiritilishi shart")
//    @Pattern(regexp = "^\\+998\\d{9}$", message = "Telefon raqami formati noto'g'ri (Masalan: +998901234567)")
    private String phoneNumber;

//    @NotBlank(message = "Parol kiritilishi shart")
//    @Size(min = 6, message = "Parol kamida 6 ta belgidan iborat bo'lishi kerak")
    private String password;
}