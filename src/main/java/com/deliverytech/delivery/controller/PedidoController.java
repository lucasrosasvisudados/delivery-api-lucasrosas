package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery.services.PedidoService; // Import da Interface
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@Validated
public class PedidoController {

    @Autowired
    private PedidoService pedidoService; 

    @PostMapping
    @Operation(summary = "Criar um novo pedido", description = "Endpoint para registrar um novo pedido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Cliente ou Restaurante não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> criarPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO pedidoSalvo = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar pedido por número", description = "Retorna um pedido específico pelo seu número (ex: 'PED1001').")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorNumeroPedido(@PathVariable String numeroPedido) {
        PedidoResponseDTO pedido = pedidoService.buscarPorNumeroPedido(numeroPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/status")
    @Operation(summary = "Listar pedidos por status", description = "Retorna pedidos filtrados por status (PENDENTE, CONFIRMADO, etc.)")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorStatus(@RequestParam String status) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status de um pedido", description = "Altera o status de um pedido existente (ex: PENDENTE -> CONFIRMADO).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        PedidoResponseDTO pedidoAtualizado = pedidoService.atualizarStatus(id, status);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cancelar um pedido", description = "Muda o status de um pedido para 'CANCELADO'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "400", description = "Não é possível cancelar (ex: já entregue)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long id) {
        PedidoResponseDTO pedidoCancelado = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedidoCancelado);
    }
}