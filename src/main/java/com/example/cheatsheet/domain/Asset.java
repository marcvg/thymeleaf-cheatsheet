package com.example.cheatsheet.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Asset {

    public enum Type { BUILDING }

    private Long id;
    private Type type = Type.BUILDING;

    @NotBlank
    private String name;

    private boolean active = true;

    @Valid
    private Address address = new Address();

    @Valid
    private BuildingInfo buildingInfo = new BuildingInfo();

    private List<SalePriceValuation> salePriceValuations = new ArrayList<>();

    public SalePriceValuation getOrAddValuation(final LocalDate valuationDate) {
        return salePriceValuations.stream()
                .filter(v -> valuationDate.equals(v.getValuationDate()))
                .findFirst()
                .orElseGet(() -> {
                    var newValuation = new SalePriceValuation();
                    newValuation.setValuationDate(valuationDate);
                    newValuation.setAsset(this);
                    salePriceValuations.add(newValuation);
                    return newValuation;
                });
    }

    /** A valuation is "open" while it has no recorded sale price yet. */
    public boolean hasOpenValuations() {
        return salePriceValuations.stream().anyMatch(v -> v.getSalePrice() == null);
    }
}
