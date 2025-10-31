package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedido pelo número (método derivado)
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    // Verificar se número do pedido já existe
    boolean existsByNumeroPedido(String numeroPedido);

    // Buscar pedidos por status
    List<Pedido> findByStatus(String status);

    // Buscar todos os pedidos de um cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Buscar todos os pedidos de um restaurante
    List<Pedido> findByRestauranteId(Long restauranteId);
}