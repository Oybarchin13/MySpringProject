package uz.pdp.foodswift.model.validation;

import org.springframework.stereotype.Component;
import uz.pdp.foodswift.exception.BadRequestException;
import uz.pdp.foodswift.model.dto.AddFoodDto;
import uz.pdp.foodswift.model.entity.AddFood;
import uz.pdp.foodswift.repository.AddFoodRepository;
import uz.pdp.foodswift.repository.AuthUserRepository;
import uz.pdp.foodswift.utils.Errors;

@Component
public class AddFoodValidator {

    private final AddFoodRepository repository;


    public AddFoodValidator(AddFoodRepository repository) {
        this.repository = repository;
    }

    public AddFood exitsAndGet(String id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(
                        () -> new BadRequestException(Errors.s_NOT_FOUND.formatted("Add food"))
                );
    }
}
