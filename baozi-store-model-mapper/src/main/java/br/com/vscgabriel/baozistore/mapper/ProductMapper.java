package br.com.vscgabriel.baozistore.mapper;

import br.com.vscgabriel.baozistore.domain.dto.ProductRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ProductResponseDTO;
import br.com.vscgabriel.baozistore.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "lastUpdatedUser", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    ProductEntity toEntity(ProductRequestDTO request);
    ProductResponseDTO toResponse(ProductEntity entity);
}
