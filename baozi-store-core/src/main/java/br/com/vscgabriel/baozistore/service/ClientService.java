package br.com.vscgabriel.baozistore.service;

import br.com.vscgabriel.baozistore.domain.dto.ClientRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientResponseDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientPhoneNumberUpdateDTO;

import java.util.List;

public interface ClientService {
    ClientResponseDTO create(ClientRequestDTO request);
    List<ClientResponseDTO> findAll();
    ClientResponseDTO findById(Long id);
    ClientResponseDTO findByDocumentNumber(String documentNumber);
    ClientResponseDTO updatePhoneNumber(String documentNumber, ClientPhoneNumberUpdateDTO request);
    void delete(Long id);
}
