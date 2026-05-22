package uz.pdp.foodswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.foodswift.model.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, String> {

    /**
     * Permission nomiga ko'ra qidirish.
     * getOrCreatePermission() metodida ishlatiladi.
     */
    Optional<Permission> findByName(String name);
}