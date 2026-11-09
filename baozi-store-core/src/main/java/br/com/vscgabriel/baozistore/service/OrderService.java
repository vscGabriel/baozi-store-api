package br.com.vscgabriel.baozistore.service;

import br.com.vscgabriel.baozistore.domain.dto.OrderRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO create(OrderRequestDTO request);
    List<OrderResponseDTO> findAll();
    OrderResponseDTO findById(Long id);
    void delete(Long id);
}
