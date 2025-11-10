package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.projection.RelatorioVendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    /**
     * Buscar restaurantes ativos
     */
    List<Restaurante> findByAtivoTrue();

    /**
     * Buscar restaurantes por nome (contendo)
     */
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    /**
     * Buscar restaurantes por categoria (contendo)
     */
    List<Restaurante> findByCategoriaContainingIgnoreCase(String categoria);

    /**
     * Buscar restaurantes com taxa de entrega menor ou igual ao valor.
     */
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa); // Corrigido

    /**
     * Buscar os 5 primeiros restaurantes ordenados por nome (A-Z).
     */
    List<Restaurante> findTop5ByOrderByNomeAsc();

        // No RestauranteRepository:
    @Query("SELECT r.nome as nomeRestaurante, " +
            "SUM(p.valorTotal) as totalVendas, " +
            "COUNT(p.id) as quantidePedidos " +
            "FROM Restaurante r " +
            "LEFT JOIN Pedido p ON r.id = p.restaurante.id " +
            "GROUP BY r.id, r.nome")
    List<RelatorioVendas> relatorioVendasPorRestaurante();
}