package br.com.vscgabriel.baozistore.domain.dto;

public record ClientResponseDTO(Long id, String name, String documentNumber,
                                Integer areaPhoneCode, String phoneNumber) {
}
