package uz.pdp.foodswift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.foodswift.model.entity.Order;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.items i " +
            "LEFT JOIN FETCH i.food " +
            "WHERE o.user.id = :userId " +
            "ORDER BY o.createdAt DESC")
    List<Order> findAllByUserIdWithItemsOrderByCreatedAtDesc(@Param("userId") String userId);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.items i " +
            "LEFT JOIN FETCH i.food " +
            "WHERE o.status = :status " +
            "ORDER BY o.createdAt DESC")
    List<Order> findAllByStatusOrderByCreatedAtDesc(@Param("status") OrderStatus status);
}