package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.ApiResponse.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Model.User;
import com.example.tuwaiqcapstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = userService.addUser(user);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("ID: " + user.getId() + " already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid User user, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = userService.updateUser(id, user);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
        return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + id + " found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        boolean isDone = userService.deleteUser(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + id + " found"));
    }


    //EXTRA ENDPOINTS
    @PutMapping("/buy-product/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        int result = userService.buyProduct(userId, productId, merchantId);
        if (result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if (result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + productId + " found"));
        if (result == 1)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantId + " found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + merchantId + " doesn't sell the product with ID: " + productId));
        if (result ==3)
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + productId + " is out of stock"));
        if (result == 4)
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        return ResponseEntity.status(200).body(new ApiResponse("Product purchased successfully"));
    }

    @GetMapping("/get-username/{username}")
    public ResponseEntity<?> searchByUsername(@PathVariable String username){
        User user = userService.searchByUsername(username);
        if(user == null)
            return ResponseEntity.status(404).body(new ApiResponse("No user with username: " + username + " found"));
        return ResponseEntity.status(200).body(user);
    }

    @GetMapping("/get-cart/{userId}")
    public ResponseEntity<?> showCart(@PathVariable String userId){
        ArrayList<Product> cartProducts = userService.showCart(userId);
        if(cartProducts == null)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if(cartProducts.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("There is no products in the cart yet"));
        return ResponseEntity.status(200).body(cartProducts);
    }

    @PutMapping("/add-to-cart/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> addToCart(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId){
        int result = userService.addToCart(userId, productId, merchantId);
        if(result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if(result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + productId + " found"));
        if(result == 1)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantId + " found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: " + merchantId + " doesn't sell the product with ID: " + productId));
        if (result ==3)
            return ResponseEntity.status(400).body(new ApiResponse("Product with ID: " + productId + " is out of stock"));
        return ResponseEntity.status(200).body(new ApiResponse("Product added to the cart successfully"));
    }

    @DeleteMapping("/remove-from-cart/{userId}/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        int result = userService.removeFromCart(userId, productId);
        if (result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if (result == 0)
            return ResponseEntity.status(400).body(new ApiResponse("Cart is empty"));
        if (result == 2)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + productId + " found in cart"));
        return ResponseEntity.status(200).body(new ApiResponse("Product removed from cart successfully"));
    }

    @PutMapping("/clear-cart/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable String userId){
        boolean isDone = userService.clearCart(userId);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Cart cleared successfully"));
        return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
    }

    @PutMapping("/claim-reward/{userId}")
    public ResponseEntity<?> claimReward(@PathVariable String userId){
        int result = userService.claimReward(userId);
        if(result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if(result == 0)
            return ResponseEntity.status(400).body(new ApiResponse("Total spent must be at least 1000 to claim reward"));
        return ResponseEntity.status(200).body(new ApiResponse("Reward claimed successfully"));
    }

    @PutMapping("/checkout/{userId}")
    public ResponseEntity<?> checkout(@PathVariable String userId){
        int result = userService.checkout(userId);
        if(result == -2) //comes from checkout
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if(result == -3) //comes from checkout
            return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + userId + " doesn't have products in the cart"));
        if (result == 0) //comes from buy product
            return ResponseEntity.status(404).body(new ApiResponse("One cart product not found"));
        if (result == 1) //comes from buy product
            return ResponseEntity.status(404).body(new ApiResponse("One merchant not found"));
        if (result == 2) //comes from buy product
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with ID: doesn't sell the product with ID: "));
        if (result ==3) //comes from buy product
            return ResponseEntity.status(400).body(new ApiResponse("There is out of stock product in the cart"));
        if (result == 4) //comes from buy product
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        return ResponseEntity.status(200).body(new ApiResponse("Checkout successfully"));
    }

    @GetMapping("/cart-cost/{userId}")
    public ResponseEntity<?> calculateCartCost(@PathVariable String userId){
        double totalCost = userService.calculateCartCost(userId);
        if(totalCost == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if(totalCost == -2)
            return ResponseEntity.status(400).body(new ApiResponse("User with ID: " + userId + " doesn't have products in the cart"));
        return ResponseEntity.status(200).body(new ApiResponse("Cart total cost: " + totalCost));
    }
}
