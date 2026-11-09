package br.com.vscgabriel.baozistore.service;

import br.com.vscgabriel.baozistore.domain.dto.ProductRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductResponseDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductUpdateDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO request);
    List<ProductResponseDTO> findAll();
    ProductResponseDTO findById(Long id);
    ProductResponseDTO update(Long id, ProductUpdateDTO request);
    void delete(Long id);
}
