package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.ApiResponse.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Merchant;
import com.example.tuwaiqcapstone1.Model.MerchantStock;
import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStock(){
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        int result = merchantStockService.addMerchantStock(merchantStock);
        if(result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + merchantStock.getProductId() + " found"));
        if(result == 1)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantStock.getMerchantId() + " found"));
        if(result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Stock must be at least 11"));
        if(result == 3)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock added successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("ID: " + merchantStock.getId() + " already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchantStock(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        int result = merchantStockService.updateMerchantStock(id, merchantStock);
        if(result == 0)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + merchantStock.getProductId() + " found"));
        if(result == 1)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantStock.getMerchantId() + " found"));
        if(result == 2)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock updated successfully"));
        return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with ID: " + id + " found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String id){
        boolean isDone = merchantStockService.deleteMerchantStock(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock deleted successfully"));
        return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with ID: " + id + " found"));
    }


    //EXTRA ENDPOINTS
    @PutMapping("/add-stock/{productId}/{merchantId}/{amount}")
    public ResponseEntity<?> addStock(@PathVariable String productId, @PathVariable String merchantId, @PathVariable int amount){
        int result = merchantStockService.addStock(productId, merchantId, amount);
        if(result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + productId + " found"));
        if(result == -2)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantId + " found"));
        if(result == -3)
            return ResponseEntity.status(400).body(new ApiResponse("Amount should be greater than zero"));
        if(result == -4)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with product id: " + productId + " and merchant id: " + merchantId + " found"));
        return ResponseEntity.status(200).body(new ApiResponse("Stock added successfully"));
    }

    @DeleteMapping("/delete-merchant-stock/{merchantId}")
    public ResponseEntity<?> clearMerchantStock(@PathVariable String merchantId){
        int result = merchantStockService.clearMerchantStock(merchantId);
        if(result == -1)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantId + " found"));
        if(result == 0)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant with ID: " + merchantId + " doesn't have any stock"));
        return ResponseEntity.status(200).body(new ApiResponse("Stock for merchant with ID: " + merchantId + " cleared successfully"));

    }

    @GetMapping("/merchant-products/{merchantId}")
    public ResponseEntity<?> getMerchantProducts(@PathVariable String merchantId){
        ArrayList<Product> merchantProducts = merchantStockService.getMerchantProducts(merchantId);
        if(merchantProducts == null)
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID: " + merchantId + " found"));
        if(merchantProducts.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("Merchant with ID: " + merchantId + " doesn't have any products in stock"));
        return ResponseEntity.status(200).body(merchantProducts);
    }

    @GetMapping("/product-merchants/{productId}")
    public ResponseEntity<?> getProductMerchants(@PathVariable String productId){
        ArrayList<Merchant> productMerchants = merchantStockService.getProductMerchants(productId);
        if(productMerchants == null)
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID: " + productId + " found"));
        if(productMerchants.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("No merchants sell product with ID: " + productId));
        return ResponseEntity.status(200).body(productMerchants);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<?> getOutOfStockProducts(){
        ArrayList<Product> outOfStockProducts = merchantStockService.getOutOfStockProducts();
        if(outOfStockProducts.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("There are no products out of stock"));
        return ResponseEntity.status(200).body(outOfStockProducts);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<?> getInStockProducts(){
        ArrayList<Product> inStockProducts = merchantStockService.getInStockProducts();
        if(inStockProducts.isEmpty())
            return ResponseEntity.status(200).body(new ApiResponse("There are no products in stock"));
        return ResponseEntity.status(200).body(inStockProducts);
    }
}
