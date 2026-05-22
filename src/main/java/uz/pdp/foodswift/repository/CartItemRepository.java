package uz.pdp.foodswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.foodswift.model.entity.CartItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Agar ID turi UUID bo'lsa. Agar Long bo'lsa, Long ishlating.

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> { // JpaRepository<Entity, ID_Turi>

    // 1. Savatda ushbu foydalanuvchida bu taom borligini tekshirish (ID turlari moslandi)
    Optional<CartItem> findByUserIdAndFoodId(String userId, String foodId);

    // 2. SAVAT SAHIFASI UCHUN: Xatolikni tuzatuvchi va taomlarni darhol yuklovchi JOIN FETCH
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.food WHERE ci.user.id = :userId")
    List<CartItem> findAllByUserId(@Param("userId") String userId);
}