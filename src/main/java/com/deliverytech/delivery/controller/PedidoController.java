package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.service.PedidoService;
import jakarta.validation.Valid; // Import para @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /*
     * Criar novo pedido
     * (Refatorado para usar DTOs e @Valid)
     */
    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        try {
            PedidoResponseDTO pedidoSalvo = pedidoService.criarPedido(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (IllegalArgumentException e) {
            // Captura erros de "Cliente/Restaurante não encontrado"
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /*
     * Buscar pedido por ID
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Buscar pedido por Número do Pedido
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<PedidoResponseDTO> buscarPorNumeroPedido(@PathVariable String numeroPedido) {
        return pedidoService.buscarPorNumeroPedido(numeroPedido)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Listar pedidos por status (ex: ?status=PENDENTE)
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/status")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorStatus(@RequestParam String status) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    /*
     * Listar todos os pedidos de um cliente
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /*
     * Atualizar status do pedido (ex: ?status=CONFIRMADO)
     * (Refatorado para retornar DTO)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            PedidoResponseDTO pedidoAtualizado = pedidoService.atualizarStatus(id, status);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura exceções de validação do service (ex: ID não encontrado, status inválido)
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /*
     * Cancelar pedido (define o status como "CANCELADO")
     * (Refatorado para retornar DTO)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            PedidoResponseDTO pedidoCancelado = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedidoCancelado);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura exceções de validação (ex: ID não encontrado, pedido já entregue)
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
}