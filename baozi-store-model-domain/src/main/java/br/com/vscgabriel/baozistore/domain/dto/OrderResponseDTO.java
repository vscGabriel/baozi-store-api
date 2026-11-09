package br.com.vscgabriel.baozistore.domain.dto;

import br.com.vscgabriel.baozistore.domain.enums.OrderStatusEnum;

public record OrderResponseDTO(Long id, Long clientId, Long productId, Integer quantity, OrderStatusEnum status) {
}
