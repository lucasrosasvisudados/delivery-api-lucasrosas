package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // Import adicionado
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
     * NOVO: Buscar restaurantes com taxa de entrega menor ou igual ao valor.
     * (Conforme Roteiro - Atividade 1.2)
     */
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    /**
     * NOVO: Buscar os 5 primeiros restaurantes ordenados por nome (A-Z).
     * (Conforme Roteiro - Atividade 1.2)
     */
    List<Restaurante> findTop5ByOrderByNomeAsc();
}