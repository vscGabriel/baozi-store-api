package br.com.vscgabriel.baozistore.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductUpdateDTO(
        @NotBlank(message = "Product name is required") String name,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,
        @NotNull(message = "Availability is required") Boolean available
) {
}
