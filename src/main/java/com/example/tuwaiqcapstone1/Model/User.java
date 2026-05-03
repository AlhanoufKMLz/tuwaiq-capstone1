package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    @NotEmpty(message = "ID must not be empty")
    private String id;

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 6, message = "Username must be at least 6 characters")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 7, message = "Password must be at least 7 characters")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain characters and digits")
    private String password;

    @NotEmpty(message = "E-mail must not be empty")
    @Email(message = "Invalid email")
    private String email;

    @NotEmpty(message = "Role must not be empty")
    @Pattern(regexp = "(?i)^(Admin|Customer)$", message = "Role must be Admin or Customer")
    private String role;

    @NotNull(message = "Balance must not be null")
    @Positive(message = "Balance must be positive number")
    private double balance;
}
