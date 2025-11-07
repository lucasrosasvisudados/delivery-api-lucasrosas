package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.service.ProdutoService;
import jakarta.validation.Valid; // Import para @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /*
     * Cadastrar novo produto
     * (Refatorado para usar DTOs e @Valid)
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        try {
            ProdutoResponseDTO produtoSalvo = produtoService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (IllegalArgumentException e) {
            // Captura erro de "Restaurante não encontrado"
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /*
     * Listar todos os produtos disponíveis
     * (Refatorado para retornar DTO)
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        List<ProdutoResponseDTO> produtos = produtoService.listarDisponiveis();
        return ResponseEntity.ok(produtos);
    }

    /*
     * Buscar produto por ID
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Atualizar produto
     * (Refatorado para usar DTOs e @Valid)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProdutoRequestDTO dto) {
        try {
            ProdutoResponseDTO produtoAtualizado = produtoService.atualizar(id, dto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            // Captura erros de "Produto não encontrado" ou "Restaurante não encontrado"
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /*
     * Tornar produto indisponível (soft delete)
     * (Refatorado para retornar DTO)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> tornarIndisponivel(@PathVariable Long id) {
        try {
            ProdutoResponseDTO produtoIndisponivel = produtoService.tornarIndisponivel(id);
            return ResponseEntity.ok(produtoIndisponivel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /*
     * Buscar produtos por nome
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    /*
     * Listar produtos disponíveis de um restaurante específico
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoResponseDTO>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.listarDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }
}