package com.deliverytech.delivery.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.dto.ClienteRequestDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Cadastrar novo cliente
     */
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        // Validar email único
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        // Definir como ativo por padrão
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        return new ClienteResponseDTO(clienteRepository.save(cliente));
    }

    /**
     * Buscar cliente por ID
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Buscar cliente por email
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    /**
     * Listar todos os clientes ativos
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    /**
     * Atualizar dados do cliente
     */
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        // 1. Busca o cliente ou lança EntityNotFoundException (404)
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));

        // 2. Valida a regra de negócio do email (400)
        if (!cliente.getEmail().equals(dto.getEmail()) &&
                clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }

        // 3. Atualiza os campos da entidade com os dados do DTO
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        // 4. Salva e retorna o DTO de resposta
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return new ClienteResponseDTO(clienteSalvo);
    }

    /**
     * Inativar cliente (soft delete)
     */
public void inativar(Long id) {
        Cliente cliente = buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id)); // Alterado

        cliente.inativar();
        clienteRepository.save(cliente);
    }
    /**
     * Buscar clientes por nome
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Validações de negócio
    
    private void validarDadosCliente(ClienteRequestDTO cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (cliente.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
    }
    */

}