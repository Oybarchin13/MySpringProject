package uz.pdp.foodswift.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.foodswift.model.dto.*;
import uz.pdp.foodswift.model.entity.AddFood;
import uz.pdp.foodswift.model.mapper.AddFoodMapper;
import uz.pdp.foodswift.model.validation.AddFoodValidator;
import uz.pdp.foodswift.repository.AddFoodRepository;
import uz.pdp.foodswift.service.base.AbstractService;

import java.util.List;


@Service
public class AddFoodService extends AbstractService<AddFoodRepository, AddFoodMapper, AddFoodValidator>

{
    public AddFoodService(AddFoodRepository repository, AddFoodMapper mapper, AddFoodValidator validator) {
        super(repository, mapper, validator);
    }

    public AddFoodDto create(AddFoodSaveDto dto) {
        dto.validator();
        AddFood addFood = mapper.fromDto(dto);
        return mapper.toDto(repository.save(addFood));
    }

    public AddFoodDto update(String id, AddFoodUpdateDto dto) {
        dto.validator();
        AddFood addFood = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        mapper.fromDto(addFood, dto);
        repository.save(addFood);
        return mapper.toDto(addFood);
    }

    @Transactional
    public void delete(String id){
        AddFood addFood = validator.exitsAndGet(id);
        addFood.setDeleted(true);
    }





    public List<AddFoodDto> getAll() {
        List<AddFood> allByDeletedFalse = repository.findAllByDeletedFalse();
        return mapper.toDto(allByDeletedFalse);
    }


    public AddFood get(String id) {
        return validator.exitsAndGet(id);
    }
}
