package uz.pdp.foodswift.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveredUpdateDto {
    private String fullName;
    private String foodName;
    private Double price;
    private Integer count;
    private Double totalPrice;
    private String phoneNumber;
    private OrderStatus orderStatus;
}
