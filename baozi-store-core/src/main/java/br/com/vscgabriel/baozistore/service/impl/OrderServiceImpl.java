package br.com.vscgabriel.baozistore.service.impl;

import br.com.vscgabriel.baozistore.domain.dto.OrderRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.OrderResponseDTO;
import br.com.vscgabriel.baozistore.domain.enums.OrderStatusEnum;
import br.com.vscgabriel.baozistore.entity.ClientEntity;
import br.com.vscgabriel.baozistore.entity.OrderEntity;
import br.com.vscgabriel.baozistore.entity.ProductEntity;
import br.com.vscgabriel.baozistore.mapper.OrderMapper;
import br.com.vscgabriel.baozistore.repository.ClientRepository;
import br.com.vscgabriel.baozistore.repository.OrderRepository;
import br.com.vscgabriel.baozistore.repository.ProductRepository;
import br.com.vscgabriel.baozistore.service.OrderService;
import br.com.vscgabriel.baozistore.service.exception.ResourceNotFoundException;
import br.com.vscgabriel.baozistore.service.exception.BusinessRuleException;
import br.com.vscgabriel.baozistore.service.notification.OrderNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;
    private final OrderNotificationService orderNotificationService;

    @Override
    public OrderResponseDTO create(OrderRequestDTO request) {
        ClientEntity client = clientRepository.findByIdAndActiveTrue(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", request.clientId()));
        ProductEntity product = productRepository.findByIdAndActiveTrue(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new BusinessRuleException("The selected flavor is not available for order");
        }

        OrderEntity order = new OrderEntity();
        order.setClient(client);
        order.setProduct(product);
        order.setQuantity(request.quantity());
        order.setStatus(OrderStatusEnum.REGISTRADO);
        OrderEntity savedOrder = orderRepository.save(order);
        orderNotificationService.notifyOrderCreated(savedOrder.getId());
        return mapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAllByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO findById(Long id) {
        return mapper.toResponse(findActiveEntity(id));
    }

    @Override
    public void delete(Long id) {
        orderRepository.save(deactivate(findActiveEntity(id)));
    }

    private OrderEntity findActiveEntity(Long id) {
        return orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    private OrderEntity deactivate(OrderEntity entity) {
        entity.deactivate();
        return entity;
    }
}
