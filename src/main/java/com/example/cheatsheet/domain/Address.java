package com.example.cheatsheet.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotBlank(message = "{address.street.required}")
    @Size(max = 100)
    private String street;

    @NotBlank(message = "{address.number.required}")
    @Pattern(regexp = "^\\d{1,5}[a-zA-Z]?$", message = "{address.number.format}")
    private String number;

    @NotBlank(message = "{address.postalCode.required}")
    @Pattern(regexp = "^\\d{4}$", message = "{address.postalCode.format}")
    private String postalCode;

    @NotBlank(message = "{address.city.required}")
    @Size(max = 100)
    private String city;

    public Address(Address other) {
        this.street = other.street;
        this.number = other.number;
        this.postalCode = other.postalCode;
        this.city = other.city;
    }
}
