package uz.pdp.foodswift.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.foodswift.model.dto.AuthUserDto;
import uz.pdp.foodswift.model.dto.AuthUserSaveDto;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.model.entity.Role;
import uz.pdp.foodswift.repository.RoleRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthUserMapper {

    // Rol nomiga ko'ra Role entity topish uchun
    private final RoleRepository roleRepository;

    // ─── fromDto: SaveDto → Entity (yangi yaratish) ──────────────────
    public AuthUsers fromDto(AuthUserSaveDto dto) {
        AuthUsers users = new AuthUsers();
        users.setFullName(dto.getFullName());
        users.setPhoneNumber(dto.getPhoneNumber());
        users.setPassword(dto.getPassword());

        // Eski: users.setRole(Roles.valueOf(dto.getRole()));
        // Yangi: bazadan Role entity ni topib set qilamiz
        if (dto.getRoleName() != null) {
            Role role = roleRepository.findByName(dto.getRoleName())
                    .orElseThrow(() -> new RuntimeException(
                            "Bunday rol topilmadi: " + dto.getRoleName()));
            users.setRole(role);
        }

        return users;
    }

    // ─── fromDto: SaveDto → mavjud Entity (yangilash) ────────────────
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

        // Eski: authUsers.setRole(Roles.valueOf(dto.getRole()));
        // Yangi: rol nomi berilgan bo'lsa — bazadan topib yangilaymiz
        if (dto.getRoleName() != null) {
            Role role = roleRepository.findByName(dto.getRoleName())
                    .orElseThrow(() -> new RuntimeException(
                            "Bunday rol topilmadi: " + dto.getRoleName()));
            authUsers.setRole(role);
        }
    }

    // ─── toDto: Entity → Dto ro'yxati ────────────────────────────────
    public List<AuthUserDto> toDto(List<AuthUsers> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ─── toDto: Entity → Dto (bitta) ─────────────────────────────────
    public AuthUserDto toDto(AuthUsers save) {
        return AuthUserDto.builder()
                .id(save.getId())
                .fullName(save.getFullName())
                .phoneNumber(save.getPhoneNumber())
                .password(save.getPassword())
                // Eski: .role(save.getRole())   (enum)
                // Yangi: role entity dan faqat nomini olamiz
                .roleName(save.getRole() != null
                        ? save.getRole().getName()
                        : null)
                .build();
    }
}