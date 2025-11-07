package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.enums.StatusPedido;

import java.time.LocalDateTime; // Import adicionado
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedido pelo número (método derivado)
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    // Verificar se número do pedido já existe
    boolean existsByNumeroPedido(String numeroPedido);

    // Buscar pedidos por status (Corrigido para usar o Enum)
    List<Pedido> findByStatus(StatusPedido status);

    // Buscar todos os pedidos de um cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Buscar todos os pedidos de um restaurante
    List<Pedido> findByRestauranteId(Long restauranteId);

    /**
     * NOVO: Buscar os 10 pedidos mais recentes, ordenados pela data do pedido.
     * (Conforme Roteiro - Atividade 1.4)
     */
    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    /**
     * NOVO: Buscar pedidos entre um período (data de início e fim).
     * (Conforme Roteiro - Atividade 1.4)
     */
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
}