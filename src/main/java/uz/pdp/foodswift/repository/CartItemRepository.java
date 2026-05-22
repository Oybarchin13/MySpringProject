package uz.pdp.foodswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.foodswift.model.entity.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    Optional<CartItem> findByUserIdAndFoodId(String userId, String foodId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.food WHERE ci.user.id = :userId")
    List<CartItem> findAllByUserId(@Param("userId") String userId);
}