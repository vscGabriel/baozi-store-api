package br.com.vscgabriel.baozistore.mapper;

import br.com.vscgabriel.baozistore.domain.dto.ClientRequestDTO;
import br.com.vscgabriel.baozistore.domain.dto.ClientResponseDTO;
import br.com.vscgabriel.baozistore.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "lastUpdatedUser", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    ClientEntity toEntity(ClientRequestDTO request);
    ClientResponseDTO toResponse(ClientEntity entity);
}
