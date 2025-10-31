package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Cadastrar novo restaurante
     * (Padrão de ClienteService)
     */
    public Restaurante cadastrar(Restaurante restaurante) {
        // Validações de negócio
        validarDadosRestaurante(restaurante);

        // Definir como ativo por padrão
        restaurante.setAtivo(true);
        
        // Define avaliação padrão se não for fornecida
        if (restaurante.getAvaliacao() == null) {
            restaurante.setAvaliacao(0.0); // Nota inicial 0
        }

        return restauranteRepository.save(restaurante);
    }

    /**
     * Buscar restaurante por ID
     * (Padrão de ClienteService)
     */
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    /**
     * Listar todos os restaurantes ativos
     * (Padrão de ClienteService)
     */
    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    /**
     * Atualizar dados do restaurante
     * (Padrão de ClienteService)
     */
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        // Validar os novos dados
        validarDadosRestaurante(restauranteAtualizado);

        // Atualizar campos
        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCategoria(restauranteAtualizado.getCategoria());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        restaurante.setTaxaEntrega(restauranteAtualizado.getTaxaEntrega());
        
        // Avaliação só deve ser atualizada se for fornecida
        if (restauranteAtualizado.getAvaliacao() != null) {
            restaurante.setAvaliacao(restauranteAtualizado.getAvaliacao());
        }

        return restauranteRepository.save(restaurante);
    }

    /**
     * Inativar restaurante (soft delete)
     * (Padrão de ClienteService)
     */
    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        restaurante.inativar();
        restauranteRepository.save(restaurante);
    }

    /**
     * Buscar restaurantes por nome
     * (Padrão de ClienteService)
     */
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Buscar restaurantes por categoria
     */
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    /**
     * Validações de negócio
     * (Padrão de ClienteService)
     */
    private void validarDadosRestaurante(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (restaurante.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        if (restaurante.getTaxaEntrega() == null) {
            throw new IllegalArgumentException("Taxa de entrega é obrigatória (pode ser 0)");
        }
        if (restaurante.getTaxaEntrega() < 0) {
            throw new IllegalArgumentException("Taxa de entrega não pode ser negativa");
        }
        if (restaurante.getEndereco() == null || restaurante.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
    }
}