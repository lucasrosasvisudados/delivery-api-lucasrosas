package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.response.RelatorioResponseDTO;
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
    public List<RelatorioResponseDTO> relatorioVendasPorRestaurante() {
        
        // 1. Chama a query de projeção do repositório
        List<RelatorioVendas> projecoes = restauranteRepository.relatorioVendasPorRestaurante();

        // 2. Mapeia a lista de projeções (Interfaces) para a lista de DTOs (Classes)
        return projecoes.stream()
                .map(projecao -> modelMapper.map(projecao, RelatorioResponseDTO.class))
                .collect(Collectors.toList());
    }
}