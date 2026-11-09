package br.com.vscgabriel.baozistore.repository;

import br.com.vscgabriel.baozistore.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByIdAndActiveTrue(Long id);
    Optional<ClientEntity> findByDocumentNumberAndActiveTrue(String documentNumber);
    List<ClientEntity> findAllByActiveTrue();
    boolean existsByDocumentNumber(String documentNumber);
}
