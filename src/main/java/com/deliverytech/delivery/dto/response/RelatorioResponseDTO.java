package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para exibir os dados consolidados do relatório de vendas.", title = "Relatório Vendas DTO")
public class RelatorioResponseDTO {

    @Schema(description = "Nome do restaurante", example = "Pizzaria Bella")
    private String nomeRestaurante;

    @Schema(description = "Valor total vendido pelo restaurante", example = "150.70")
    private BigDecimal totalVendas;

    @Schema(description = "Quantidade de pedidos recebidos", example = "5")
    private Long quantidadePedidos; // Nome corresponde à projeção corrigida
}