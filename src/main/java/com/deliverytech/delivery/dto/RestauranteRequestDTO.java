package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para o cadastro e atualização de Restaurantes.
 * Contém os campos que o cliente deve informar, com as devidas validações
 * baseadas no RestauranteService.
 */
@Data
public class RestauranteRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    private String nome;

    // Categoria é opcional, conforme a lógica de validação do service
    private String categoria;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    // Telefone é opcional
    private String telefone;

    @NotNull(message = "A taxa de entrega é obrigatória (pode ser 0)")
    @Min(value = 0, message = "A taxa de entrega não pode ser negativa")
    private Double taxaEntrega;

    // Campos como 'id', 'avaliacao' e 'ativo' são omitidos,
    // pois são gerenciados pelo backend (definidos no service).
}