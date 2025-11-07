package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository; // Para validar o restaurante

    /**
     * Cadastrar novo produto
     * (Refatorado para usar DTOs)
     */
        public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        // ...

        // 1. Valida se o Restaurante associado existe
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId())); // Alterado

        // ... (mapeamento e save)
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setRestaurante(restaurante); 
        produto.setDisponivel(true);

        Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo);
    }

    /**
     * Buscar produto por ID
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public Optional<ProdutoResponseDTO> buscarPorId(Long id) {
        return produtoRepository.findById(id).map(ProdutoResponseDTO::new);
    }

    /**
     * Listar todos os produtos disponíveis
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarDisponiveis() {
        return produtoRepository.findByDisponivelTrue().stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Listar produtos disponíveis de um restaurante
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId).stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar dados do produto
     * (Refatorado para usar DTOs)
     */
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id)); // Alterado

        // Valida se o novo Restaurante associado existe
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId())); // Alterado

        // ... (atualização de campos e save)
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setRestaurante(restaurante); 

        Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo);
    }

    /**
     * Tornar produto indisponível (soft delete/inativação)
     * (Refatorado para retornar DTO)
     */
    public ProdutoResponseDTO tornarIndisponivel(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id)); // Alterado

        produto.setDisponivel(false);
        Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo);
    }

    /**
     * Buscar produtos por nome
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /*
     * O método validarDadosProduto() foi removido pois
     * as validações agora estão no ProdutoRequestDTO.
     */
}