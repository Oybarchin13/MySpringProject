package uz.pdp.foodswift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.foodswift.model.dto.AddFoodDto;
import uz.pdp.foodswift.model.entity.AddFood;

import java.util.List;
import java.util.Optional;

public interface AddFoodRepository extends JpaRepository<AddFood, String> {

    Optional<AddFood> findByIdAndDeletedFalse(String id);
    List<AddFood> findAllByDeletedFalse();


}
