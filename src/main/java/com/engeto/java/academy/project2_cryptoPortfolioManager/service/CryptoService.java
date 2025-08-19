package com.engeto.java.academy.project2_cryptoPortfolioManager.service;

import com.engeto.java.academy.project2_cryptoPortfolioManager.model.Crypto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CryptoService {

    private final UuidService uuidService;

    public CryptoService(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    private List<Crypto> cryptosInList = new ArrayList<>();

    // Přidání nové kryptoměny
    public Crypto addCrypto(String name, String symbol, double price, double quantity) {
        UUID id = uuidService.generateUuid(); // generování unikátního ID
        Crypto crypto = new Crypto(id, name, symbol, price, quantity);
        cryptosInList.add(crypto);
        return crypto;
    }

    // Výpis seznamu všech kryptoměn
    public List<Crypto> getAllCryptos(String sortedBy) {
        List<Crypto> allCryptos = new ArrayList<>(cryptosInList);
        if (allCryptos.isEmpty()) return allCryptos;// vrátí neseřazený seznam
        if (sortedBy == null || sortedBy.isBlank()) {
            return allCryptos;// vrátí neseřazený seznam
        }
        switch (sortedBy.toLowerCase()) {
            case "name" -> allCryptos.sort(
                    Comparator.comparing(
                            Crypto::getName,
                            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    )
            );
            case "price" -> allCryptos.sort(
                    Comparator.comparing(
                            Crypto::getPrice,
                            Comparator.nullsLast(Double::compareTo)
                    )
            );
            case "quantity" -> allCryptos.sort(
                    Comparator.comparing(
                            Crypto::getQuantity,
                            Comparator.nullsLast(Double::compareTo)
                    )
            );
            default -> { /* nic – ponecháno původní řazení */ }
        }
        return allCryptos;
    }

    // Získání kryptoměny podle ID
    public Crypto getCryptoById(UUID id) {
        for (Crypto crypto : cryptosInList) {
            if (crypto.getId().equals(id)) {
                return crypto;
            }
        }
        return null;
    }

    // Aktualizace existující kryptoměny
    public Crypto updateCrypto(UUID id, String name, String symbol, double price, double quantity) {
        for (Crypto crypto : cryptosInList) {
            if (crypto.getId().equals(id)) {
                crypto.setName(name);
                crypto.setSymbol(symbol);
                crypto.setPrice(price);
                crypto.setQuantity(quantity);
                return crypto;
            }
        }
        return null;
    }

    // Výpočet celkové hodnoty všech kryptoměn v seznamu
    public double calculateTotalCryptosValue() {
        double totalValue = 0.0;
        if (cryptosInList.isEmpty()) {
            return totalValue;
        } else {
            for (Crypto crypto : cryptosInList) {
                totalValue += (crypto.getPrice()) * (crypto.getQuantity());
            }
        }
        return totalValue;
    }

}//konec třídy CryptoService
