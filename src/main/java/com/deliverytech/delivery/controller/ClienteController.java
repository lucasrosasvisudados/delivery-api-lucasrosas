package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.deliverytech.delivery.services.ClienteService;
import com.deliverytech.delivery.services.PedidoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    /*
     * Cadastrar novo cliente
     */
    @PostMapping
    @Operation(summary = "Cadastrar um novo cliente", description = "Endpoint para cadastrar um novo cliente na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Cliente já Cadastrado")
    })
    public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO cliente = clienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    /*
     * Listar todos os clientes ativos
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        List<ClienteResponseDTO> clientes = clienteService.listarAtivos();
        return ResponseEntity.ok(clientes);
    }

    /*
     * Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }
/*
     * Atualizar cliente
     * (Refatorado para usar DTOs e @Valid)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
    @Validated @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO cliente = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(cliente);
    }

    /*
     * Inativar cliente (soft delete)
     */
    @PatchMapping("/{id}")
     public ResponseEntity<ClienteResponseDTO> inativar(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(cliente);
    }

    /*
     * Buscar clientes por nome
     */
    @GetMapping("/buscar")
      public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    /*
     * Buscar cliente por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
        ClienteResponseDTO cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }
     
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos listados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

}
