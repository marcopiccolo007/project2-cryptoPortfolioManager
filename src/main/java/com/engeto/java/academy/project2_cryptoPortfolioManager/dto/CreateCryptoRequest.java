package com.engeto.java.academy.project2_cryptoPortfolioManager.dto;

import jakarta.validation.constraints.*;

public record CreateCryptoRequest(
        @NotBlank(message = "name must not be blank")
        String name,

        @NotBlank(message = "symbol must not be blank")
        @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "symbol must be 2-10 uppercase alphanumerics")
        String symbol,

        @NotNull @Positive
        Double price,

        @NotNull @PositiveOrZero
        Double quantity
) {}
