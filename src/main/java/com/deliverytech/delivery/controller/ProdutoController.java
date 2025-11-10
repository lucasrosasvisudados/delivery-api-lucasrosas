package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.services.ProdutoService; // Import da Interface
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
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
@Validated
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; 

    @PostMapping
    @Operation(summary = "Cadastrar um novo produto", description = "Endpoint para cadastrar um novo produto associado a um restaurante.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO produtoSalvo = produtoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @GetMapping
    @Operation(summary = "Listar produtos disponíveis", description = "Retorna uma lista de todos os produtos que estão disponíveis.")
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        List<ProdutoResponseDTO> produtos = produtoService.listarDisponiveis();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um produto", description = "Atualiza os dados de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Produto ou Restaurante não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO produtoAtualizado = produtoService.atualizar(id, dto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Tornar produto indisponível", description = "Muda o status 'disponivel' de um produto para 'false'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do produto alterado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> tornarIndisponivel(@PathVariable Long id) {
        ProdutoResponseDTO produtoIndisponivel = produtoService.tornarIndisponivel(id);
        return ResponseEntity.ok(produtoIndisponivel);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar produtos por nome", description = "Retorna uma lista de produtos cujo nome contenha o texto buscado.")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/restaurante/{restauranteId}")
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