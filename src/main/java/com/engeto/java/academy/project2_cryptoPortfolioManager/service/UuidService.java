package com.engeto.java.academy.project2_cryptoPortfolioManager.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidService {

    public UUID generateUuid() {
        return UUID.randomUUID();
    }

}
