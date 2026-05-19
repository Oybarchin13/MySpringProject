package uz.pdp.foodswift.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.foodswift.model.entity.base.BaseEntity;
import uz.pdp.foodswift.model.entity.enums.FoodCategory;

@Entity
@Table(name = "add_food")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddFood  extends BaseEntity {

    @Column(nullable = false)
    private String foodName;

    private String imgUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category;

}
