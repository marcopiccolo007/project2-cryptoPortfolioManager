package com.engeto.java.academy.project2_cryptoPortfolioManager.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    @NotNull(message = "id must not be null")
    private UUID id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "symbol must not be blank")
    @Pattern(
            regexp = "^[A-Z0-9]{3,5}$",
            message = "symbol must be 3-5 uppercase alphanumerics (e.g., BTC, ETC, TAR)"
    )
    private String symbol;

    @NotNull(message = "price must be provided")
    @Positive(message = "price must be > 0")
    private Double price;

    @NotNull(message = "quantity must be provided")
    @PositiveOrZero(message = "quantity must be >= 0")
    private Double quantity;
}
