package com.engeto.java.academy.project2_cryptoPortfolioManager.service;

import com.engeto.java.academy.project2_cryptoPortfolioManager.exception.BadRequestException;
import com.engeto.java.academy.project2_cryptoPortfolioManager.exception.ConflictException;
import com.engeto.java.academy.project2_cryptoPortfolioManager.exception.NotFoundException;
import com.engeto.java.academy.project2_cryptoPortfolioManager.model.Crypto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CryptoService {

    private final UuidService uuidService;

    public CryptoService(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    private final List<Crypto> cryptosInList = new ArrayList<>();

    // Přidání nové kryptoměny
    public Crypto addCrypto(String name, String symbol, double price, double quantity) {
        String sym = normalizeSymbol(symbol);
        ensureSymbolUnique(sym, null);

        UUID id = uuidService.generateUuid();
        Crypto crypto = new Crypto(id, name, sym, price, quantity);
        cryptosInList.add(crypto);
        return crypto;
    }

    // Výpis seznamu všech kryptoměn
    public List<Crypto> getAllCryptos(String sortedBy) {
        List<Crypto> allCryptos = new ArrayList<>(cryptosInList);
        if (allCryptos.isEmpty() || sortedBy == null || sortedBy.isBlank()) {
            return allCryptos; // neseřazený seznam
        }
        switch (sortedBy.toLowerCase(Locale.ROOT)) {
            case "name" -> allCryptos.sort(
                    Comparator.comparing(Crypto::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
            );
            case "price" -> allCryptos.sort(
                    Comparator.comparingDouble(Crypto::getPrice)
            );
            case "quantity" -> allCryptos.sort(
                    Comparator.comparingDouble(Crypto::getQuantity)
            );
            default ->
                    throw new BadRequestException(
                            "Unknown sort: " + sortedBy + " (allowed: name, price, quantity)");
        }
        return allCryptos;
    }

    // Získání kryptoměny podle ID
    public Crypto getCryptoById(UUID id) {
        return cryptosInList.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Crypto not found: " + id));
    }

    // Aktualizace existující kryptoměny
    public Crypto updateCrypto(UUID id, String name, String symbol, double price, double quantity) {
        Crypto crypto = getCryptoById(id);             // existenci ověří už tato metoda -- 404 pokud neexistuje

        String sym = normalizeSymbol(symbol);
        ensureSymbolUnique(sym, id);                   // 409 pokud koliduje s jiným

        // Aktualizujeme přímo nalezený objekt (není třeba ho mazat a znovu přidávat)
        crypto.setName(name);
        crypto.setSymbol(sym);
        crypto.setPrice(price);
        crypto.setQuantity(quantity);
        return crypto;
    }

    // Odebrání/smazání existující kryptoměny
    public void deleteCrypto(UUID id) {
        Crypto existing = getCryptoById(id); // vyhodí výjimku pokud neexistuje -- 404 pokud neexistuje
        cryptosInList.remove(existing);
    }

    // Výpočet celkové hodnoty všech kryptoměn v seznamu
    public double calculateTotalCryptosValue() {
        double totalValue = 0.0;
        for (Crypto crypto : cryptosInList) {
            totalValue += crypto.getPrice() * crypto.getQuantity();
        }
        return totalValue;
    }
    // helper - normalizeSymbol
    private String normalizeSymbol(String symbol) {
        if (symbol == null) throw new BadRequestException("Symbol is required");
        String sym = symbol.toUpperCase(Locale.ROOT);
        if (!sym.matches("^[A-Z0-9]{3,5}$")) {
            throw new BadRequestException("Invalid symbol format (A-Z/0-9, length 3–5)");
        }
        return sym;
    }
    // helper - ensureSymbolUnique
    private void ensureSymbolUnique(String sym, UUID excludeId) {
        boolean collision = cryptosInList.stream()
                .anyMatch(c -> (excludeId == null || !c.getId().equals(excludeId))
                        && sym.equalsIgnoreCase(c.getSymbol()));
        if (collision) throw new ConflictException("Symbol already exists in portfolio: " + sym);
    }
}