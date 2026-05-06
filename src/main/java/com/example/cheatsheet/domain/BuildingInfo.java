package com.example.cheatsheet.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingInfo {

    @NotNull(message = "{buildingInfo.yearBuilt.required}")
    @Min(value = 1800, message = "{buildingInfo.yearBuilt.range}")
    @Max(value = 2100, message = "{buildingInfo.yearBuilt.range}")
    private Integer yearBuilt;

    @NotNull(message = "{buildingInfo.floorAreaM2.required}")
    @Min(value = 1, message = "{buildingInfo.floorAreaM2.range}")
    @Max(value = 100000, message = "{buildingInfo.floorAreaM2.range}")
    private Integer floorAreaM2;

    @NotNull(message = "{buildingInfo.floors.required}")
    @Min(value = 1, message = "{buildingInfo.floors.range}")
    @Max(value = 200, message = "{buildingInfo.floors.range}")
    private Integer floors;

    @NotNull(message = "{buildingInfo.energyLabel.required}")
    @Pattern(regexp = "^[A-G]$", message = "{buildingInfo.energyLabel.format}")
    private String energyLabel;

    private boolean hasElevator;

    public BuildingInfo(BuildingInfo other) {
        this.yearBuilt = other.yearBuilt;
        this.floorAreaM2 = other.floorAreaM2;
        this.floors = other.floors;
        this.energyLabel = other.energyLabel;
        this.hasElevator = other.hasElevator;
    }
}
