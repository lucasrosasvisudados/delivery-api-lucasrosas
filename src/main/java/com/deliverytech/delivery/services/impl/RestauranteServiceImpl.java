package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.RestauranteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper; 

    @Override
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {
        // Mapeia DTO para Entidade
        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);

        // Define regras de negócio
        restaurante.setAtivo(true);
        restaurante.setAvaliacao(0.0);

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        // Mapeia Entidade para DTO de Resposta
        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {
        return restauranteRepository.findById(id)
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue().stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));

        // Atualiza os campos da entidade com base no DTO
        modelMapper.map(dto, restaurante);
        
        // Garante que campos não presentes no DTO (como avaliacao) sejam preservados

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));

        restaurante.inativar();
        Restaurante restauranteInativado = restauranteRepository.save(restaurante);
        return modelMapper.map(restauranteInativado, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria).stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }
}