package uz.pdp.foodswift.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserUpdateDto {
    private String fullName;
    private String phoneNumber;
    private String password;
}
