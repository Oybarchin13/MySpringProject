package uz.pdp.foodswift.model.entity;

import lombok.*;
import uz.pdp.foodswift.model.entity.base.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Statistics extends BaseEntity {
    private String name;
}
