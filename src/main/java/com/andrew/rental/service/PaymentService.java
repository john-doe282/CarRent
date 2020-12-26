package com.andrew.rental.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    public boolean transaction(String sender, String receiver, BigDecimal amount) {
        return true;
    }
}
