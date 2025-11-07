package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.service.RestauranteService;
import jakarta.validation.Valid; // Import para @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    /*
     * Cadastrar novo restaurante
     * (Refatorado para usar DTOs e @Valid)
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody RestauranteRequestDTO dto) {
        try {
            RestauranteResponseDTO restauranteSalvo = restauranteService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /*
     * Listar todos os restaurantes ativos
     * (Refatorado para retornar DTO)
     */
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }

    /*
     * Buscar restaurante por ID
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Atualizar restaurante
     * (Refatorado para usar DTOs e @Valid)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody RestauranteRequestDTO dto) {
        try {
            RestauranteResponseDTO restauranteAtualizado = restauranteService.atualizar(id, dto);
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /*
     * Inativar restaurante (soft delete)
     * (Refatorado para retornar DTO)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            RestauranteResponseDTO restauranteInativado = restauranteService.inativar(id);
            return ResponseEntity.ok(restauranteInativado); // Retorna o objeto inativado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /*
     * Buscar restaurantes por nome
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(restaurantes);
    }

    /*
     * Buscar restaurantes por categoria
     * (Refatorado para retornar DTO)
     */
    @GetMapping("/categoria")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@RequestParam String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }
}