package org.thivernale.notificationservice.domain.models;

import jakarta.validation.constraints.NotBlank;

public record Address(
    @NotBlank(message = "AddressLine1 is required") String line1,
    String line2,
    @NotBlank(message = "City is required") String city,
    @NotBlank(message = "State is required") String state,
    @NotBlank(message = "ZipCode is required") String zipCode,
    @NotBlank(message = "Country is required") String country) {
}
