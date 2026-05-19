package com.example.cheatsheet.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreditDetail(
        String id,
        LocalDate datumContract,
        BigDecimal kredietbedrag,
        BigDecimal beschikbaarSaldo,
        String status,
        LocalDate datumStatus,
        String type,
        String notaris,
        String dossierbeheerder,
        String dienst,
        List<Waarborg> waarborgen,
        List<Participant> participanten
) {
    public record Waarborg(
            String type,
            String waarborg,
            Integer rang,
            BigDecimal bedragWaarborg,
            String omschrijving,
            List<String> eigenaars
    ) {}

    public record Participant(
            String naam,
            String adres,
            String hoedanigheid
    ) {}
}
