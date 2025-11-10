package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import java.util.List;

public interface RestauranteService {

    RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto);

    RestauranteResponseDTO buscarPorId(Long id);

    List<RestauranteResponseDTO> listarAtivos();

    RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto);

    RestauranteResponseDTO inativar(Long id);

    List<RestauranteResponseDTO> buscarPorNome(String nome);

    List<RestauranteResponseDTO> buscarPorCategoria(String categoria);
}