package uz.pdp.foodswift.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id; // UUID o'rniga String qildik!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUsers user;

    private BigDecimal totalAmount; // Double o'rniga BigDecimal qildik!

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // String o'rniga Enum qildik!

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // Mijoz telefon raqami va manzili uchun maydonlar
    private String deliveryAddress;
    private String contactPhone;
}