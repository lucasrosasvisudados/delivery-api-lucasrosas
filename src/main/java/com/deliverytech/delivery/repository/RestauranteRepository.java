package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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
}