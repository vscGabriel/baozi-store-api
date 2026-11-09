package br.com.vscgabriel.baozistore.repository;

import br.com.vscgabriel.baozistore.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdAndActiveTrue(Long id);
    List<OrderEntity> findAllByActiveTrue();
}
