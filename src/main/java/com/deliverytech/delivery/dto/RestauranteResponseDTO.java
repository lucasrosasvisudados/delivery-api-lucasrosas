package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.entity.Restaurante;
import lombok.Data;

/**
 * DTO para exibir dados de Restaurante.
 * (Preenchido com base no padrão do ClienteResponseDTO).
 */
@Data
public class RestauranteResponseDTO {

    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private Double taxaEntrega;
    private Double avaliacao;
    private Boolean ativo;

    /**
     * Construtor para mapear a entidade Restaurante para o DTO.
     * @param restaurante A entidade JPA.
     */
    public RestauranteResponseDTO(Restaurante restaurante) {
        this.id = restaurante.getId();
        this.nome = restaurante.getNome();
        this.categoria = restaurante.getCategoria();
        this.endereco = restaurante.getEndereco();
        this.telefone = restaurante.getTelefone();
        this.taxaEntrega = restaurante.getTaxaEntrega();
        this.avaliacao = restaurante.getAvaliacao();
        this.ativo = restaurante.getAtivo();
    }
}