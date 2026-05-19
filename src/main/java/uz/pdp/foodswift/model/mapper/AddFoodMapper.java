package uz.pdp.foodswift.model.mapper;

import org.springframework.stereotype.Component;
import uz.pdp.foodswift.model.dto.AddFoodDto;
import uz.pdp.foodswift.model.dto.AddFoodSaveDto;
import uz.pdp.foodswift.model.dto.AddFoodUpdateDto;
import uz.pdp.foodswift.model.entity.AddFood;

import java.util.List;

@Component
public class AddFoodMapper {
    public AddFood fromDto(AddFoodSaveDto dto) {
        if(dto == null) return null;
        AddFood addFood = new AddFood();
        addFood.setFoodName(dto.getFoodName());
        addFood.setImgUrl(dto.getImgUrl());
        addFood.setDescription(dto.getDescription());
        addFood.setPrice(dto.getPrice());
        addFood.setCategory(dto.getCategory());
        return addFood;
    }

    public AddFood fromDto(AddFood addFood, AddFoodUpdateDto dto) {
        if(dto == null) return null;
        addFood.setFoodName(dto.getFoodName());
        addFood.setImgUrl(dto.getImgUrl());
        addFood.setDescription(dto.getDescription());
        addFood.setPrice(dto.getPrice());
        addFood.setCategory(dto.getCategory());
        return addFood;
    }



    public List<AddFoodDto> toDto(List<AddFood> all) {
        return all.stream().map(this::toDto).toList();
    }


    public AddFoodDto toDto(AddFood save) {
        return AddFoodDto.builder()
                .id(save.getId())
                .foodName(save.getFoodName())
                .imgUrl(save.getImgUrl())
                .description(save.getDescription())
                .price(save.getPrice())
                .category(save.getCategory())
                .build();
    }
}
