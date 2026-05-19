package uz.pdp.foodswift.model.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import jakarta.persistence.Id;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class IdEntity {
    @Id
    @Column(length = 36)
    private String id = UUID.randomUUID().toString();
}
