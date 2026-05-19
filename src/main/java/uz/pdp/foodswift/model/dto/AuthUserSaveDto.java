package uz.pdp.foodswift.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.foodswift.utils.Errors;
import uz.pdp.foodswift.exception.BadRequestException;
import uz.pdp.foodswift.model.entity.enums.Roles;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserSaveDto {
    private String fullName;
    private String phoneNumber;
    private String password;
    private String role;

    public AuthUserSaveDto validator(){
        if(fullName == null || fullName.isEmpty()){
            throw new BadRequestException(Errors.NAME_IS_REQUIRED);
        }
        if(phoneNumber == null || phoneNumber.isEmpty()){
            throw new BadRequestException(Errors.PHONENUMBER_IS_REQUIRED);
        }
        return this;
    }
}
