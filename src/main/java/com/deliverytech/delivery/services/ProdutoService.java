package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;

import java.util.List;

public interface ProdutoService {

    ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto);

    ProdutoResponseDTO buscarPorId(Long id);

    List<ProdutoResponseDTO> listarDisponiveis();

    List<ProdutoResponseDTO> listarDisponiveisPorRestaurante(Long restauranteId);

    ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto);

    ProdutoResponseDTO tornarIndisponivel(Long id);

    List<ProdutoResponseDTO> buscarPorNome(String nome);
}