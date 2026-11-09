package br.com.vscgabriel.baozistore.controller;

import br.com.vscgabriel.baozistore.domain.dto.OrderRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.OrderResponseDTO;
import br.com.vscgabriel.baozistore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO create(@Valid @RequestBody OrderRequestDTO request) { return service.create(request); }

    @GetMapping
    public List<OrderResponseDTO> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public OrderResponseDTO findById(@PathVariable Long id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
