package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.entity.Pedido;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para exibir dados de um Pedido.
 * Retorna informações detalhadas, incluindo objetos aninhados
 * para o cliente e o restaurante.
 */
@Data
public class PedidoResponseDTO {

    private Long id;
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private String status;
    private Double valorTotal;
    private String observacoes;
    private String itens;

    // DTOs aninhados para fornecer mais contexto na resposta
    private ClienteResponseDTO cliente;
    private RestauranteResponseDTO restaurante;

    /**
     * Construtor para mapear a entidade Pedido para o DTO de resposta.
     * @param pedido A entidade JPA.
     */
    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.numeroPedido = pedido.getNumeroPedido();
        this.dataPedido = pedido.getDataPedido();
        this.status = pedido.getStatus();
        this.valorTotal = pedido.getValorTotal();
        this.observacoes = pedido.getObservacoes();
        this.itens = pedido.getItens();

        // Mapeia as entidades aninhadas para seus respectivos DTOs
        if (pedido.getCliente() != null) {
            this.cliente = new ClienteResponseDTO(pedido.getCliente());
        }

        if (pedido.getRestaurante() != null) {
            this.restaurante = new RestauranteResponseDTO(pedido.getRestaurante());
        }
    }
}