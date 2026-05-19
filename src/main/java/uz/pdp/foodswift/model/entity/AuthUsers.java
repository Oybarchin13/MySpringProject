package uz.pdp.foodswift.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.foodswift.model.entity.base.BaseEntity;
import uz.pdp.foodswift.model.entity.enums.Roles;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "auth_users")
public class AuthUsers extends BaseEntity{
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id")
    private Roles role;
}
