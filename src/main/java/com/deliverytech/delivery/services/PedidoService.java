package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import java.util.List;

public interface PedidoService {

    PedidoResponseDTO criarPedido(PedidoRequestDTO dto);

    PedidoResponseDTO buscarPorId(Long id);

    PedidoResponseDTO buscarPorNumeroPedido(String numeroPedido);

    List<PedidoResponseDTO> listarPorCliente(Long clienteId);

    List<PedidoResponseDTO> listarPorStatus(String status);

    PedidoResponseDTO atualizarStatus(Long id, String novoStatus);

    PedidoResponseDTO cancelarPedido(Long id);
}