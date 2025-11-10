package com.deliverytech.delivery.services;

import java.util.List;
import com.deliverytech.delivery.dto.response.RelatorioResponseDTO;

public interface RelatorioService {

    //Método para o relatório específico
    RelatorioResponseDTO relatorioVendasPorRestauranteId(Long restauranteId);
}
