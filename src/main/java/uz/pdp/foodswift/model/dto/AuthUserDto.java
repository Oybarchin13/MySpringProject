package uz.pdp.foodswift.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import uz.pdp.foodswift.model.entity.enums.Roles;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserDto {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private Roles role;
}
