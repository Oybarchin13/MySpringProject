package uz.pdp.foodswift.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.foodswift.criteria.BaseCriteria;
import uz.pdp.foodswift.exception.BadRequestException;
import uz.pdp.foodswift.model.dto.*;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.model.entity.Role;
import uz.pdp.foodswift.model.mapper.AuthUserMapper;
import uz.pdp.foodswift.model.validation.AuthUserValidator;
import uz.pdp.foodswift.repository.AuthUserRepository;
import uz.pdp.foodswift.repository.RoleRepository;
import uz.pdp.foodswift.service.base.AbstractService;
import uz.pdp.foodswift.service.base.CrudService;
import uz.pdp.foodswift.utils.Errors;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AuthUserService
        extends AbstractService<AuthUserRepository, AuthUserMapper, AuthUserValidator>
        implements CrudService<AuthUserDto, AuthUserSaveDto, AuthUserSaveDto, BaseCriteria, String> {

    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected AuthUserService(AuthUserRepository repository,
                              AuthUserMapper mapper,
                              AuthUserValidator validator,
                              RoleRepository roleRepository) {
        super(repository, mapper, validator);
        this.roleRepository = roleRepository;
    }

    @Override
    public AuthUserDto create(AuthUserSaveDto dto) {
        dto.validator(); // validation mantiqi (agar ichida parol tekshiruvi bo'lsa)

        // 1. DTO dan entity ga o'giramiz
        AuthUsers authUsers = mapper.fromDto(dto);

        // 2. Parolni majburiy ravishda encode qilamiz
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Yangi foydalanuvchi uchun parol majburiy!");
        }
        authUsers.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 3. Formadan kelgan roleName bo'yicha bazadan Rolni topib biriktiramiz
        if (dto.getRoleName() != null) {
            Role role = roleRepository.findByName(dto.getRoleName())
                    .orElseThrow(() -> new BadRequestException("Tanlangan rol tizimda topilmadi: " + dto.getRoleName()));
            authUsers.setRole(role);
        }

        return mapper.toDto(repository.save(authUsers));
    }

    @Override
    public AuthUserDto update(String id, AuthUserSaveDto dto) {
        // 1. Bazadan eski foydalanuvchini to'liq holatda yuklab olamiz
        AuthUsers authUsers = validator.existsAndGet(id);

        // Bazadagi eski parolni va eski rolni vaqtincha saqlab turamiz
        String oldPassword = authUsers.getPassword();
        Role oldRole = authUsers.getRole();

        // 2. DTO dagi yangi ma'lumotlarni entity ga o'tkazamiz
        mapper.fromDto(authUsers, dto);

        // 3. Parol o'zgarishini tekshirish logikasi:
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            // Agar input bo'sh bo'lmasa -> yangi parolni encode qilib saqlaymiz
            authUsers.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            // Agar input bo'sh bo'lsa -> bazadagi eski parol o'z holaticha qoladi
            authUsers.setPassword(oldPassword);
        }

        // 4. Rol yangilanishini tekshirish logikasi:
        if (dto.getRoleName() != null && !dto.getRoleName().trim().isEmpty()) {
            Role newRole = roleRepository.findByName(dto.getRoleName())
                    .orElseThrow(() -> new BadRequestException("Tanlangan rol topilmadi: " + dto.getRoleName()));
            authUsers.setRole(newRole);
        } else {
            authUsers.setRole(oldRole);
        }

        return mapper.toDto(repository.saveAndFlush(authUsers));
    }

    @Override
    public AuthUserDto get(String id) {
        AuthUsers entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + id));
        return mapper.toDto(entity);
    }

    @Override
    public DataList<List<AuthUserDto>> getAll(BaseCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize());
        Page<AuthUsers> page = repository.findAllByCriteria(criteria.getSearch(), pageable);
        List<AuthUserDto> users = mapper.toDto(page.getContent());
        return new DataList<>(users, page.getTotalElements(), (long) page.getTotalPages());
    }

    @Override
    public void delete(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BadRequestException(Errors.CANT_DELETE_ROLE);
        }
    }

    public Optional<AuthUsers> findByPhoneNumber(String phone) {
        return repository.findByPhoneNumber(phone);
    }

    public void register(AuthUserRegisterDto dto) {
        if (repository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Bu telefon raqami allaqachon ro'yxatdan o'tgan!");
        }

        Role defaultRole = roleRepository.findByName("FOYDALANUVCHI")
                .orElseThrow(() -> new RuntimeException("Tizimda 'FOYDALANUVCHI' roli topilmadi."));

        AuthUsers authUser = AuthUsers.builder()
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(defaultRole)
                .build();

        repository.save(authUser);
    }
}