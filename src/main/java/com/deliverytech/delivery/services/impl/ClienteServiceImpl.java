package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.services.ClienteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        if(clienteRepository.existsByEmail(dto.getEmail())){
            throw new BusinessException("Email já cadastrado" + dto.getEmail());
        }
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        Cliente saveSalvo = clienteRepository.save(cliente);

        return modelMapper.map(saveSalvo, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com id: " + id));

        return modelMapper.map(clienteExistente, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com id: " + id));
        if(clienteRepository.existsByEmail(dto.getEmail())){
            throw new BusinessException("Email já cadastrado" + dto.getEmail());
        }
        if(dto.getNome() == null || dto.getNome().isEmpty()){
            throw new BusinessException("Nome não pode ser vazio");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new BusinessException("Email não pode ser vazio");
        }
        clienteExistente.setNome(dto.getNome());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setTelefone(dto.getTelefone());

        Cliente saveAtualizado = clienteRepository.save(clienteExistente);

        return modelMapper.map(saveAtualizado, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO ativarDesativar(Long id) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com id: " + id));
        if (clienteExistente.getAtivo() == false) {
            clienteExistente.setAtivo(true);    
        }
        clienteExistente.setAtivo(false);
        return null;
    }

    @Override
    public List<ClienteResponseDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue().stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO buscarPorEmail(String email) {
        Cliente clienteExistente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com email: " + email));

        return modelMapper.map(clienteExistente, ClienteResponseDTO.class);
    }
}