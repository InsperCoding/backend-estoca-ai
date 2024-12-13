package com.estocaai.backend.Controller;

import com.estocaai.backend.Classes.Produto;
import com.estocaai.backend.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Lista todos os produtos cadastrados
    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    // Retorna os detalhes de um produto específico
    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable String id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // Cadastra um novo produto
    @PostMapping
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    // Remove um produto
    @DeleteMapping("/{id}")
    public void deleteProduto(@PathVariable String id) {
        produtoRepository.deleteById(id);
    }
}
