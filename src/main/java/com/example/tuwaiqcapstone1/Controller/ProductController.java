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
        if(result == -1)
            return ResponseEntity.status(400).body(new ApiResponse("ID: " + product.getId() + " already used"));
        if(result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No category with ID: " + product.getCategoryId() + " found"));
        if(result == -2)
            return ResponseEntity.status(400).body(new ApiResponse("Times purchased must be 0 when adding a product"));
        return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody @Valid Product product, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        int result = productService.updateProduct(id, product);
        if(result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + id + " found"));
        if(result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No category with ID: " + product.getCategoryId() + " found"));
        return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        boolean isDone = productService.deleteProduct(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
        return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + id + " found"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-name/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name){
        Product product = productService.searchByName(name);
        if(product == null)
            return ResponseEntity.status(404).body(new ApiResponse("No product with name: " + name + " found"));
        return ResponseEntity.status(200).body(product);
    }

    @GetMapping("/get-category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String categoryId){
        ArrayList<Product> categoryProducts = productService.getProductsByCategory(categoryId);
        if(categoryProducts == null)
            return ResponseEntity.status(404).body(new ApiResponse("No category with ID: " + categoryId + " found"));
        if(categoryProducts.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("Category with ID: " + categoryId + " doesn't have any products yet"));
        return ResponseEntity.status(200).body(categoryProducts);
    }

    @GetMapping("/get-price-range/{min}/{max}")
    public ResponseEntity<?> getProductsByPriceRange(@PathVariable double min, @PathVariable double max){
        ArrayList<Product> productsInRange = productService.getProductsByPriceRange(min, max);
        if(productsInRange == null)
            return ResponseEntity.status(400).body(new ApiResponse("Min must be positive number and max must be larger than min"));
        if(productsInRange.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("No products with price in the range: (" + min + ", " + max + ")"));
        return ResponseEntity.status(200).body(productsInRange);
    }

    @GetMapping("/get-sorted/{order}")
    public ResponseEntity<?> sortByPrice(@PathVariable String order){
        ArrayList<Product> sortedProducts = productService.sortByPrice(order);
        if(sortedProducts == null)
            return ResponseEntity.status(400).body(new ApiResponse("Order must be low-high or high-low"));
        return ResponseEntity.status(200).body(sortedProducts);
    }

    @GetMapping("/best-seller")
    public ResponseEntity<?> getBestSeller() {
        Product product = productService.getBestSeller();
        if (product == null)
            return ResponseEntity.status(404).body(new ApiResponse("No products found"));
        return ResponseEntity.status(200).body(product);
    }

}
