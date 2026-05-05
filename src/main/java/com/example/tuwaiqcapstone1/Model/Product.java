package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    @NotEmpty(message = "ID must not be empty")
    @Size(min = 2, message = "ID must be at least 2 characters")
    private String id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 4, message = "Name must be at least 4 characters")
    private String name;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be positive number")
    private double price;

    @NotEmpty(message = "Category ID must not be empty")
    private String categoryId;

    @NotNull(message = "Price must not be null")
    @PositiveOrZero(message = "Times Purchased must be zero or positive number")
    private int timesPurchased;
}
