package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.ApiResponse.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Merchant;
import com.example.tuwaiqcapstone1.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getMerchants(){
        return ResponseEntity.status(200).body(merchantService.getMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@RequestBody @Valid Merchant merchant, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = merchantService.addMerchant(merchant);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("ID: " + merchant.getId() + " already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchant(@PathVariable String id, @RequestBody @Valid Merchant merchant, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = merchantService.updateMerchant(id, merchant);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No merchant with ID: " + id + " found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String id){
        boolean isDone = merchantService.deleteMerchant(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No merchant with ID: " + id + " found"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-name/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name){
        Merchant merchant = merchantService.searchByName(name);
        if(merchant == null)
            return ResponseEntity.status(400).body(new ApiResponse("No merchant with name: " + name + " found"));
        return ResponseEntity.status(200).body(merchant);
    }
}
