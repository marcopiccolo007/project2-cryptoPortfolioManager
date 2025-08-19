package com.engeto.java.academy.project2_cryptoPortfolioManager.model;

//POJO = prostý Java objekt, žádna speciální pravidla, čistě datová struktura (data container)
//Java Bean = POJO + pravidla (private fields, gettery/settery, default constructor)

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor    // defaultní konstruktor
public class Crypto {

    @NotNull private UUID id; // unikátní identifikátor
    @NotBlank private String name;
    @NotBlank private String symbol;
    @Positive private Double price;
    @PositiveOrZero private Double quantity;

//  Konstruktor pro vytvoření nové kryptoměny (spolu s vygenerováním ID)
    public Crypto(UUID id, String name, String symbol, Double price, Double quantity) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

}
