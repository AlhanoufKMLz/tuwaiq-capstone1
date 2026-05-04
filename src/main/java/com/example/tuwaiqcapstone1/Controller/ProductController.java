package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.ApiResponse.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getProducts(){
        return ResponseEntity.status(200).body(productService.getProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        int result = productService.addProduct(product);
        if(result == 1)
            return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
        if(result == 0)
            return ResponseEntity.status(200).body(new ApiResponse("No category with ID: " + product.getCategoryId() + " found"));
        return ResponseEntity.status(400).body(new ApiResponse("ID: " + product.getId() + " already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody @Valid Product product, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = productService.updateProduct(id, product);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No product with ID: " + id + " found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        boolean isDone = productService.deleteProduct(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No product with ID: " + id + " found"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String categoryId){
        ArrayList<Product> categoryProducts = productService.getProductsByCategory(categoryId);
        if(categoryProducts == null)
            return ResponseEntity.status(404).body(new ApiResponse("No category with ID: " + categoryId + " found"));
        if(categoryProducts.isEmpty())
            return ResponseEntity.status(400).body(new ApiResponse("Category with ID: " + categoryId + " doesn't have any products yet"));
        return ResponseEntity.status(200).body(categoryProducts);
    }

}
