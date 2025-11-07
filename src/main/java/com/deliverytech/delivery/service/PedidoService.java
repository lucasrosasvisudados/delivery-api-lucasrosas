package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
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
        // (As validações de campos @NotBlank, @NotNull, etc., são feitas pelo @Valid no Controller)
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + dto.getClienteId()));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + dto.getRestauranteId()));

        // 2. Mapear DTO para Entidade
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setItens(dto.getItens());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setObservacoes(dto.getObservacoes());

        // 3. Definir valores padrão (gerenciados pelo sistema)
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 15).toUpperCase()); // Gera um número de pedido
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        // 4. Salvar e retornar o DTO de resposta
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
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        // Validação de transição de status (exemplo)
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio");
        }
        
        // Regra de negócio: Não pode alterar status de pedido cancelado ou entregue
        if ("CANCELADO".equals(pedido.getStatus()) || "ENTREGUE".equals(pedido.getStatus())) {
             throw new IllegalStateException("Não é possível alterar o status de um pedido que já foi " + pedido.getStatus().toLowerCase());
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
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        
        // Regra de negócio: Não cancelar pedido já entregue
        if ("ENTREGUE".equals(pedido.getStatus())) {
            throw new IllegalStateException("Não é possível cancelar um pedido já entregue.");
        }

        // Evita cancelamento duplicado
        if ("CANCELADO".equals(pedido.getStatus())) {
            throw new IllegalStateException("Este pedido já está cancelado.");
        }

        pedido.setStatus("CANCELADO");
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(pedidoSalvo);
    }
}