package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.ProdutoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId()));

        Produto produto = modelMapper.map(dto, Produto.class);
        produto.setRestaurante(restaurante);
        produto.setDisponivel(true);

        Produto produtoSalvo = produtoRepository.save(produto);
        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarDisponiveis() {
        return produtoRepository.findByDisponivelTrue().stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId).stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + dto.getRestauranteId()));

        // Mapeia o DTO para a entidade existente
        modelMapper.map(dto, produto);
        produto.setRestaurante(restaurante); // Garante a associação correta

        Produto produtoSalvo = produtoRepository.save(produto);
        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO tornarIndisponivel(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));

        produto.setDisponivel(false);
        Produto produtoSalvo = produtoRepository.save(produto);
        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }
}