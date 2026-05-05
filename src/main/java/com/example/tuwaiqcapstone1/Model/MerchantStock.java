package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "ID must not be empty")
    @Size(min = 2, message = "ID must be at least 2 characters")
    private String id;

    @NotEmpty(message = "Product ID must not be empty")
    private String productId;

    @NotEmpty(message = "Merchant ID must not be empty")
    private String merchantId;

    @NotNull(message = "Stock must not be null")
    private Integer stock;

}
