package uz.pdp.foodswift.model.mapper;

import org.springframework.stereotype.Component;
import uz.pdp.foodswift.model.dto.AuthUserDto;
import uz.pdp.foodswift.model.dto.AuthUserSaveDto;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.model.entity.enums.Roles;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthUserMapper {
    public AuthUsers fromDto(AuthUserSaveDto dto) {
        AuthUsers users = new AuthUsers();
        users.setFullName(dto.getFullName());
        users.setPhoneNumber(dto.getPhoneNumber());
        users.setPassword(dto.getPassword());
        users.setRole(Roles.valueOf(dto.getRole()));
        return users;
    }
    public void fromDto(AuthUsers authUsers, AuthUserSaveDto dto) {
        if (dto.getFullName() != null) {
            authUsers.setFullName(dto.getFullName());
        }
        if (dto.getPhoneNumber() != null) {
            authUsers.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getPassword() != null) {
            authUsers.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            authUsers.setRole(Roles.valueOf(dto.getRole()));
        }
    }

    public List<AuthUserDto> toDto(List<AuthUsers> users) {
        if (users == null){
            return Collections.emptyList();
        }

        return users.stream().map(this::toDto).collect(Collectors.toList());
    }
    public AuthUserDto toDto(AuthUsers save) {

        return AuthUserDto.builder()
                .id(save.getId())
                .fullName(save.getFullName())
                .phoneNumber(save.getPhoneNumber())
                .password(save.getPassword())
                .role(save.getRole())
                .build();
    }
}
