package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.PedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + dto.getClienteId()));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId()));

        Pedido pedido = modelMapper.map(dto, Pedido.class);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 15).toUpperCase());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorNumeroPedido(String numeroPedido) {
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + numeroPedido));
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorStatus(String status) {
        StatusPedido statusEnum = parseStatus(status);
        return pedidoRepository.findByStatus(statusEnum).stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));

        StatusPedido novoStatusEnum = parseStatus(novoStatus);

        if (pedido.getStatus() == StatusPedido.CANCELADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
             throw new BusinessException("Não é possível alterar o status de um pedido que já foi " + pedido.getStatus().getDescricao().toLowerCase());
        }

        pedido.setStatus(novoStatusEnum);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
        
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Não é possível cancelar um pedido já entregue.");
        }
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new BusinessException("Este pedido já está cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    private StatusPedido parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("Status não pode ser nulo ou vazio");
        }
        try {
            return StatusPedido.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Status inválido: " + status);
        }
    }
}