package uz.pdp.foodswift.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.pdp.foodswift.model.entity.enums.FoodCategory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddFoodDto {
    private String id;
    private String foodName;
    private String imgUrl;
    private String description;
    private Double price;
    private FoodCategory category;
}
