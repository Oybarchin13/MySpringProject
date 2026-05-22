package uz.pdp.foodswift.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.foodswift.model.entity.base.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "role")
@Entity
@Table(name = "auth_users")
public class AuthUsers extends BaseEntity {

    private String fullName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    /**
     * Eski: @Enumerated Roles role
     * Yangi: @ManyToOne Role entity ga bog'lanish
     * FetchType.EAGER — loadUserByUsername da permission lar ham yuklansin
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
