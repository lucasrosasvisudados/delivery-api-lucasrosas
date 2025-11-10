package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;
import lombok.Data;

/**
 * DTO para exibir dados de um Produto.
 */
@Data
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private Boolean disponivel;

    private RestauranteResponseDTO restaurante;

}