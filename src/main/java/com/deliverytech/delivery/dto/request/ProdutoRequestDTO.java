package com.deliverytech.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO para o cadastro e atualização de Produtos.
 */
@Data
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    // Categoria é opcional
    private String categoria;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

}