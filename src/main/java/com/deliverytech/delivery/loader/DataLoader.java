package com.deliverytech.delivery.loader;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// import java.math.BigDecimal; // Removido
import java.time.LocalDateTime;
import java.util.List;

/**
 * ATIVIDADE 2: Testes de Persistência com CommandLineRunner.
 * Esta classe é executada automaticamente na inicialização do Spring Boot.
 * Ela insere dados de teste e valida as consultas dos repositórios.
 */
@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- [INICIANDO DATA LOADER] ---");

        // 1. Carregar Dados (Atividade 2.1)
        carregarDados();

        // 2. Testar Consultas (Atividade 2.2 e Cenários de Teste)
        testarConsultas();

        System.out.println("--- [DATA LOADER FINALIZADO] ---");
    }

    private void carregarDados() {
        System.out.println("Inserindo dados de teste...");

        // 1.1 Clientes (3)
        Cliente c1 = new Cliente(null, "João Silva", "joao@email.com", "(11) 99999-1111", "Rua A, 123", LocalDateTime.now(), true);
        Cliente c2 = new Cliente(null, "Maria Santos", "maria@email.com", "(11) 99999-2222", "Rua B, 456", LocalDateTime.now(), true);
        Cliente c3 = new Cliente(null, "Pedro Oliveira", "pedro@email.com", "(11) 99999-3333", "Rua C, 789", LocalDateTime.now(), true);
        clienteRepository.saveAll(List.of(c1, c2, c3));

        // 1.2 Restaurantes (2)
        Restaurante r1 = new Restaurante(null, "Pizzaria Bella", "Italiana", "Av. Paulista, 1000", "(11) 3333-1111", 5.00, 4.5, true);
        Restaurante r2 = new Restaurante(null, "Burger House", "Hamburgueria", "Rua Augusta, 500", "(11) 3333-2222", 3.50, 4.2, true);
        restauranteRepository.saveAll(List.of(r1, r2));

        // 1.3 Produtos (5)
        Produto p1 = new Produto(null, "Pizza Margherita", "Molho, mussarela, manjericão", 35.90, "Pizza", true, r1);
        Produto p2 = new Produto(null, "Pizza Calabresa", "Molho, mussarela, calabresa", 38.90, "Pizza", true, r1);
        Produto p3 = new Produto(null, "X-Burger", "Hambúrguer, queijo, alface, tomate", 18.90, "Hambúrguer", true, r2);
        Produto p4 = new Produto(null, "X-Bacon", "Hambúrguer, queijo, bacon", 22.90, "Hambúrguer", true, r2);
        Produto p5 = new Produto(null, "Batata Frita", "Porção Média", 12.90, "Acompanhamento", true, r2);
        produtoRepository.saveAll(List.of(p1, p2, p3, p4, p5));

        // 1.4 Pedidos (2)
        Pedido ped1 = new Pedido(null, "PED1001", LocalDateTime.now().minusDays(1), StatusPedido.ENTREGUE, 54.80, "Sem cebola", "Pizza Margherita", c1, r1);
        Pedido ped2 = new Pedido(null, "PED1002", LocalDateTime.now(), StatusPedido.PENDENTE, 41.80, "", "X-Burger, Batata Frita", c2, r2);
        pedidoRepository.saveAll(List.of(ped1, ped2));

        System.out.println("Dados de teste inseridos.");
    }

    private void testarConsultas() {
        System.out.println("\nTestando consultas dos repositórios...");

        // --- Testes ClienteRepository ---
        System.out.println("Cenário 1: Busca Cliente por Email (joao@email.com):");
        clienteRepository.findByEmail("joao@email.com")
                .ifPresent(c -> System.out.println("Resultado: " + c.getNome()));

        System.out.println("Teste: Clientes Ativos:");
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        System.out.println("Resultado: Encontrados " + clientesAtivos.size() + " clientes ativos.");

        System.out.println("Teste: Cliente por Nome (Contendo 'Maria'):");
        List<Cliente> clientesNome = clienteRepository.findByNomeContainingIgnoreCase("Maria");
        System.out.println("Resultado: " + clientesNome.get(0).getNome());

        System.out.println("Teste: Email 'maria@email.com' existe?");
        System.out.println("Resultado: " + clienteRepository.existsByEmail("maria@email.com"));


        // --- Testes RestauranteRepository ---
        System.out.println("Cenário 4: Restaurantes por Taxa (<= R$ 5,00):");
        // CORRIGIDO: Trocado 'new BigDecimal("5.00")' por '5.00' (Double)
        List<Restaurante> restsTaxa = restauranteRepository.findByTaxaEntregaLessThanEqual(5.00);
        System.out.println("Resultado: Encontrados " + restsTaxa.size() + " restaurantes.");
        restsTaxa.forEach(r -> System.out.println(" - " + r.getNome() + " (Taxa: " + r.getTaxaEntrega() + ")"));

        System.out.println("Teste: Top 5 Restaurantes por Nome (A-Z):");
        List<Restaurante> top5 = restauranteRepository.findTop5ByOrderByNomeAsc();
        top5.forEach(r -> System.out.println(" - " + r.getNome()));


        // --- Testes ProdutoRepository ---
        System.out.println("Cenário 2: Produtos por Restaurante (ID 1 - Pizzaria Bella):");
        // Nota: IDs são gerados automaticamente. Se r1 não for ID=1, este teste pode falhar.
        // Vamos buscar pelo objeto Restaurante para garantir.
        Restaurante r1 = restauranteRepository.findByNomeContainingIgnoreCase("Pizzaria Bella").get(0);
        List<Produto> produtosRest = produtoRepository.findByRestauranteId(r1.getId());
        System.out.println("Resultado: Encontrados " + produtosRest.size() + " produtos.");
        produtosRest.forEach(p -> System.out.println(" - " + p.getNome()));

        System.out.println("Teste: Produtos por Preço (<= R$ 20,00):");
        // CORRIGIDO: Trocado 'new BigDecimal("20.00")' por '20.00' (Double)
        List<Produto> prodsPreco = produtoRepository.findByPrecoLessThanEqual(20.00);
        System.out.println("Resultado: Encontrados " + prodsPreco.size() + " produtos.");
        prodsPreco.forEach(p -> System.out.println(" - " + p.getNome() + " (Preço: " + p.getPreco() + ")"));


        // --- Testes PedidoRepository ---
        System.out.println("Cenário 3: Pedidos Recentes (Top 10):");
        List<Pedido> pedidosRecentes = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        System.out.println("Resultado: Encontrados " + pedidosRecentes.size() + " pedidos.");
        pedidosRecentes.forEach(p -> System.out.println(" - " + p.getNumeroPedido() + " (Status: " + p.getStatus() + ")"));

        System.out.println("Teste: Pedidos por Status (PENDENTE):");
        List<Pedido> pedidosStatus = pedidoRepository.findByStatus(StatusPedido.PENDENTE);
        System.out.println("Resultado: " + pedidosStatus.size() + " pedido(s) pendente(s).");

        System.out.println("Teste: Pedidos das últimas 48h:");
        LocalDateTime inicio = LocalDateTime.now().minusHours(48);
        LocalDateTime fim = LocalDateTime.now();
        List<Pedido> pedidosPeriodo = pedidoRepository.findByDataPedidoBetween(inicio, fim);
        System.out.println("Resultado: " + pedidosPeriodo.size() + " pedido(s) encontrados no período.");

        System.out.println("\nTestes de consulta finalizados.");
    }
}