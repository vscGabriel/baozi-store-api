package br.com.vscgabriel.baozistore.controller;

import br.com.vscgabriel.baozistore.domain.dto.ClientRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientResponseDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientPhoneNumberUpdateDTO;
import br.com.vscgabriel.baozistore.service.ClientService;
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
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDTO create(@Valid @RequestBody ClientRequestDTO request) { return service.create(request); }

    @GetMapping
    public List<ClientResponseDTO> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ClientResponseDTO findById(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/document-number/{documentNumber}")
    public ClientResponseDTO findByDocumentNumber(@PathVariable String documentNumber) {
        return service.findByDocumentNumber(documentNumber);
    }

    @PutMapping("/document-number/{documentNumber}/phone-number")
    public ClientResponseDTO updatePhoneNumber(@PathVariable String documentNumber,
                                               @Valid @RequestBody ClientPhoneNumberUpdateDTO request) {
        return service.updatePhoneNumber(documentNumber, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
