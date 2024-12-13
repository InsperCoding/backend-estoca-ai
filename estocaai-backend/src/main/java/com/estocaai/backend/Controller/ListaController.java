package com.estocaai.backend.Controller;

import com.estocaai.backend.Classes.Lista;
import com.estocaai.backend.Classes.Produto;
import com.estocaai.backend.Repository.ListaRepository;
import com.estocaai.backend.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/listas")
public class ListaController {

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Lista todas as listas
    @GetMapping
    public List<Lista> getAllListas() {
        return listaRepository.findAll();
    }

    // Cria uma nova lista
    @PostMapping
    public Lista createLista(@RequestBody Lista lista) {
        return listaRepository.save(lista);
    }

    // Retorna os detalhes de uma lista específica
    @GetMapping("/{id}")
    public Lista getListaById(@PathVariable String id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
    }

    // Remove uma lista
    @DeleteMapping("/{id}")
    public void deleteLista(@PathVariable String id) {
        listaRepository.deleteById(id);
    }

    // Retorna os produtos de uma lista específica
    @GetMapping("/{id}/produtos")
    public List<Produto> getProdutosInLista(@PathVariable String id) {
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        return produtoRepository.findAllById(lista.getProdutosIds());
    }

    // Adiciona um produto a uma lista
    @PostMapping("/{id}/produtos/{id_produto}")
    public Lista addProdutoToLista(@PathVariable String id, @PathVariable String id_produto) {
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        lista.getProdutosIds().add(id_produto);
        return listaRepository.save(lista);
    }

    // Remove um produto de uma lista
    @DeleteMapping("/{id}/produtos/{id_produto}")
    public Lista removeProdutoFromLista(@PathVariable String id, @PathVariable String id_produto) {
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        lista.getProdutosIds().remove(id_produto);
        return listaRepository.save(lista);
    }
}
