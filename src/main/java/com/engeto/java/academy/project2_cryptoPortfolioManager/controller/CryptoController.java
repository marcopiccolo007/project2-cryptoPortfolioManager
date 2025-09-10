package com.engeto.java.academy.project2_cryptoPortfolioManager.controller;

import com.engeto.java.academy.project2_cryptoPortfolioManager.dto.CreateCryptoRequest;
import com.engeto.java.academy.project2_cryptoPortfolioManager.dto.UpdateCryptoRequest;
import com.engeto.java.academy.project2_cryptoPortfolioManager.model.Crypto;
import com.engeto.java.academy.project2_cryptoPortfolioManager.service.CryptoService;
import com.engeto.java.academy.project2_cryptoPortfolioManager.service.UuidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    private final UuidService uuidService;

    public CryptoController(CryptoService cryptoService, UuidService uuidService) {
        this.uuidService = uuidService;
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
        return ResponseEntity.created(URI.create("/api/cryptos/" + createdCrypto.getId())).body(createdCrypto);
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
            @RequestParam(name = "sort", required = false) String sort) {
            // validace hodnoty sort – podporovány jen name|price|quantity
            if (sort != null && !sort.isBlank()) {
                String sortBy = sort.toLowerCase();
                if (!(sortBy.equals("name") || sortBy.equals("price") || sortBy.equals("quantity"))) {
                    // 400 Bad Request – "sebere to"" globální handler a vrátí JSON chybu
                    throw new IllegalArgumentException("Unsupported sort value. Use one of: name, price, quantity.");
                }
            }

            List<Crypto> result = cryptoService.getAllCryptos(sort);
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();// 204 No Content – bez body
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    @GetMapping("/{id}")
    public ResponseEntity<Crypto> getCryptoById(@PathVariable UUID id) {
        Crypto retrievedCrypto = cryptoService.getCryptoById(id);
        if (retrievedCrypto != null) {
            return new ResponseEntity<>(retrievedCrypto, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrypto(@PathVariable UUID id) {
        cryptoService.deleteCrypto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portfolio-value")
    public ResponseEntity<Double> calculateTotalCryptosValue() {
        double totalCryptosValue = cryptoService.calculateTotalCryptosValue();
        if (totalCryptosValue == 0.0) return ResponseEntity.noContent().build();// 204 bez body
        return ResponseEntity.ok(totalCryptosValue);
        }
}