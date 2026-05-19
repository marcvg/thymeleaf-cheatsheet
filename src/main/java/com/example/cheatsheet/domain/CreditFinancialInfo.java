package com.example.cheatsheet.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditFinancialInfo(
        String productId,
        BigDecimal interestRate,
        Integer termMonths,
        BigDecimal monthlyPayment,
        BigDecimal totalPaid,
        BigDecimal outstandingAmount,
        LocalDate nextPaymentDate
) {}
