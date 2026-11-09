package br.com.vscgabriel.baozistore.domain.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id, String name, BigDecimal price, Boolean available) {
}
