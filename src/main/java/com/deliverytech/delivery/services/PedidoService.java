package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido; // Import adicionado
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Criar novo pedido
     */
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + dto.getClienteId()));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId()));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setItens(dto.getItens());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setObservacoes(dto.getObservacoes());

        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 15).toUpperCase());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE); // Alterado para Enum

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }

    /**
     * Buscar pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<PedidoResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id).map(PedidoResponseDTO::new);
    }

    /**
     * Buscar pedido por Número do Pedido
     */
    @Transactional(readOnly = true)
    public Optional<PedidoResponseDTO> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido).map(PedidoResponseDTO::new);
    }

    /**
     * Listar todos os pedidos de um cliente
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Listar pedidos por status (ex: "PENDENTE", "CONFIRMADO", "ENTREGUE")
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorStatus(String status) {
        // Converte a String de entrada para o Enum
        StatusPedido statusEnum = parseStatus(status);
        return pedidoRepository.findByStatus(statusEnum).stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar status do pedido
     */
    public PedidoResponseDTO atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));

        // Converte e valida a String do novo status
        StatusPedido novoStatusEnum = parseStatus(novoStatus);

        // Regra de negócio: Não pode alterar status de pedido cancelado ou entregue
        if (pedido.getStatus() == StatusPedido.CANCELADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
             throw new BusinessException("Não é possível alterar o status de um pedido que já foi " + pedido.getStatus().getDescricao().toLowerCase());
        }

        pedido.setStatus(novoStatusEnum);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }

    /**
     * Cancelar um pedido (define o status como "CANCELADO")
     */
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
        
        // Regra de negócio: Não cancelar pedido já entregue
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Não é possível cancelar um pedido já entregue.");
        }

        // Evita cancelamento duplicado
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new BusinessException("Este pedido já está cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELADO); // Alterado para Enum
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }

    /**
     * Método utilitário para converter String em Enum StatusPedido
     */
    private StatusPedido parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("Status não pode ser nulo ou vazio");
        }
        try {
            // Converte a string (ex: "PENDENTE") para o enum (StatusPedido.PENDENTE)
            return StatusPedido.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Lança um erro de negócio se a string não for um enum válido
            throw new BusinessException("Status inválido: " + status);
        }
    }
}