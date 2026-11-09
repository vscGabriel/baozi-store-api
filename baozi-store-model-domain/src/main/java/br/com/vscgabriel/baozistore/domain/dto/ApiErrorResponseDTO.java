package br.com.vscgabriel.baozistore.domain.dto;

import java.util.List;

public record ApiErrorResponseDTO(int status, List<String> errorMessage) {
}
