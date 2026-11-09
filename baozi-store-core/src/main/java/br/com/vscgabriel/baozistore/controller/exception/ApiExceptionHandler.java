package br.com.vscgabriel.baozistore.controller.exception;

import br.com.vscgabriel.baozistore.domain.dto.ApiErrorResponseDTO;
import br.com.vscgabriel.baozistore.service.exception.BusinessRuleException;
import br.com.vscgabriel.baozistore.service.exception.ResourceNotFoundException;
import br.com.vscgabriel.baozistore.service.exception.ResourceAlreadyExistsException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponseDTO resourceNotFound(ResourceNotFoundException exception) {
        return errorResponse(HttpStatus.NOT_FOUND, List.of(exception.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponseDTO resourceAlreadyExists(ResourceAlreadyExistsException exception) {
        return errorResponse(HttpStatus.CONFLICT, List.of(exception.getMessage()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ApiErrorResponseDTO businessRule(BusinessRuleException exception) {
        return errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, List.of(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponseDTO validation(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct()
                .sorted()
                .toList();
        return errorResponse(HttpStatus.BAD_REQUEST, messages);
    }

    private ApiErrorResponseDTO errorResponse(HttpStatus status, List<String> messages) {
        return new ApiErrorResponseDTO(status.value(), messages);
    }
}
