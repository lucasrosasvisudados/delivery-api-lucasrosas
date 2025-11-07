package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO para o cadastro e atualização de Produtos.
 * Contém os campos que o cliente deve informar, com as devidas validações.
 */
@Data
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    // Descrição é opcional
    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private Double preco;

    // Categoria é opcional
    private String categoria;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    // O campo 'disponivel' é gerenciado pelo service:
    // - Definido como 'true' no cadastro.
    // - Alterado para 'false' pelo endpoint DELETE (tornarIndisponivel).
}