package uz.pdp.foodswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.foodswift.model.entity.AddFood;

public interface DeliveredRepository extends JpaRepository<AddFood, Integer> {
}
