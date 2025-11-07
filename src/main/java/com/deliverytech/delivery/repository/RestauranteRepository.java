package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.math.BigDecimal; // Removido
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    /**
     * Buscar restaurantes ativos
     * (Padrão de ClienteRepository)
     */
    List<Restaurante> findByAtivoTrue();

    /**
     * Buscar restaurantes por nome (contendo)
     * (Padrão de ClienteRepository)
     */
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    /**
     * Buscar restaurantes por categoria (contendo)
     * (Método específico de Restaurante)
     */
    List<Restaurante> findByCategoriaContainingIgnoreCase(String categoria);

    /**
     * CORRIGIDO: Buscar restaurantes com taxa de entrega menor ou igual ao valor.
     * (Trocado BigDecimal por Double para bater com a Entidade)
     */
    List<Restaurante> findByTaxaEntregaLessThanEqual(Double taxa); // Corrigido

    /**
     * NOVO: Buscar os 5 primeiros restaurantes ordenados por nome (A-Z).
     * (Conforme Roteiro - Atividade 1.2)
     */
    List<Restaurante> findTop5ByOrderByNomeAsc();
}