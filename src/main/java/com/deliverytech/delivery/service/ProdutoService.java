package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository; // Para validar o restaurante

    /**
     * Cadastrar novo produto
     */
    public Produto cadastrar(Produto produto) {
        // Validações de negócio
        validarDadosProduto(produto);

        // Define como disponível por padrão
        if (produto.getDisponivel() == null) {
            produto.setDisponivel(true);
        }

        return produtoRepository.save(produto);
    }

    /**
     * Buscar produto por ID
     */
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /**
     * Listar todos os produtos disponíveis
     */
    @Transactional(readOnly = true)
    public List<Produto> listarDisponiveis() {
        return produtoRepository.findByDisponivelTrue();
    }

    /**
     * Listar produtos disponíveis de um restaurante
     */
    @Transactional(readOnly = true)
    public List<Produto> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);
    }

    /**
     * Atualizar dados do produto
     */
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        // Validar os novos dados
        validarDadosProduto(produtoAtualizado);

        // Atualizar campos
        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setDisponivel(produtoAtualizado.getDisponivel());
        produto.setRestaurante(produtoAtualizado.getRestaurante()); // Atualiza a referência

        return produtoRepository.save(produto);
    }

    /**
     * Tornar produto indisponível (soft delete/inativação)
     * (Equivalente ao 'inativar' de Cliente)
     */
    public void tornarIndisponivel(Long id) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        // A entidade Produto não tem o método inativar(),
        // então definimos o campo 'disponivel' manualmente.
        produto.setDisponivel(false);
        produtoRepository.save(produto);
    }

    /**
     * Buscar produtos por nome
     */
    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Validações de negócio
     */
    private void validarDadosProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (produto.getRestaurante() == null || produto.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }

        // Valida se o Restaurante associado existe no banco
        // (Similar à validação do PedidoService)
        restauranteRepository.findById(produto.getRestaurante().getId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + produto.getRestaurante().getId()));
    }
}