package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Pedido;
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
     */
    public Pedido criarPedido(Pedido pedido) {
        // Validações de negócio
        validarDadosPedido(pedido);

        // Definir valores padrão
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 15).toUpperCase()); // Gera um número de pedido
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        return pedidoRepository.save(pedido);
    }

    /**
     * Buscar pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    /**
     * Buscar pedido por Número do Pedido
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    /**
     * Listar todos os pedidos de um cliente
     */
    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    /**
     * Listar pedidos por status (ex: "PENDENTE", "CONFIRMADO", "ENTREGUE")
     */
    @Transactional(readOnly = true)
    public List<Pedido> listarPorStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    /**
     * Atualizar status do pedido
     */
    public Pedido atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        // Aqui você poderia adicionar lógica para validar a transição de status
        // (ex: não pode ir de "ENTREGUE" para "PENDENTE")
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio");
        }

        pedido.setStatus(novoStatus.toUpperCase());
        return pedidoRepository.save(pedido);
    }

    /**
     * Cancelar um pedido (lógica similar ao 'inativar' do Cliente)
     */
    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        
        // Regra de negócio: Não cancelar pedido já entregue
        if ("ENTREGUE".equals(pedido.getStatus())) {
            throw new IllegalStateException("Não é possível cancelar um pedido já entregue.");
        }

        pedido.setStatus("CANCELADO");
        pedidoRepository.save(pedido);
    }

    /**
     * Validações de negócio (similar ao 'validarDadosCliente' do ClienteService)
     */
    private void validarDadosPedido(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (pedido.getRestaurante() == null || pedido.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }

        if (pedido.getItens() == null || pedido.getItens().trim().isEmpty()) {
            throw new IllegalArgumentException("A lista de itens é obrigatória");
        }

        if (pedido.getValorTotal() == null || pedido.getValorTotal() <= 0) {
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }

        // Valida se o Cliente e o Restaurante existem no banco
        clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + pedido.getCliente().getId()));

        restauranteRepository.findById(pedido.getRestaurante().getId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + pedido.getRestaurante().getId()));
    }
}