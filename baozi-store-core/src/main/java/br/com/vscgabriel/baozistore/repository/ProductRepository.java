package br.com.vscgabriel.baozistore.repository;

import br.com.vscgabriel.baozistore.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByIdAndActiveTrue(Long id);
    List<ProductEntity> findAllByActiveTrue();
}
