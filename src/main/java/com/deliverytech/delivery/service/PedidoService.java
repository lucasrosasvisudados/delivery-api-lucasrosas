package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
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
    private ClienteRepository clienteRepository; // Para validar o Cliente

    @Autowired
    private RestauranteRepository restauranteRepository; // Para validar o Restaurante

    /**
     * Criar novo pedido
     * (Refatorado para usar DTOs)
     */
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        // 1. Validar e buscar entidades relacionadas
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + dto.getClienteId())); // Alterado

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId())); // Alterado

        // ... (mapeamento e save)
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setItens(dto.getItens());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setObservacoes(dto.getObservacoes());

        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 15).toUpperCase()); 
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }

    /**
     * Buscar pedido por ID
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public Optional<PedidoResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id).map(PedidoResponseDTO::new);
    }

    /**
     * Buscar pedido por Número do Pedido
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public Optional<PedidoResponseDTO> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido).map(PedidoResponseDTO::new);
    }

    /**
     * Listar todos os pedidos de um cliente
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Listar pedidos por status (ex: "PENDENTE", "CONFIRMADO", "ENTREGUE")
     * (Refatorado para retornar DTO)
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorStatus(String status) {
        return pedidoRepository.findByStatus(status.toUpperCase()).stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar status do pedido
     * (Refatorado para retornar DTO)
     */
    public PedidoResponseDTO atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id)); // Alterado

        // Validação de transição de status (exemplo)
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            throw new BusinessException("Status não pode ser nulo ou vazio"); // Mantido (regra de negócio)
        }
        
        // Regra de negócio: Não pode alterar status de pedido cancelado ou entregue
        if ("CANCELADO".equals(pedido.getStatus()) || "ENTREGUE".equals(pedido.getStatus())) {
             throw new BusinessException("Não é possível alterar o status de um pedido que já foi " + pedido.getStatus().toLowerCase()); // Mantido (regra de negócio)
        }

        pedido.setStatus(novoStatus.toUpperCase());
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }

    /**
     * Cancelar um pedido (define o status como "CANCELADO")
     * (Refatorado para retornar DTO)
     */
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id)); // Alterado
        
        // Regra de negócio: Não cancelar pedido já entregue
        if ("ENTREGUE".equals(pedido.getStatus())) {
            throw new BusinessException("Não é possível cancelar um pedido já entregue."); // Mantido (regra de negócio)
        }

        // Evita cancelamento duplicado
        if ("CANCELADO".equals(pedido.getStatus())) {
            throw new BusinessException("Este pedido já está cancelado."); // Mantido (regra de negócio)
        }

        pedido.setStatus("CANCELADO");
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }
}