package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.services.ProdutoService;
import com.deliverytech.delivery.services.RestauranteService; 

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
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@Validated 
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService; // Injetando a interface

    @Autowired
    private ProdutoService produtoService;

    /*
     * Cadastrar novo restaurante
     */
    @PostMapping
    @Operation(summary = "Cadastrar um novo restaurante", description = "Endpoint para cadastrar um novo restaurante na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (dados faltando ou incorretos)")
    })
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody RestauranteRequestDTO dto) {
        // Bloco try-catch removido. O GlobalExceptionHandler cuidará dos erros.
        RestauranteResponseDTO restauranteSalvo = restauranteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
    }

    /*
     * Listar todos os restaurantes ativos
     */
    @GetMapping
    @Operation(summary = "Listar restaurantes ativos", description = "Retorna uma lista de todos os restaurantes que estão com status 'ativo'.")
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }

    /*
     * Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna um restaurante específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }

    /*
     * Atualizar restaurante
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um restaurante", description = "Atualiza os dados de um restaurante existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id,
        @Valid @RequestBody RestauranteRequestDTO dto) {
    
        RestauranteResponseDTO restauranteAtualizado = restauranteService.atualizar(id, dto);
        return ResponseEntity.ok(restauranteAtualizado);
    }

    /*
     * Inativar/Ativar restaurante (soft delete toggle)
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Ativar ou Inativar um restaurante", description = "Muda o status 'ativo' de um restaurante (lógica de soft delete/toggle).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do restaurante alterado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> inativar(@PathVariable Long id) {
        
        RestauranteResponseDTO restaurante = restauranteService.inativar(id); 
        return ResponseEntity.ok(restaurante);
    }

    /*
     * Buscar restaurantes por nome
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar restaurantes por nome", description = "Retorna uma lista de restaurantes cujo nome contenha o texto buscado.")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(restaurantes);
    }

    /*
     * Buscar restaurantes por categoria
     */
    @GetMapping("/categoria")
    @Operation(summary = "Buscar restaurantes por categoria", description = "Retorna uma lista de restaurantes cuja categoria contenha o texto buscado.")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@RequestParam String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{restauranteId}/produtos")
    @Operation(summary = "Listar produtos de um restaurante", description = "Retorna produtos disponíveis de um restaurante específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos listados"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.listarDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }
}