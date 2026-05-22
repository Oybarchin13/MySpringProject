package uz.pdp.foodswift.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.model.entity.Permission;
import uz.pdp.foodswift.model.entity.Role;
import uz.pdp.foodswift.repository.AuthUserRepository;
import uz.pdp.foodswift.repository.PermissionRepository;
import uz.pdp.foodswift.repository.RoleRepository;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository       roleRepository;
    private final AuthUserRepository   authUserRepository;
    private final PasswordEncoder      passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        if (roleRepository.findByName("ADMIN").isPresent()) {
            log.info("DataInitializer: Ma'lumotlar allaqachon bor — o'tkazib yuborildi.");
            return;
        }

        log.info("DataInitializer: Boshlang'ich ma'lumotlar yuklanmoqda...");

        Permission foodRead    = getOrCreate("FOOD_READ");
        Permission foodCreate  = getOrCreate("FOOD_CREATE");
        Permission foodDelete  = getOrCreate("FOOD_DELETE");
        Permission orderCreate = getOrCreate("ORDER_CREATE");
        Permission orderRead   = getOrCreate("ORDER_READ");
        Permission userRead    = getOrCreate("USER_READ");
        Permission userDelete  = getOrCreate("USER_DELETE");


        Role adminRole = roleRepository.save(
                Role.builder()
                        .name("ADMIN")
                        .permissions(Set.of(
                                foodRead, foodCreate, foodDelete,
                                orderCreate, orderRead,
                                userRead, userDelete))
                        .build());

        Role userRole = roleRepository.save(
                Role.builder()
                        .name("FOYDALANUVCHI")
                        .permissions(Set.of(foodRead, orderCreate, orderRead))
                        .build());

        roleRepository.save(
                Role.builder()
                        .name("YETKAZUVCHI")
                        .permissions(Set.of(foodRead, orderRead))
                        .build());

        log.info("DataInitializer: 7 permission, 3 rol yaratildi.");

        if (authUserRepository.findByPhoneNumber("+998900000000").isEmpty()) {
            authUserRepository.save(
                    AuthUsers.builder()
                            .fullName("Super Admin")
                            .phoneNumber("+998900000000")
                            .password(passwordEncoder.encode("admin123"))
                            .role(adminRole)
                            .build());
            log.info("Admin: +998900000000 / admin123");
        }

        log.info("DataInitializer: Tayyor ✓");
    }

    private Permission getOrCreate(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(
                        Permission.builder().name(name).build()));
    }
}