package com.engeto.java.academy.project2_cryptoPortfolioManager.dto;

import jakarta.validation.constraints.*;

public record CreateCryptoRequest(
        @NotBlank(message = "name must not be blank")
        @Size(min = 2, max = 50, message = "name has to contain min. 2/max. 50 characters")
        String name,

        @NotBlank(message = "symbol must not be blank")
        @Pattern(regexp = "^[A-Z0-9]{3,5}$", message = "symbol must be 3-5 uppercase alphanumerics")
        String symbol,

        @Positive(message = "price must be > 0")
        double price,

        @PositiveOrZero(message = "quantity must be >= 0")
        double quantity
) {}
