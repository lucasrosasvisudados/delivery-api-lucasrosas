package com.deliverytech.delivery.dto.response;

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
    private String status; // O DTO continua retornando String para o JSON
    private Double valorTotal;
    private String observacoes;
    private String itens;

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
        
        // --- CORREÇÃO APLICADA AQUI ---
        // Verifica se o status não é nulo antes de converter para String
        if (pedido.getStatus() != null) {
            this.status = pedido.getStatus().name(); // Converte o Enum (ex: StatusPedido.PENDENTE) para String ("PENDENTE")
        }
        // ------------------------------

        this.valorTotal = pedido.getValorTotal();
        this.observacoes = pedido.getObservacoes();
        this.itens = pedido.getItens();

        if (pedido.getCliente() != null) {
            this.cliente = new ClienteResponseDTO(pedido.getCliente());
        }

        if (pedido.getRestaurante() != null) {
            this.restaurante = new RestauranteResponseDTO(pedido.getRestaurante());
        }
    }
}