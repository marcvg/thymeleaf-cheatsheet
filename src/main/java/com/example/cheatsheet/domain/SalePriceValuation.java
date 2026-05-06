package com.example.cheatsheet.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SalePriceValuation {

    private LocalDate valuationDate;
    private Asset asset;

    /** Null while the valuation is still "open" (price not recorded yet). */
    private BigDecimal salePrice;
}
