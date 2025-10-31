package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Buscar produtos disponíveis
     * (Equivalente ao findByAtivoTrue de Cliente)
     */
    List<Produto> findByDisponivelTrue();

    /**
     * Buscar produtos por nome (contendo)
     * (Padrão de ClienteRepository)
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Buscar todos os produtos de um restaurante (disponíveis ou não)
     */
    List<Produto> findByRestauranteId(Long restauranteId);

    /**
     * Buscar produtos disponíveis de um restaurante específico
     */
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

    /**
     * Buscar produtos por categoria (contendo)
     */
    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);
}