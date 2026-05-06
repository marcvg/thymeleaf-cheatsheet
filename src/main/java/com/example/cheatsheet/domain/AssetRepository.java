package com.example.cheatsheet.domain;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory store for buildings. Doubles as the application service for now —
 * when persistence moves into main, split into a JPA repository plus a service
 * (the deactivate() business rule belongs in the service layer either way).
 */
@Component
public class AssetRepository {

    private final Map<Long, Asset> store = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong();

    @PostConstruct
    void seed() {
        save(building("HQ Antwerp",      addr("Meir", "12",   "2000", "Antwerpen"),
                      info(1925,  850, 5, "C", true),  List.of()));

        save(building("Branch Brussels", addr("Rue Royale", "60", "1000", "Brussel"),
                      info(1998, 2400, 8, "B", true),  List.of(BigDecimal.valueOf(1_750_000))));

        // This one has an open (unpriced) valuation, so deactivate() will refuse.
        save(building("Warehouse Ghent", addr("Dokweg", "7",   "9000", "Gent"),
                      info(2010, 5200, 1, "A", false), Arrays.asList((BigDecimal) null)));
    }

    public List<Asset> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Asset> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void updateAddress(Long id, Address address) {
        findById(id).orElseThrow().setAddress(new Address(address));
    }

    public void updateInfo(Long id, BuildingInfo info) {
        findById(id).orElseThrow().setBuildingInfo(new BuildingInfo(info));
    }

    /** Business rule: a building with any open valuation cannot be deactivated. */
    public void deactivate(Long id) {
        Asset asset = findById(id).orElseThrow();
        if (asset.hasOpenValuations()) {
            throw new IllegalStateException(
                "Cannot deactivate \"" + asset.getName() + "\": it has open valuations.");
        }
        asset.setActive(false);
    }

    private Asset save(Asset asset) {
        if (asset.getId() == null) {
            asset.setId(idGen.incrementAndGet());
        }
        store.put(asset.getId(), asset);
        return asset;
    }

    private Asset building(String name, Address address, BuildingInfo info, List<BigDecimal> valuationPrices) {
        Asset asset = new Asset();
        asset.setName(name);
        asset.setAddress(address);
        asset.setBuildingInfo(info);
        for (int i = 0; i < valuationPrices.size(); i++) {
            SalePriceValuation v = asset.getOrAddValuation(LocalDate.now().minusMonths(i + 1L));
            v.setSalePrice(valuationPrices.get(i));
        }
        return asset;
    }

    private Address addr(String street, String number, String postalCode, String city) {
        return new Address(street, number, postalCode, city);
    }

    private BuildingInfo info(int yearBuilt, int floorArea, int floors, String energyLabel, boolean elevator) {
        return new BuildingInfo(yearBuilt, floorArea, floors, energyLabel, elevator);
    }
}
