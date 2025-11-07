package com.deliverytech.delivery.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * Resposta de erro padronizada.
 * @JsonInclude(JsonInclude.Include.NON_NULL) evita que campos nulos
 * (como 'message' ou 'details') sejam exibidos no JSON final.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse {
    private int status;
    private String error;
    private String message; // Para erros simples (BusinessException, EntityNotFound)
    private Object details; // Para erros complexos (ValidationException)
    private LocalDateTime timestamp;

    /**
     * Construtor para erros simples (ex: BusinessException, EntityNotFoundException).
     */
    public ValidationErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Construtor para erros de validação (MethodArgumentNotValidException).
     * @param details Um Map<String, String> com os campos e seus erros.
     */
    public ValidationErrorResponse(int status, String error, LocalDateTime timestamp, Object details) {
        this.status = status;
        this.error = error;
        this.timestamp = timestamp;
        this.details = details;
    }

    // Getters e Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getDetails() { return details; }
    public void setDetails(Object details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}