package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MerchantStock {

    @NotEmpty(message = "ID must not be empty")
    private String id;

    @NotEmpty(message = "Product ID must not be empty")
    private String productId;

    @NotEmpty(message = "Merchant ID must not be empty")
    private String merchantId;

    @NotNull(message = "Stock must not be null")
    private Integer stock;

}
