package br.com.vscgabriel.baozistore.service.impl;

import br.com.vscgabriel.baozistore.domain.dto.ProductRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductResponseDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductUpdateDTO;
import br.com.vscgabriel.baozistore.entity.ProductEntity;
import br.com.vscgabriel.baozistore.mapper.ProductMapper;
import br.com.vscgabriel.baozistore.repository.ProductRepository;
import br.com.vscgabriel.baozistore.service.ProductService;
import br.com.vscgabriel.baozistore.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public ProductResponseDTO create(ProductRequestDTO request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return repository.findAllByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return mapper.toResponse(findActiveEntity(id));
    }

    @Override
    public ProductResponseDTO update(Long id, ProductUpdateDTO request) {
        ProductEntity product = findActiveEntity(id);
        product.setName(request.name());
        product.setPrice(request.price());
        product.setAvailable(request.available());
        return mapper.toResponse(repository.save(product));
    }

    @Override
    public void delete(Long id) {
        repository.save(deactivate(findActiveEntity(id)));
    }

    private ProductEntity findActiveEntity(Long id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    private ProductEntity deactivate(ProductEntity entity) {
        entity.deactivate();
        return entity;
    }
}
