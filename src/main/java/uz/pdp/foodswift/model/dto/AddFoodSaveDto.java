package uz.pdp.foodswift.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.foodswift.exception.BadRequestException;
import uz.pdp.foodswift.model.entity.enums.FoodCategory;
import uz.pdp.foodswift.utils.Errors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddFoodSaveDto {
    private String foodName;
    private String imgUrl;
    private String description;
    private Double price;
    private FoodCategory category;


    public AddFoodSaveDto validator() {
        if (foodName == null || foodName.trim().isEmpty()) {
            throw new BadRequestException(Errors.INVALID_FOOD_NAME);
        }
        if (imgUrl == null || imgUrl.trim().isEmpty()) {
            throw new BadRequestException(Errors.INVALID_IMAGE_URL);
        }
        if (description == null || description.trim().isEmpty()) {
            throw new BadRequestException(Errors.INVALID_DESCRIPTION);
        }
        if (price == null || price <= 0) {
            throw new BadRequestException(Errors.INVALID_PRICE);
        }
        if (category == null) {
            throw new BadRequestException(Errors.INVALID_CATEGORY);
        }
        return this;
    }
}
