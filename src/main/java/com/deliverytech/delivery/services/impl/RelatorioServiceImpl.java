package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.response.RelatorioResponseDTO;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.projection.RelatorioVendas;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.RelatorioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Relatórios são geralmente 'read-only'
public class RelatorioServiceImpl implements RelatorioService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public RelatorioResponseDTO relatorioVendasPorRestauranteId(Long restauranteId) {
        
        // 1. Chama a nova query do repositório
        RelatorioVendas projecao = restauranteRepository.relatorioVendasPorRestauranteId(restauranteId)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + restauranteId));
        
        // 2. Mapeia a projeção única para o DTO
        return modelMapper.map(projecao, RelatorioResponseDTO.class);
    }
}