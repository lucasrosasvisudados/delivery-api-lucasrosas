package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Cadastrar novo restaurante
     * (Refatorado para usar DTOs)
     */
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {
        // Validações de @NotBlank, @Min, etc., são tratadas pelo @Valid no Controller

        // Mapeia DTO para Entidade
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());

        // Definir como ativo por padrão
        restaurante.setAtivo(true);
        // Define avaliação padrão
        restaurante.setAvaliacao(0.0); // Nota inicial 0

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(restauranteSalvo);
    }

    /**
     * Buscar restaurante por ID
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public Optional<RestauranteResponseDTO> buscarPorId(Long id) {
        return restauranteRepository.findById(id).map(RestauranteResponseDTO::new);
    }

    /**
     * Listar todos os restaurantes ativos
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue().stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar dados do restaurante
     * (Refatorado para usar DTOs)
     */
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        // Validações de @NotBlank, @Min, etc., são tratadas pelo @Valid no Controller

        // Atualizar campos com base no DTO
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());
        
        // Campos não incluídos no DTO (como avaliacao) são preservados.

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(restauranteSalvo);
    }

    /**
     * Inativar restaurante (soft delete)
     * (Refatorado para retornar DTO)
     */
    public RestauranteResponseDTO inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        restaurante.inativar();
        Restaurante restauranteInativado = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(restauranteInativado);
    }

    /**
     * Buscar restaurantes por nome
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Buscar restaurantes por categoria
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria).stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    /*
     * O método validarDadosRestaurante() foi removido pois
     * as validações agora estão no RestauranteRequestDTO.
     */
}