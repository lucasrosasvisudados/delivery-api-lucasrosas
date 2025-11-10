package com.deliverytech.delivery.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para exibir dados de um Pedido.
 */
@Data
public class PedidoResponseDTO {

    private Long id;
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private String status; 
    private BigDecimal valorTotal; 
    private String observacoes;
    private String itens;

    private ClienteResponseDTO cliente;
    private RestauranteResponseDTO restaurante;

}