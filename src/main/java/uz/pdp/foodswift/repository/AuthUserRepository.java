package uz.pdp.foodswift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.foodswift.model.entity.AddFood;
import uz.pdp.foodswift.model.entity.AuthUsers;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUsers, String> {

    @Query("""
            from AuthUsers r
            where (:search is null or r.fullName ilike ('%' || :search || '%'))
            """)

    Page<AuthUsers> findAllByCriteria(String search, Pageable pageable);

    Optional<AuthUsers> findByIdAndDeletedFalse(String id);

    Optional<AuthUsers> findByPhoneNumber(String phoneNumber);

}
