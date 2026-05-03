package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.ApiResponse.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Category;
import com.example.tuwaiqcapstone1.Model.User;
import com.example.tuwaiqcapstone1.Service.CategoryService;
import com.example.tuwaiqcapstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(400).body(new ApiResponse("No user with ID: " + id + " found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        boolean isDone = userService.deleteUser(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No user with ID: " + id + " found"));
    }

    //EXTRA ENDPOINTS
    public ResponseEntity<?> buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId){
        int result = userService.buyProduct(userId, productId, merchantId);
        if(result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID: " + userId + " found"));
        if(result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + productId + " found"));
        if(result == 1)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantId + " found"));
        if(result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));

        return ResponseEntity.status(200).body(new ApiResponse("Product purchased successfully"));
    }
}
