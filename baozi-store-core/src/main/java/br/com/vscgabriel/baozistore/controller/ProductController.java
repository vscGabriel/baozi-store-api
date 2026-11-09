package br.com.vscgabriel.baozistore.controller;

import br.com.vscgabriel.baozistore.domain.dto.ProductRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductResponseDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductUpdateDTO;
import br.com.vscgabriel.baozistore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO create(@Valid @RequestBody ProductRequestDTO request) { return service.create(request); }

    @GetMapping
    public List<ProductResponseDTO> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
