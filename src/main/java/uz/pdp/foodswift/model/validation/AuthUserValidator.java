package uz.pdp.foodswift.model.validation;

import org.springframework.stereotype.Component;
import uz.pdp.foodswift.exception.BadRequestException;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.repository.AuthUserRepository;
import uz.pdp.foodswift.utils.Errors;

@Component
public class AuthUserValidator {
    private final AuthUserRepository repository;

    public AuthUserValidator(AuthUserRepository repository) {
        this.repository = repository;
    }

    public AuthUsers existsAndGet(String id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(
                        () -> new BadRequestException(Errors.s_NOT_FOUND.formatted("User"))
                );
    }
}
