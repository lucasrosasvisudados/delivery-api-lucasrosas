package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.entity.Produto;
import lombok.Data;

/**
 * DTO para exibir dados de um Produto.
 * Retorna informações detalhadas, incluindo um objeto aninhado
 * para o restaurante.
 */
@Data
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private String categoria;
    private Boolean disponivel;

    // DTO aninhado para fornecer o contexto do restaurante
    private RestauranteResponseDTO restaurante;

    /**
     * Construtor para mapear a entidade Produto para o DTO de resposta.
     * @param produto A entidade JPA.
     */
    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.categoria = produto.getCategoria();
        this.disponivel = produto.getDisponivel();

        // Mapeia a entidade aninhada para seu respectivo DTO
        if (produto.getRestaurante() != null) {
            this.restaurante = new RestauranteResponseDTO(produto.getRestaurante());
        }
    }
}