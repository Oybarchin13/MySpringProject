package uz.pdp.foodswift.service;

import lombok.NonNull;
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
import uz.pdp.foodswift.model.entity.AddFood;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.model.entity.enums.Roles;
import uz.pdp.foodswift.model.mapper.AuthUserMapper;
import uz.pdp.foodswift.model.validation.AuthUserValidator;
import uz.pdp.foodswift.repository.AuthUserRepository;
import uz.pdp.foodswift.service.base.AbstractService;
import uz.pdp.foodswift.service.base.CrudService;
import uz.pdp.foodswift.utils.Errors;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AuthUserService extends AbstractService<AuthUserRepository, AuthUserMapper, AuthUserValidator>
        implements CrudService<AuthUserDto, AuthUserSaveDto, AuthUserSaveDto, BaseCriteria, String>
{
    protected AuthUserService(AuthUserRepository repository, AuthUserMapper mapper, AuthUserValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public AuthUserDto create(AuthUserSaveDto dto) {
        dto.validator();
        AuthUsers authUsers = mapper.fromDto(dto);
        return mapper.toDto(repository.save(authUsers));
    }

    public Optional<AuthUsers> findByPhoneNumber(@NonNull String phone){
       return repository.findByPhoneNumber(phone);
    }


    @Override
    public AuthUserDto update(String id, AuthUserSaveDto dto) {
        AuthUsers authUsers = validator.existsAndGet(id);
        mapper.fromDto(authUsers, dto);
        AuthUsers save = repository.saveAndFlush(authUsers);
        return mapper.toDto(save);
    }

    @Override
    public AuthUserDto get(String id) {
        AuthUsers entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ushbu ID ga ega foydalanuvchi topilmadi: " + id));
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




    @Autowired
    private PasswordEncoder passwordEncoder;

    // Yangi metod qo'shamiz:
    public void register(AuthUserRegisterDto dto) {
        // 1. Telefon raqam band emasligini tekshirish
        if (repository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Bu telefon raqami allaqachon ro'yxatdan o'tgan!");
        }

        // 2. Yangi foydalanuvchi obyektini yasash
        AuthUsers authUser = AuthUsers.builder()
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .password(passwordEncoder.encode(dto.getPassword())) // <--- PAROL SHIFRLANDI
                .role(Roles.FOYDALANUVCHI) // <--- To'g'ridan-to'g'ri Enum berildi (yoki Roles.USER)
                .build();

        // 3. Bazaga saqlash
        repository.save(authUser);
    }

}
