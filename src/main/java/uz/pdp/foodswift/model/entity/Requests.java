package uz.pdp.foodswift.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "requests")
public class Requests extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String foodName;

    private Double price;
    private Integer count;
    private Double totalPrice;

    @Column(length = 20)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

}
