package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.services.PedidoService;
import com.deliverytech.delivery.services.RelatorioService;
import com.deliverytech.delivery.services.RestauranteService;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {
    
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO relatorio = restauranteRespository.relatorioVendasPorRestaurante();
        return ResponseEntity.ok(cliente);
    }
}
