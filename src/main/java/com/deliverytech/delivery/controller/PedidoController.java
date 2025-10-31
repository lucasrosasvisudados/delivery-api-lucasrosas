package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /*
     * Criar novo pedido
     */
    @PostMapping
    public ResponseEntity<?> criarPedido(@Validated @RequestBody Pedido pedido) {
        try {
            Pedido pedidoSalvo = pedidoService.criarPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /*
     * Buscar pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * Buscar pedido por Número do Pedido
     */
    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<?> buscarPorNumeroPedido(@PathVariable String numeroPedido) {
        Optional<Pedido> pedido = pedidoService.buscarPorNumeroPedido(numeroPedido);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * Listar pedidos por status (ex: ?status=PENDENTE)
     */
    @GetMapping("/status")
    public ResponseEntity<List<Pedido>> listarPorStatus(@RequestParam String status) {
        List<Pedido> pedidos = pedidoService.listarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    /*
     * Listar todos os pedidos de um cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> listarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /*
     * Atualizar status do pedido (ex: ?status=CONFIRMADO)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, status);
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
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.ok().body("Pedido cancelado com sucesso");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura exceções de validação (ex: ID não encontrado, pedido já entregue)
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
}