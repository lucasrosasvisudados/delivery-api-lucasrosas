package com.deliverytech.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal; 

/**
 * DTO para o cadastro de novos pedidos.
 */
@Data
public class PedidoRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    @NotBlank(message = "A lista de itens é obrigatória")
    private String itens;

    @NotNull(message = "O valor total é obrigatório")
    @Positive(message = "O valor total deve ser maior que zero")
    private BigDecimal valorTotal; 

    private String observacoes;
}