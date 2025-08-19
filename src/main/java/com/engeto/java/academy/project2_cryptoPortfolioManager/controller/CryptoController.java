package com.engeto.java.academy.project2_cryptoPortfolioManager.controller;

import com.engeto.java.academy.project2_cryptoPortfolioManager.model.Crypto;
import com.engeto.java.academy.project2_cryptoPortfolioManager.service.CryptoService;
import com.engeto.java.academy.project2_cryptoPortfolioManager.service.UuidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptos")
public class CryptoController {

    private final CryptoService cryptoService;
    private final UuidService uuidService;

    public CryptoController(CryptoService cryptoService, UuidService uuidService) {
        this.uuidService = uuidService;
        this.cryptoService = cryptoService;
    }

    @PostMapping
    public ResponseEntity<Crypto> addCrypto(@Valid @RequestBody Crypto cryptoRequest) {
        // Ignoruje se ID od klienta, při každém založení kryptoměny nové (UU)ID
        Crypto newCrypto = cryptoService.addCrypto(
                cryptoRequest.getName(),
                cryptoRequest.getSymbol(),
                cryptoRequest.getPrice(),
                cryptoRequest.getQuantity()
        );
        return new ResponseEntity<>(newCrypto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Crypto>> getAllCryptos() {
        List<Crypto> newCryptosInList = new ArrayList<>();
        newCryptosInList = cryptoService.getAllCryptos();
        return new ResponseEntity<>(newCryptosInList, HttpStatus.OK);
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

    @PutMapping("/{id}")
    public ResponseEntity<Crypto> updateCrypto(
            @PathVariable UUID id,
            @RequestBody Crypto cryptoRequest) {
        Crypto updatedCrypto = cryptoService.updateCrypto(
                id,
                cryptoRequest.getName(),
                cryptoRequest.getSymbol(),
                cryptoRequest.getPrice(),
                cryptoRequest.getQuantity()
        );
        if (updatedCrypto != null) {
            return new ResponseEntity<>(updatedCrypto, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //
    @GetMapping("/porfolio-value")
    public ResponseEntity calculateTotalCryptosValue() {
        double totalCryptosValue = cryptoService.calculateTotalCryptosValue();
        if (totalCryptosValue == 0.0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No cryptos in portfolio.");
        } else {
        return new ResponseEntity<>(totalCryptosValue, HttpStatus.OK);
        }
    }

}
