package com.engeto.java.academy.project2_cryptoPortfolioManager.controller;

import com.engeto.java.academy.project2_cryptoPortfolioManager.dto.CreateCryptoRequest;
import com.engeto.java.academy.project2_cryptoPortfolioManager.dto.UpdateCryptoRequest;
import com.engeto.java.academy.project2_cryptoPortfolioManager.model.Crypto;
import com.engeto.java.academy.project2_cryptoPortfolioManager.service.CryptoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptos")
@Validated
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping
    public ResponseEntity<Crypto> createCrypto(@Valid @RequestBody CreateCryptoRequest req) {
        Crypto createdCrypto = cryptoService.addCrypto(
                req.name(),
                req.symbol(),
                req.price(),
                req.quantity()
        );
        return ResponseEntity
                .created(URI.create("/api/cryptos/" + createdCrypto.getId()))
                .body(createdCrypto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Crypto> updateCrypto(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCryptoRequest req) {
        Crypto updatedCrypto = cryptoService.updateCrypto(
                id,
                req.name(),
                req.symbol(),
                req.price(),
                req.quantity()
        );
        return ResponseEntity.ok(updatedCrypto);
    }

    @GetMapping
    public ResponseEntity<List<Crypto>> getAllCryptos(
            @RequestParam(name = "sort", required = false)
            @Pattern(regexp = "^(name|price|quantity)$", message = "Allowed values: name, price, quantity")
            String sortedBy
    ) {
        List<Crypto> cryptos = cryptoService.getAllCryptos(sortedBy);
        return ResponseEntity.ok(cryptos); // i prázdný seznam → 200 OK + []
    }

    @GetMapping("/{id}")
    public ResponseEntity<Crypto> getCryptoById(@PathVariable UUID id) {
        return ResponseEntity.ok(cryptoService.getCryptoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrypto(@PathVariable UUID id) {
        cryptoService.deleteCrypto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portfolio-value")
    public ResponseEntity<Double> calculateTotalCryptosValue() {
        double totalCryptosValue = cryptoService.calculateTotalCryptosValue();
        return ResponseEntity.ok(totalCryptosValue); // vždy vrací číslo (včetně 0.0)
    }
}