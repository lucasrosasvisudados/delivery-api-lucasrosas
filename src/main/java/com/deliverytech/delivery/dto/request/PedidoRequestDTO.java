package com.deliverytech.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO para o cadastro de novos pedidos.
 * Contém apenas os campos que o cliente deve informar.
 */
@Data
public class PedidoRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    @NotBlank(message = "A lista de itens é obrigatória")
    private String itens; // Mantido como String, conforme a entidade

    @NotNull(message = "O valor total é obrigatório")
    @Positive(message = "O valor total deve ser maior que zero")
    private Double valorTotal;

    // Observações são opcionais
    private String observacoes;
}