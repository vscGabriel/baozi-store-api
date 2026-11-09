package br.com.vscgabriel.baozistore.mapper;

import br.com.vscgabriel.baozistore.domain.dto.OrderResponseDTO;
import br.com.vscgabriel.baozistore.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "productId", source = "product.id")
    OrderResponseDTO toResponse(OrderEntity entity);
}
