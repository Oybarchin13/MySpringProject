package uz.pdp.foodswift.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.foodswift.model.entity.base.BaseEntity;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "delivered_orders")
public class Delivered extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String foodName;

    private Double price;
    private Integer count;
    private Double totalPrice;

    @Column(length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
