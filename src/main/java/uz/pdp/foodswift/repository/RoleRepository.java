package uz.pdp.foodswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.foodswift.model.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Rol nomiga ko'ra qidirish.
     * DataInitializer da ishlatiladi: "ADMIN" bormi yo'qmi — tekshirish.
     */
    Optional<Role> findByName(String name);
}