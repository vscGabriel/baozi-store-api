package br.com.vscgabriel.baozistore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientRequestDTO(
        @NotBlank(message = "Client name is required") String name,
        @NotBlank(message = "Document number is required")
        @Size(max = 30, message = "Document number must contain at most 30 characters")
        String documentNumber,
        @NotNull(message = "Area phone code is required")
        @Min(value = 10, message = "Area phone code must contain 2 digits")
        @Max(value = 99, message = "Area phone code must contain 2 digits")
        Integer areaPhoneCode,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{8,9}$", message = "Phone number must contain 8 or 9 digits")
        String phoneNumber
) {
}
