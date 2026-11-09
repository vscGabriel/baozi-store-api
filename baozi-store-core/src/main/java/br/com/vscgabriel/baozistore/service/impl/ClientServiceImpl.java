package br.com.vscgabriel.baozistore.service.impl;

import br.com.vscgabriel.baozistore.domain.dto.ClientRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientResponseDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientPhoneNumberUpdateDTO;
import br.com.vscgabriel.baozistore.entity.ClientEntity;
import br.com.vscgabriel.baozistore.mapper.ClientMapper;
import br.com.vscgabriel.baozistore.repository.ClientRepository;
import br.com.vscgabriel.baozistore.service.ClientService;
import br.com.vscgabriel.baozistore.service.exception.ResourceNotFoundException;
import br.com.vscgabriel.baozistore.service.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Override
    public ClientResponseDTO create(ClientRequestDTO request) {
        if (repository.existsByDocumentNumber(request.documentNumber())) {
            throw new ResourceAlreadyExistsException("Client", "documentNumber", request.documentNumber());
        }

        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return repository.findAllByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findById(Long id) {
        return mapper.toResponse(findActiveEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findByDocumentNumber(String documentNumber) {
        return mapper.toResponse(findActiveEntityByDocumentNumber(documentNumber));
    }

    @Override
    public ClientResponseDTO updatePhoneNumber(String documentNumber, ClientPhoneNumberUpdateDTO request) {
        ClientEntity client = findActiveEntityByDocumentNumber(documentNumber);
        client.setAreaPhoneCode(request.areaPhoneCode());
        client.setPhoneNumber(request.phoneNumber());
        return mapper.toResponse(repository.save(client));
    }

    @Override
    public void delete(Long id) {
        repository.save(deactivate(findActiveEntity(id)));
    }

    private ClientEntity findActiveEntity(Long id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    private ClientEntity findActiveEntityByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumberAndActiveTrue(documentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Client", documentNumber));
    }

    private ClientEntity deactivate(ClientEntity entity) {
        entity.deactivate();
        return entity;
    }

}
