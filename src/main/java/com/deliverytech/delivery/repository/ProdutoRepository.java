package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // Import adicionado
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

    /**
     * NOVO: Buscar produtos com preço menor ou igual ao valor.
     * (Conforme Roteiro - Atividade 1.3)
     */
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);
}