package uz.pdp.foodswift.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.foodswift.model.entity.CartItem;
import uz.pdp.foodswift.model.entity.AuthUsers;
import uz.pdp.foodswift.model.entity.AddFood;
import uz.pdp.foodswift.repository.AddFoodRepository;
import uz.pdp.foodswift.repository.AuthUserRepository;
import uz.pdp.foodswift.repository.CartItemRepository;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final AuthUserRepository userRepository;
    private final AddFoodRepository foodRepository;

    public CartService(CartItemRepository cartItemRepository, AuthUserRepository userRepository, AddFoodRepository foodRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    public void addToCart(String phoneNumber, String  foodId, Integer quantity) {
        AuthUsers user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + phoneNumber));

        AddFood food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Taom topilmadi! ID: " + foodId));

        CartItem cartItem = cartItemRepository.findByUserIdAndFoodId(user.getId(), foodId)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = CartItem.builder()
                    .user(user)
                    .food(food)
                    .quantity(quantity)
                    .build();
        }

        cartItemRepository.save(cartItem);
    }


    public List<CartItem> getCartItemsByPrice(String phoneNumber) {
        AuthUsers user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        return cartItemRepository.findAllByUserId(user.getId());
    }


    @Transactional
    public void updateItemQuantity(String phoneNumber, String foodId, Integer change) {
        AuthUsers user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        CartItem cartItem = cartItemRepository.findByUserIdAndFoodId(user.getId(), foodId)
                .orElseThrow(() -> new RuntimeException("Savatda bunday mahsulot topilmadi"));

        int newQuantity = cartItem.getQuantity() + change;

        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }
}