package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.response.RelatorioResponseDTO;
import com.deliverytech.delivery.services.RelatorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
@Tag(name = "Relatórios", description = "Endpoints para visualização de dados consolidados.")
public class RelatorioController {
    
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vendas-por-restaurante/{restauranteId}")
    @Operation(summary = "Relatório Específico de Vendas por Restaurante", 
               description = "Retorna o valor total vendido e a quantidade de pedidos para um restaurante específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RelatorioResponseDTO> getRelatorioVendasPorRestauranteId(
            @PathVariable Long restauranteId) {
        
        RelatorioResponseDTO relatorio = relatorioService.relatorioVendasPorRestauranteId(restauranteId);
        return ResponseEntity.ok(relatorio);
    }
}
